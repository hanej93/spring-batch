package io.springbatch.springbatchlecture.lecture.section6_flow._12_jobscopestepscope;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CustomStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		stepExecution.getExecutionContext().putString("name2", "user2");
	}
}
