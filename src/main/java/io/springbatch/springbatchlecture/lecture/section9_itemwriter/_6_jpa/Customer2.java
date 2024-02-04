package io.springbatch.springbatchlecture.lecture.section9_itemwriter._6_jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
// @Entity
public class Customer2 {

	@Id
	private Long id;

	private String firstname;

	private String lastname;

	private String birthdate;
}
