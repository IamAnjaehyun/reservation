package com.jaehyun.reservation.user.review.controller;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationReqDto;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.review.domain.dto.ReviewReqDto;
import com.jaehyun.reservation.user.review.domain.dto.ReviewResDto;
import com.jaehyun.reservation.user.review.service.ReviewService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservation/user/review")
public class ReviewController {
  //리뷰 작성 + 조회는 상점 detail 조회시 같이 보이도록 할 예정
  //별점 + 리뷰 내용

  private final ReviewService reviewService;
  private static final String API_NAME = "reivew";

  //상점 대한 리뷰 작성
  @PostMapping("/{storeId}/{reservationId}")
  public APIResponse<ReviewResDto> createReview(@PathVariable Long storeId,
      @PathVariable Long reservationId,
      @RequestBody ReviewReqDto reviewReqDto,
      Principal principal) {
    return APIResponse.success(API_NAME, reviewService.createReview(storeId, reviewReqDto, reservationId, principal));
  }
}
