package io.springbatch.springbatchlecture.lecture.section5;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class SimpleBuilderConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob1() {
		return new JobBuilder("batchJob1", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.next(step2())
			.next(step3())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step1 was executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.<String, String>chunk(3, transactionManager)
			.reader(new ItemReader<String>() {
				@Override
				public String read() throws
					Exception,
					UnexpectedInputException,
					ParseException,
					NonTransientResourceException {
					return null;
				}
			})
			.processor(new ItemProcessor<String, String>() {
				@Override
				public String process(String item) throws Exception {
					return null;
				}
			})
			.writer(new ItemStreamWriter<String>() {
				@Override
				public void write(Chunk<? extends String> chunk) throws Exception {

				}
			})
			.build();
	}

	@Bean
	public Step step3() {
		return new StepBuilder("step3", jobRepository)
			// .partitioner(step1())
			.partitioner("partition", new Partitioner() {
				@Override
				public Map<String, ExecutionContext> partition(int gridSize) {
					return new HashMap<>();
				}
			})
			.step(step1())
			.gridSize(2)
			.build();
	}

	// @Bean
	// public Step step4() {
	// 	return new StepBuilder("step4", jobRepository)
	// 		.job(job1())
	// 		.build();
	// }

	@Bean
	public Step step5() {
		return new StepBuilder("step5", jobRepository)
			.flow(flow())
			.build();
	}

	// 하나의 잡만 실행시킬 수 있다. 프로젝트 실행 시 어떤 잡을 실행시킬지 알 수 없기 때문에 주석처리
	// @Bean
	// public Job job1() {
	// 	return new JobBuilder("job1", jobRepository)
	// 		.start(step1())
	// 		.next(step2())
	// 		.next(step3())
	// 		.build();
	// }

	@Bean
	public Flow flow() {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
		flowBuilder.start(step2()).end();
		return flowBuilder.build();
	}
}
