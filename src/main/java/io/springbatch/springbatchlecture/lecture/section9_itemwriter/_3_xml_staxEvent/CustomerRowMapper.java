package io.springbatch.springbatchlecture.lecture.section9_itemwriter._3_xml_staxEvent;

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
