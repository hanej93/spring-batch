package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.partition;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.job.api.QueryGenerator;

public class ProductPartitioner implements Partitioner {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		ProductVO[] productList = QueryGenerator.getProductList(dataSource);
		Map<String, ExecutionContext> result = new HashMap<>();

		for (int i = 0; i < productList.length; i++) {
			ExecutionContext value = new ExecutionContext();
			result.put("partition" + i, value);
			value.put("product", productList[i]);
		}

		return result;
	}
}
