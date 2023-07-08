package com.jaehyun.reservation.user.favorite.service;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.exception.impl.favorite.AlreadyExistFavoriteException;
import com.jaehyun.reservation.global.exception.impl.favorite.NotFavoriteStoreException;
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
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

  private final StoreRepository storeRepository;
  private final FavoriteRepository favoriteRepository;
  private final FavoriteStoreRepository favoriteStoreRepository;
  private final UserRepository userRepository;

  public Long addStoreToFavorite(Long storeId, Principal principal) {

    //NotFoundStore 만들기
    Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
    User user = userRepository.findByLoginId(principal.getName()).get();
    Favorite favorite = favoriteRepository.findByUser(user);

    if (favorite == null) {
      favorite = createFavorite(user);
      favoriteRepository.saveAndFlush(favorite);
    }
    boolean isExist = favoriteStoreRepository.existsByStoreAndFavorite(store, favorite);
    if (isExist) {
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

  public void deleteStoreFromFavorite(Long storeId, Principal principal) {
    //상품 뒤에 달려있으면 상품만 삭제 아니면 전체삭제
    User user = userRepository.findByLoginId(principal.getName()).get();
    Favorite favorite = favoriteRepository.findByUser(user);

    //storeId 없으면 전체 삭제
    if (storeId == null) {
      List<FavoriteStore> favoriteStoreList = favoriteStoreRepository.findAllByFavorite(favorite);
      for (FavoriteStore favoriteStore : favoriteStoreList) {
        Store store = favoriteStore.getStore();
        store.setFavoriteCount(favoriteStore.getStore().getFavoriteCount() - 1);
        storeRepository.saveAndFlush(store);
      }
      favoriteRepository.delete(favorite);
    } else {
      Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
      FavoriteStore favoriteStore = favoriteStoreRepository.findByStoreAndFavorite(store, favorite)
          .orElseThrow(NotFavoriteStoreException::new);
      store.setFavoriteCount(store.getFavoriteCount() - 1);
      storeRepository.save(store);
      favoriteStoreRepository.delete(favoriteStore);
    }
  }

  @Transactional
  public Favorite createFavorite(User user) {
    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setFavoriteStoreList(new ArrayList<>());
    return favorite;
  }

}
