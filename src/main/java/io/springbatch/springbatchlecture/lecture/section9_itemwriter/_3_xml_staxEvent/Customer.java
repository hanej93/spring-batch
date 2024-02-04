package io.springbatch.springbatchlecture.lecture.section9_itemwriter._3_xml_staxEvent;

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
