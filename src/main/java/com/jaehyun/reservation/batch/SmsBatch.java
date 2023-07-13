package com.jaehyun.reservation.batch;

import com.jaehyun.reservation.admin.smsservice.dto.SmsDto;
import com.jaehyun.reservation.admin.smsservice.service.SmsService;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration // Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용해야 한다.
public class SmsBatch {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final ReservationRepository reservationRepository;
  private final SmsService smsService;

  @Bean
  public Job myJob() {
    return jobBuilderFactory.get("myJob")
        .start(myStep())
        .build();
  }

  @Bean
  public Step myStep() {
    return stepBuilderFactory.get("myStep")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>> This is myStep");
          List<Reservation> reservationList = reservationRepository.findAllByStatus(
              ReservationStatus.OKAY);
          for (Reservation reservation : reservationList) {
            LocalDateTime reservationDateTimeMinus30Minutes = reservation.getReservationDateTime()
                .minusMinutes(30);
            LocalDateTime currentDateTime = LocalDateTime.now();

            if (reservationDateTimeMinus30Minutes.isBefore(currentDateTime)) {
              User user = reservation.getUser(); // User 객체를 가져오는 방법이 구현되어 있다고 가정

              SmsDto smsDto = new SmsDto().builder()
                  .to(user.getPhoneNum())
                  .content(reservation.getStore().getName() + "\n" + user.getName() + "님께서 "
                      + reservation.getReservationDateTime()
                      + "에 예약하신 예약시간까지 30분 남았습니다. 현 시간 이후로 예약 취소는 불가능합니다. " )
                  .build();
              smsService.sendSms(smsDto);

              //문자를 보냈다면 더이상 예약 취소가 불가능 하기 때문에 상태를 USED로 변경
              reservation.setStatus(ReservationStatus.USED);
              reservationRepository.save(reservation);

              //SMS가 보내졌으면 log 출력
              log.info("Sent SMS for reservation ID: {}", reservation.getId());
            }
          }
          return RepeatStatus.FINISHED;
        })
        .build();
  }

}