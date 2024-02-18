package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._3_parallelStep;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomTasklet implements Tasklet {

	private long sum;
	private Object lock = new Object();

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		synchronized (lock) {
			for (int i = 0; i < 100_000_000; i++) {
				sum++;
			}
			System.out.printf("%s has been executed on thread %s\n",
				chunkContext.getStepContext().getStepName(),
				Thread.currentThread().getName());
			System.out.printf("sum: %d\n", sum);
		}

		return RepeatStatus.FINISHED;
	}
}
