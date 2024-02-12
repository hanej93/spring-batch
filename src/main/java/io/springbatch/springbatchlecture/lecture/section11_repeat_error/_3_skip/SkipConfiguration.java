package io.springbatch.springbatchlecture.lecture.section11_repeat_error._3_skip;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SkipConfiguration {

	public static final int CHUNK_SIZE = 5;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<String, String>chunk(CHUNK_SIZE, transactionManager)
			.reader(new ItemReader<>() {
				int i = 0;
				@Override
				public String read() throws
					Exception,
					UnexpectedInputException,
					ParseException,
					NonTransientResourceException {
					i++;
					System.out.println("FaultTolerantConfiguration.read i=" + i);
					if (i == 3) {
						throw new SkippableException("skip");
					}
					return i > 20 ? null : String.valueOf(i);
				}
			})
			.processor(itemProcess())
			.writer(itemWriter())
			.faultTolerant()
			.skip(SkippableException.class)
			// .noSkip(NonSkippableException.class)
			.skipLimit(4)
			// .skipPolicy(limitCheckingItemSkipPolicy())
			// .skipPolicy(new AlwaysSkipItemSkipPolicy())
			.build();
	}

	@Bean
	public SkipPolicy limitCheckingItemSkipPolicy() {
		Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
		exceptionClass.put(SkippableException.class, true);

		return new LimitCheckingItemSkipPolicy(3, exceptionClass);
	}

	@Bean
	public ItemWriter<? super String> itemWriter() {
		return new SkipItemWriter();
	}

	@Bean
	public ItemProcessor<? super String, String> itemProcess() {
		return new SkipItemProcessor();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository).tasklet((contribution, chunkContext) -> {
			System.out.println("step2 has executed");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}
}
