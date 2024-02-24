package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		JobExecutionListener.super.beforeJob(jobExecution);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		JobExecutionListener.super.afterJob(jobExecution);
	}
}
