package com.jaehyun.reservation.user.favorite.favortiestore.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.favorite.domain.Favorite;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "FAVORITE_STORE")
public class FavoriteStore extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; //pk

  @ManyToOne(fetch = FetchType.EAGER)
  @JsonManagedReference
  @JoinColumn(name = "FAVORITE_ID")
  private Favorite favorite; //부모ID

  @ManyToOne(fetch = FetchType.EAGER)
  @JsonManagedReference
  @JoinColumn(name = "STORE_ID")
  private Store store; //식당ID

  public void setFavorite(Favorite favorite) {
    this.favorite = favorite;
  }

  public void setStore(Store store) {
    this.store = store;
  }
}
