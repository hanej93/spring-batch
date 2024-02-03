package io.springbatch.springbatchlecture.lecture.section8_itemreader._9_jdbcpaging;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Customer {

	private Long id;

	private String firstname;

	private String lastname;

	private String birthdate;
}
