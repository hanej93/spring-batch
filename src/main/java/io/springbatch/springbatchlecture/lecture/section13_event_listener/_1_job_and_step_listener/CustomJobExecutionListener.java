package io.springbatch.springbatchlecture.lecture.section13_event_listener._1_job_and_step_listener;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobExecutionListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job is started");
		System.out.println("jobName : " + jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LocalDateTime startTime = jobExecution.getStartTime();
		LocalDateTime endTime = jobExecution.getEndTime();
		Duration diff = Duration.between(startTime, endTime);
		long diffMillis = diff.toMillis();

		System.out.println("총 소요시간: " + diffMillis);
	}
}
