package com.jaehyun.reservation.admin.store.controller;

import com.jaehyun.reservation.admin.store.domain.dto.StoreReqDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreResDto;
import com.jaehyun.reservation.admin.store.service.StoreService;
import com.jaehyun.reservation.global.common.APIResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @PostMapping
  public APIResponse<StoreResDto> createStore(@RequestBody StoreReqDto storeReqDto, Principal principal) {
    return storeService.createStore(storeReqDto, principal);
  }

  @PutMapping("/{storeName}")
  public APIResponse<StoreResDto> updateStore(@PathVariable String storeName,
      @RequestBody StoreReqDto storeReqDto, Principal principal) {
    return storeService.updateStore(storeName, storeReqDto, principal);
  }

}
