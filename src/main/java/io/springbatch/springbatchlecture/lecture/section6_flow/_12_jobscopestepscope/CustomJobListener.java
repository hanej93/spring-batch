package io.springbatch.springbatchlecture.lecture.section6_flow._12_jobscopestepscope;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		jobExecution.getExecutionContext().putString("name", "user1");
	}
}
