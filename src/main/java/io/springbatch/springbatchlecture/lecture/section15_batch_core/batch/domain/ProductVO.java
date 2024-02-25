package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO implements Serializable {

	private Long id;
	private String name;
	private int price;
	private String type;
}
