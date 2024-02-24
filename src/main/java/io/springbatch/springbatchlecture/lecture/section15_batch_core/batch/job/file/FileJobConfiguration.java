package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.job.file;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.processor.FileItemProcessor;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.Product;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FileJobConfiguration {

	public static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job fileJob() {
		return new JobBuilder("fileJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(fileStep())
			.build();
	}

	@Bean
	public Step fileStep() {
		return new StepBuilder("fileStep", jobRepository)
			.<ProductVO, Product>chunk(CHUNK_SIZE, transactionManager)
			.reader(fileItemReader(null))
			.processor(fileItemProcessor())
			.writer(fileItemWriter())
			.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<ProductVO> fileItemReader(@Value("#{jobParameters['requestDate']}") String requestDate) {
		return new FlatFileItemReaderBuilder<ProductVO>()
			.name("flatFile")
			.resource(new ClassPathResource("product_" + requestDate + ".csv"))
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>())
			.targetType(ProductVO.class)
			.linesToSkip(1)
			.delimited().delimiter(",")
			.names("id", "name", "price", "type")
			.build();
	}

	@Bean
	public ItemProcessor<ProductVO, Product> fileItemProcessor() {
		return new FileItemProcessor();
	}

	@Bean
	public JpaItemWriter<Product> fileItemWriter() {
		return new JpaItemWriterBuilder<Product>()
			.entityManagerFactory(entityManagerFactory)
			.usePersist(true)
			.build();
	}
}
