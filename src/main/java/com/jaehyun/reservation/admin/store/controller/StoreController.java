package com.jaehyun.reservation.admin.store.controller;

import com.jaehyun.reservation.admin.store.domain.dto.StoreReqDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreResDto;
import com.jaehyun.reservation.admin.store.service.StoreService;
import com.jaehyun.reservation.global.common.APIResponse;
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
@RequestMapping("/v1/reservation/admin/store")
public class StoreController {

  private final StoreService storeService;
  private static final String API_NAME = "store";


  @PostMapping
  public APIResponse<StoreResDto> createStore(@RequestBody StoreReqDto storeReqDto,
      Principal principal) {
    return APIResponse.success(API_NAME, storeService.createStore(storeReqDto, principal));
  }

  @PutMapping("/{storeId}")
  public APIResponse<StoreResDto> updateStore(@PathVariable Long storeId,
      @RequestBody StoreReqDto storeReqDto, Principal principal) {
    return APIResponse.success(API_NAME, storeService.updateStore(storeId, storeReqDto, principal));
  }

  @DeleteMapping("/{storeId}")
  public APIResponse<Void> deleteStore(@PathVariable Long storeId, Principal principal) {
    storeService.deleteStore(storeId, principal);
    return APIResponse.delete();
  }

}
