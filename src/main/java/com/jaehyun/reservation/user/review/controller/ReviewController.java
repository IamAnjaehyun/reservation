package com.jaehyun.reservation.user.review.controller;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.review.domain.dto.ReviewReqDto;
import com.jaehyun.reservation.user.review.domain.dto.ReviewResDto;
import com.jaehyun.reservation.user.review.domain.entity.Review;
import com.jaehyun.reservation.user.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"REVIEW API"})
@RequestMapping("/v1/reservation/user/review")
public class ReviewController {

  private final ReviewService reviewService;

  //상점 대한 리뷰 작성
  @PostMapping("/{reservationId}")
  public APIResponse<ReviewResDto> createReview(
      @ApiParam(value = "예약 ID") @PathVariable Long reservationId,
      @ApiParam(value = "리뷰 작성 Dto") @RequestBody ReviewReqDto reviewReqDto,
      Principal principal) {
    return APIResponse.success("review",
        reviewService.createReview(reviewReqDto, reservationId, principal));
  }

  @PutMapping("/{reviewId}")
  public APIResponse<ReviewResDto> updateReview(
      @ApiParam(value = "리뷰 ID") @PathVariable Long reviewId,
      @ApiParam(value = "리뷰 작성 Dto") @RequestBody ReviewReqDto reviewReqDto,
      Principal principal) {
    return APIResponse.success("updatedReview",
        reviewService.updateReview(reviewId, reviewReqDto, principal));
  }

  @DeleteMapping("/{reviewId}")
  public APIResponse<Void> deleteReview(
      @ApiParam(value = "리뷰 ID") @PathVariable Long reviewId,
      Principal principal) {
    reviewService.deleteReview(reviewId, principal);
    return APIResponse.delete();
  }

  @GetMapping("/stores/{storeId}")
  public APIResponse<List<ReviewResDto>> getReview(
      @ApiParam(value = "상점 ID") @PathVariable Long storeId) {
    return APIResponse.success(reviewService.getReview(storeId));
  }
}
