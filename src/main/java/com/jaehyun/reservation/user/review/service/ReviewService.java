package com.jaehyun.reservation.user.review.service;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.exception.impl.reservation.NotExistReservationException;
import com.jaehyun.reservation.global.exception.impl.reservation.NotUsedStoreException;
import com.jaehyun.reservation.global.exception.impl.review.AlreadyExistReviewException;
import com.jaehyun.reservation.global.exception.impl.review.NotExistReviewException;
import com.jaehyun.reservation.global.exception.impl.role.UnauthorizedException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.review.domain.dto.ReviewReqDto;
import com.jaehyun.reservation.user.review.domain.dto.ReviewResDto;
import com.jaehyun.reservation.user.review.domain.entity.Review;
import com.jaehyun.reservation.user.review.domain.repository.ReviewRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

  private final StoreRepository storeRepository;

  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final ReservationRepository reservationRepository;

  public ReviewResDto createReview(ReviewReqDto reviewReqDto, Long reservationId,
      Principal principal) {
    User user = userRepository.findByLoginId(principal.getName()).get();
    Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
        NotExistReservationException::new); //예약 번호가 존재하지 않을 경우 작성 불가
    if(!reservation.getUser().getId().equals(user.getId())){
      throw new UnauthorizedException(); //예약자와 내가 일치하지 않을 경우 작성 불가
    } else if (reservation.getStatus() != ReservationStatus.USED) {
      throw new NotUsedStoreException(); //USED 아닌 상태에서 리뷰 작성 불가
    }
    Store store = storeRepository.findById(reservation.getStore().getId())
        .orElseThrow(NotExistStoreException::new);
    //내가 예약한 상점의 내역 확인, 상태가 USED로 바뀌어야 함.
    if (reviewRepository.existsByReservationIdAndUser(reservationId, user)) {
      throw new AlreadyExistReviewException();
    }
    Review review = new Review().builder()
        .store(store)
        .user(user)
        .reviewText(reviewReqDto.getReviewText())
        .starRating(reviewReqDto.getStars())
        .reservation(reservation)
        .build();

    reviewRepository.save(review);

    return ReviewResDto.fromReviewResDto(review);
  }

  public ReviewResDto updateReview(Long reviewId, ReviewReqDto reviewReqDto, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName()).get();
    Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistReviewException::new);
    if(!review.getUser().getId().equals(user.getId())){
      throw new UnauthorizedException(); //리뷰 작성자가 본인이 아닐 경우 오류 발생
    }

    review.setReviewText(reviewReqDto.getReviewText());
    review.setStarRating(reviewReqDto.getStars());
    reviewRepository.save(review);

    return ReviewResDto.fromReviewResDto(review);
  }

  public void deleteReview(Long reviewId, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName()).get();
    Review review = reviewRepository.findByIdAndUser(reviewId, user)
        .orElseThrow(NotExistReviewException::new);

    reviewRepository.delete(review);
  }
}
