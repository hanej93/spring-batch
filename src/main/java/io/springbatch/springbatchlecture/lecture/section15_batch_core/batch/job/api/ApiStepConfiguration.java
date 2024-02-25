package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.job.api;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemProcessorBuilder;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.processor.ApiItemProcessor1;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.processor.ApiItemProcessor2;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.processor.ApiItemProcessor3;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.writer.ApiItemWriter1;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.writer.ApiItemWriter2;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.writer.ApiItemWriter3;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.classifier.ProcessorClassifier;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.classifier.WriterClassifier;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.partition.ProductPartitioner;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.service.ApiService1;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.service.ApiService2;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.service.ApiService3;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

	private final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;
	private final ApiService1 apiService1;
	private final ApiService2 apiService2;
	private final ApiService3 apiService3;

	@Bean
	public Step apiMasterStep() {
		return new StepBuilder("apiMasterStep", jobRepository)
			.partitioner(apiSlaveStep().getName(), partitioner())
			.step(apiSlaveStep())
			.gridSize(3)
			.taskExecutor(taskExecutor())
			.build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(3);
		taskExecutor.setMaxPoolSize(6);
		taskExecutor.setThreadNamePrefix("api-thread");
		return taskExecutor;
	}

	@Bean
	public Step apiSlaveStep() {
		return new StepBuilder("apiSlaveStep", jobRepository)
			.<ProductVO, ProductVO>chunk(CHUNK_SIZE, transactionManager)
			.reader(itemReader(null))
			.processor(itemProcessor())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ProductPartitioner partitioner() {
		ProductPartitioner productPartitioner = new ProductPartitioner();
		productPartitioner.setDataSource(dataSource);
		return productPartitioner;
	}

	@Bean
	@StepScope
	public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO) {
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, name, price, type");
		queryProvider.setFromClause("from product");
		queryProvider.setWhereClause("where type = :type");

		Map<String, Order> sortKeys = Map.of("id", Order.DESCENDING);
		queryProvider.setSortKeys(sortKeys);

		return new JdbcPagingItemReaderBuilder<ProductVO>()
			.name("jdbcPagingItemReader")
			.dataSource(dataSource)
			.fetchSize(CHUNK_SIZE)
			.rowMapper(new BeanPropertyRowMapper<>(ProductVO.class))
			.queryProvider(queryProvider)
			.parameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()))
			.build();
	}

	@Bean
	public ItemProcessor itemProcessor() {
		ProcessorClassifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> classifier = new ProcessorClassifier<>();
		Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();
		processorMap.put("1", new ApiItemProcessor1());
		processorMap.put("2", new ApiItemProcessor2());
		processorMap.put("3", new ApiItemProcessor3());
		classifier.setProcessorMap(processorMap);

		return new ClassifierCompositeItemProcessorBuilder<ProductVO, ApiRequestVO>()
			.classifier(classifier)
			.build();
	}

	@Bean
	public ItemWriter itemWriter() {
		WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier = new WriterClassifier<>();
		Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();
		writerMap.put("1", new ApiItemWriter1(apiService1));
		writerMap.put("2", new ApiItemWriter2(apiService2));
		writerMap.put("3", new ApiItemWriter3(apiService3));
		classifier.setWriterMap(writerMap);

		return new ClassifierCompositeItemWriterBuilder<ApiRequestVO>()
			.classifier(classifier)
			.build();
	}
}
