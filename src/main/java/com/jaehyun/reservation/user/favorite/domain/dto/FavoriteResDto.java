package com.jaehyun.reservation.user.favorite.domain.dto;

import com.jaehyun.reservation.user.favorite.favortiestore.domain.dto.FavoriteStoreResDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResDto {
  private Long id;
  private String userName;
  private List<FavoriteStoreResDto> favoriteStoreResDto;

}
