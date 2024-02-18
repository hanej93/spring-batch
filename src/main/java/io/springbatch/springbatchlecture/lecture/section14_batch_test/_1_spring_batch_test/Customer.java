package io.springbatch.springbatchlecture.lecture.section14_batch_test._1_spring_batch_test;

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
