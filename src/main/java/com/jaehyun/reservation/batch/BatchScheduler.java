//package com.jaehyun.reservation.batch;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParameter;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@Slf4j
//@RequiredArgsConstructor
//public class BatchScheduler {
//
//  private final JobLauncher jobLauncher;
//  private final Job job;
//
//  @Scheduled(cron = "0 * * * * *") // 매 분마다 실행 (1분마다 실행하려면 "0 * * * * *"로 수정)
//  public void jobScheduled() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
//      JobRestartException, JobInstanceAlreadyCompleteException {
//
//    Map<String, JobParameter> jobParametersMap = new HashMap<>();
//
//    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//    Date time = new Date();
//
//    String time1 = format1.format(time);
//
//    jobParametersMap.put("date", new JobParameter(time1));
//
//    JobParameters parameters = new JobParameters(jobParametersMap);
//
//    JobExecution jobExecution = jobLauncher.run(job, parameters);
//
//    while (jobExecution.isRunning()) {
//      log.info("...");
//    }
//
//    log.info("Job Execution: " + jobExecution.getStatus());
//    log.info("Job getJobConfigurationName: " + jobExecution.getJobConfigurationName());
//    log.info("Job getJobId: " + jobExecution.getJobId());
//    log.info("Job getExitStatus: " + jobExecution.getExitStatus());
//    log.info("Job getJobInstance: " + jobExecution.getJobInstance());
//    log.info("Job getStepExecutions: " + jobExecution.getStepExecutions());
//    log.info("Job getLastUpdated: " + jobExecution.getLastUpdated());
//    log.info("Job getFailureExceptions: " + jobExecution.getFailureExceptions());
//  }
//}
