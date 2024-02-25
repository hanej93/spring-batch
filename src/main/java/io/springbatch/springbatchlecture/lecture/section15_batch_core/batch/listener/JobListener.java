package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.listener;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LocalDateTime startTime = jobExecution.getStartTime();
		LocalDateTime endTime = jobExecution.getEndTime();
		long millis = Duration.between(startTime, endTime).toMillis();
		System.out.println("총 소요시간 = " + millis);
	}
}
