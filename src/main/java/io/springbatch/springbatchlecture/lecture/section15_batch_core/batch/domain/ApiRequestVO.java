package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestVO {

	private long id;
	private ProductVO productVO;
}
