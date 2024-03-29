package io.springbatch.springbatchlecture.lecture.section8_itemreader._8_jpacursor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
// @Entity
public class Customer {

	@Id
	@GeneratedValue
	private Long id;

	private String firstname;

	private String lastname;

	private String birthdate;
}
