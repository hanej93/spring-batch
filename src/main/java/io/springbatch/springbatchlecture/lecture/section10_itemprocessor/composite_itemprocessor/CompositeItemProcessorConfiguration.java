package io.springbatch.springbatchlecture.lecture.section10_itemprocessor.composite_itemprocessor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class CompositeItemProcessorConfiguration {

	public static final int CHUNK_SIZE = 10;

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
					return i > 10 ? null : "item" + i;
				}
			})
			.processor(customItemProcessor())
			.writer(items -> System.out.println("items = " + items))
			.build();
	}

	@Bean
	public ItemProcessor<? super String, String> customItemProcessor() {
		List itemProcessors = new ArrayList();
		itemProcessors.add(new CustomItemProcessor());
		itemProcessors.add(new CustomItemProcessor2());

		return new CompositeItemProcessorBuilder<>()
			.delegates(itemProcessors)
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository).tasklet((contribution, chunkContext) -> {
			System.out.println("step2 has executed");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}
}
