package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.job.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.rowmapper.ProductRowMapper;

public class QueryGenerator {

	public static ProductVO[] getProductList(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<ProductVO> productList = jdbcTemplate.query("select type from product group by type", new ProductRowMapper() {
			@Override
			public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return ProductVO.builder()
					.type(rs.getString("type"))
					.build();
			}
		});

		return productList.toArray(new ProductVO[] {});
	}

	public static Map<String, Object> getParameterForQuery(String parameter, String value) {
		Map<String, Object> parameters = Map.of(parameter, value);
		return parameters;
	}
}
