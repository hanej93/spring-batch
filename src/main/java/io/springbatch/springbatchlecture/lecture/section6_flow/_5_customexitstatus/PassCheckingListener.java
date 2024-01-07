package io.springbatch.springbatchlecture.lecture.section6_flow._5_customexitstatus;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class PassCheckingListener implements StepExecutionListener {

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		String exitCode = stepExecution.getExitStatus().getExitCode();
		if (!exitCode.equals(ExitStatus.FAILED.getExitCode())) {
			return new ExitStatus("PASS");
		}

		return null;
	}
}
