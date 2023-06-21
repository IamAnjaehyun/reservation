package com.jaehyun.reservation.user.favorite.store.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.favorite.domain.Favorite;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FavoriteStore extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long favoriteStoreId; //pk

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FAVORITE_ID")
  private Favorite favorite; //부모ID

  @JsonManagedReference
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "STORE_ID")
  private FavoriteStore store; //식당ID
}
