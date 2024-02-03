package io.springbatch.springbatchlecture.lecture.section8_itemreader._4_exceptionhandling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ExceptionHandlingConfiguration {

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
			.resource(new FileSystemResource("C:\\Users\\hanej\\Desktop\\Study\\정수원\\spring-batch\\springbatch\\src\\main\\resources\\customer.txt"))
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>())
			.targetType(Customer.class)
			.linesToSkip(1)
			.fixedLength()
			.strict(false)
			.addColumns(new Range(1, 5))
			.addColumns(new Range(6, 7))
			.addColumns(new Range(8, 11))
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
