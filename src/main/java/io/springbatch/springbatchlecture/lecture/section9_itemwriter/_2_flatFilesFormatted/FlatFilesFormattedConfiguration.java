package io.springbatch.springbatchlecture.lecture.section9_itemwriter._2_flatFilesFormatted;


import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FlatFilesFormattedConfiguration {

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
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(customerItemReader())
			.writer(customerItemWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super Customer> customerItemWriter() {
		return new FlatFileItemWriterBuilder<Customer>()
			.name("flatFileWriter")
			.resource(new FileSystemResource("C:\\Users\\hanej\\Desktop\\Study\\정수원\\spring-batch\\springbatch\\src\\main\\resources\\files\\customer.txt"))
			.append(true)
			.formatted().format("%-2d%-15s%-2d")
			.names("id", "name", "age")
			.build();
	}

	@Bean
	public ItemReader<? extends Customer> customerItemReader() {
		List<Customer> customers = Arrays.asList(
			new Customer(1, "hong gil dong1", 41),
			new Customer(2, "hong gil dong2", 42),
			new Customer(3, "hong gil dong3", 43)
		);

		return new ListItemReader<>(customers);
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
