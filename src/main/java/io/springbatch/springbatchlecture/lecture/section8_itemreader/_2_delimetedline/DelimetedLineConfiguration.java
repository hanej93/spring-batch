package io.springbatch.springbatchlecture.lecture.section8_itemreader._2_delimetedline;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class DelimetedLineConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<Customer, Customer>chunk(5, transactionManager)
			.reader(itemReader())
			.writer((ItemWriter<Customer>)chunk -> {
				chunk.forEach(item -> System.out.println("item = " + item));
				System.out.println("");
			})
			.build();
	}

	@Bean
	public ItemReader itemReader() {
		return new FlatFileItemReaderBuilder<Customer>()
			.name("flatFile")
			.resource(new ClassPathResource("/customer.csv"))
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>())
			.targetType(Customer.class)
			.linesToSkip(1)
			.delimited().delimiter(",")
			.names("name", "age", "year")
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step2 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
