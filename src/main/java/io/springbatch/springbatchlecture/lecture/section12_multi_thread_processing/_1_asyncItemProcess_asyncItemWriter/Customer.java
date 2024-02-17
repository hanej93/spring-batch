package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._1_asyncItemProcess_asyncItemWriter;

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
