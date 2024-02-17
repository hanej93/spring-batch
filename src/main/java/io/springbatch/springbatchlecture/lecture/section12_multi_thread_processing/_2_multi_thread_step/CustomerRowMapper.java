package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._2_multi_thread_step;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerRowMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Customer(
			rs.getLong("id"),
			rs.getString("firstname"),
			rs.getString("lastname"),
			rs.getString("birthdate")
		);
	}
}
