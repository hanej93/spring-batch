package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._2_multi_thread_step;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class StopWatchJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LocalDateTime endTime = jobExecution.getEndTime();
		ZonedDateTime zdt1 = ZonedDateTime.of(endTime, ZoneId.systemDefault());
		long endTimeLong = zdt1.toInstant().toEpochMilli();

		LocalDateTime startTime = jobExecution.getStartTime();
		ZonedDateTime zdt2 = ZonedDateTime.of(startTime, ZoneId.systemDefault());
		long startTimeLong = zdt2.toInstant().toEpochMilli();

		long time = endTimeLong - startTimeLong;
		System.out.println("=======================================================");
		System.out.println("총 소요시간 : " + time);
		System.out.println("=======================================================");
	}
}
