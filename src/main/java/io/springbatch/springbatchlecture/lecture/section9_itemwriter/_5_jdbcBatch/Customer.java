package io.springbatch.springbatchlecture.lecture.section9_itemwriter._5_jdbcBatch;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

	private Long id;

	private String firstname;

	private String lastname;

	private String birthdate;
}
