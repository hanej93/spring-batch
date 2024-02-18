package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._5_not_synchronized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	private Long id;

	private String firstname;

	private String lastname;

	private String birthdate;
}
