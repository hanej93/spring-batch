package io.springbatch.springbatchlecture.lecture.section9_itemwriter._1_flatFilesDelimited;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

	private long id;
	private String name;
	private int age;
}
