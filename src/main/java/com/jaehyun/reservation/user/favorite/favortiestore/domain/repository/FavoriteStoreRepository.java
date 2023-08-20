package com.jaehyun.reservation.user.favorite.favortiestore.domain.repository;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.user.favorite.domain.Favorite;
import com.jaehyun.reservation.user.favorite.favortiestore.domain.entity.FavoriteStore;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore, Long> {
  Optional<FavoriteStore> findByStoreAndFavorite(Store store, Favorite favorite);
  boolean existsByStoreAndFavorite(Store store, Favorite favorite);
  List<FavoriteStore> findAllByFavorite(Favorite favorite);
  void deleteAllByFavorite(Favorite favorite);
}
