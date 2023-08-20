package com.jaehyun.reservation.admin.store.controller;

import com.jaehyun.reservation.admin.store.domain.dto.StoreReqDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreResDto;
import com.jaehyun.reservation.admin.store.service.StoreService;
import com.jaehyun.reservation.global.common.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"STORE API"})
@RequestMapping("/v1/reservation/admin/store")
public class StoreController {

  private final StoreService storeService;


  @PostMapping
  public APIResponse<StoreResDto> createStore(
      @ApiParam(value = "상점 상점 등록(수정) Dto") @RequestBody StoreReqDto storeReqDto,
      Principal principal) {
    return APIResponse.success("store", storeService.createStore(storeReqDto, principal));
  }

  @PutMapping("/{storeId}")
  public APIResponse<StoreResDto> updateStore(@ApiParam(value = "상점 ID") @PathVariable Long storeId,
      @ApiParam(value = "상점 등록(수정) Dto") @RequestBody StoreReqDto storeReqDto,
      Principal principal) {
    return APIResponse.success("updatedStore",
        storeService.updateStore(storeId, storeReqDto, principal));
  }

  @DeleteMapping("/{storeId}")
  public APIResponse<Void> deleteStore(@ApiParam(value = "상점 ID") @PathVariable Long storeId,
      Principal principal) {
    storeService.deleteStore(storeId, principal);
    return APIResponse.delete();
  }

}
