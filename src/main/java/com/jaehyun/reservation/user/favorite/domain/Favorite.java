package com.jaehyun.reservation.user.favorite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.favorite.store.domain.FavoriteStore;
import com.jaehyun.reservation.user.user.domain.User;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAVORITE")
public class Favorite extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long favoriteId; //사람 1명당 1개의 favorite 테이블 존재.

  @OneToOne
  @JoinColumn(name = "USER_ID")
  private User user; //유저id

  @JsonBackReference
  @OneToMany(mappedBy = "favorite", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<FavoriteStore> favoriteStoreList; //자주가는 식당 목록
}
