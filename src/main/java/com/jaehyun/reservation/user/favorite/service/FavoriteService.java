package com.jaehyun.reservation.user.favorite.service;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.exception.impl.favorite.AlreadyExistFavoriteException;
import com.jaehyun.reservation.global.exception.impl.favorite.NotFavoriteStoreException;
import com.jaehyun.reservation.global.exception.impl.store.CantFindStoreException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.user.favorite.domain.Favorite;
import com.jaehyun.reservation.user.favorite.domain.dto.FavoriteResDto;
import com.jaehyun.reservation.user.favorite.domain.repository.FavoriteRepository;
import com.jaehyun.reservation.user.favorite.store.domain.dto.FavoriteStoreResDto;
import com.jaehyun.reservation.user.favorite.store.domain.entity.FavoriteStore;
import com.jaehyun.reservation.user.favorite.store.domain.repository.FavoriteStoreRepository;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

  private final StoreRepository storeRepository;
  private final FavoriteRepository favoriteRepository;
  private final FavoriteStoreRepository favoriteStoreRepository;
  private final UserRepository userRepository;

  @Transactional
  public synchronized Long addStoreToFavorite(Long storeId, Principal principal) {

    Store store = storeRepository.findById(storeId).orElseThrow(CantFindStoreException::new);
    User user = userRepository.findByLoginId(principal.getName()).get();
    Favorite favorite = favoriteRepository.findByUser(user);

    if (favorite == null) {
      favorite = createFavorite(user);
      favoriteRepository.saveAndFlush(favorite);
    } else if (favoriteStoreRepository.existsByStoreAndFavorite(store, favorite)) {
      throw new AlreadyExistFavoriteException();
    }

    FavoriteStore favoriteStore = new FavoriteStore();
    favoriteStore.setFavorite(favorite);
    favoriteStore.setStore(store);
    store.setFavoriteCount(store.getFavoriteCount() + 1);

    storeRepository.saveAndFlush(store);
    favoriteStoreRepository.saveAndFlush(favoriteStore);

    return storeId;
  }

  public FavoriteResDto getFavoriteList(Principal principal) {
    User user = userRepository.findByLoginId(principal.getName()).get();
    Favorite favorite = favoriteRepository.findByUser(user);
    List<FavoriteStore> favoriteStores = favoriteStoreRepository.findAllByFavorite(favorite);

    List<FavoriteStoreResDto> favoriteStoreResDtos = favoriteStores.stream()
        .map(FavoriteStoreResDto::fromFavoriteStoreResDto)
        .collect(Collectors.toList());

    return FavoriteResDto.builder()
        .id(favorite.getId())
        .userName(user.getName())
        .favoriteStoreResDto(favoriteStoreResDtos)
        .build();
  }

  @Transactional
  public synchronized void deleteStoreFromFavorite(Long storeId, Principal principal, boolean deleteAll) {
    //상품 뒤에 달려있으면 상품만 삭제 아니면 전체삭제
    User user = userRepository.findByLoginId(principal.getName()).get();
    Favorite favorite = favoriteRepository.findByUser(user);

    if (deleteAll) {
      List<FavoriteStore> favoriteStoreList = favoriteStoreRepository.findAllByFavorite(favorite);
      //TODO : favoriteCount를 redis에 캐시형태로 저장하여 변화시킬 때 cache를 날려 DB I/O 줄여야 함
      for (FavoriteStore favoriteStore : favoriteStoreList) {
        Store store = favoriteStore.getStore();
        store.setFavoriteCount(favoriteStore.getStore().getFavoriteCount() - 1);

        favoriteStoreRepository.deleteAllByFavorite(favorite);
        storeRepository.saveAndFlush(store);
      }
    } else {
      Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
      FavoriteStore favoriteStore = favoriteStoreRepository.findByStoreAndFavorite(store, favorite)
          .orElseThrow(NotFavoriteStoreException::new);
      store.setFavoriteCount(store.getFavoriteCount() - 1);
      storeRepository.save(store);
      favoriteStoreRepository.delete(favoriteStore);
    }
  }

  public Favorite createFavorite(User user) {
    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setFavoriteStoreList(new ArrayList<>());
    return favorite;
  }

}
