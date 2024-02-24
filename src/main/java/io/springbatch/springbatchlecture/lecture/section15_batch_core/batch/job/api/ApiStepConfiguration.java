package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.job.api;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._2_multi_thread_step.Customer;
import io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._2_multi_thread_step.CustomerRowMapper;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.partition.ProductPartitioner;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

	private final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;

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
}
