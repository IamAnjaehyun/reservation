package com.jaehyun.reservation.user.favorite.controller;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.favorite.domain.dto.FavoriteResDto;
import com.jaehyun.reservation.user.favorite.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"FAVORITE API"})
@RequestMapping("/v1/reservation/user/favorite")
public class FavoriteController {

  private final FavoriteService favoriteService;

  //favorite 목록에 추가
  @PostMapping
  public APIResponse<Long> addStoreToFavorite(
      @ApiParam(value = "상점 ID") @RequestParam Long storeId, Principal principal) {

    return APIResponse.success("favorite", favoriteService.addStoreToFavorite(storeId, principal));
  }


  //전체 목록 조회
  @GetMapping
  public APIResponse<FavoriteResDto> getFavoriteList(Principal principal) {

    return APIResponse.success("favoriteList", favoriteService.getFavoriteList(principal));
  }

  //requestParam 있으면 특정 상품만 삭제하고, 없으면 전체 목록 비우기
  @DeleteMapping
  public APIResponse<Void> deleteStoreFromFavorite(
      @ApiParam(value = "상점 ID") @RequestParam(required = false) Long storeId,
      Principal principal) {
    boolean deleteAll = storeId == null;
    favoriteService.deleteStoreFromFavorite(storeId, principal, deleteAll);
    return APIResponse.delete();
  }
}