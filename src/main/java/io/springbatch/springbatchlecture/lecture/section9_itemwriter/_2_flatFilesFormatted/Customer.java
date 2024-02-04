package io.springbatch.springbatchlecture.lecture.section9_itemwriter._2_flatFilesFormatted;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

	private long id;
	private String name;
	private int age;
}
