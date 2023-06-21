package com.jaehyun.reservation.user.favorite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.favorite.store.domain.FavoriteStore;
import com.jaehyun.reservation.user.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Favorite extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long favoriteId; //사람 1명당 1개의 favorite 테이블 존재.

  @OneToOne
  @JoinColumn(name = "USER_ID")
  private User user; //유저id

  @Builder.Default
  @JsonBackReference
  @OneToMany(mappedBy = "favorite", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<FavoriteStore> favoriteStoreList; //자주가는 식당 목록
}
