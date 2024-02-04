package io.springbatch.springbatchlecture.lecture.section8_itemreader._11_itemreaderadapter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String location;

	@OneToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
