package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiResponseVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.service.AbstractApiService;

public class ApiItemWriter2 implements ItemWriter<ApiRequestVO> {

	private final AbstractApiService apiService;

	public ApiItemWriter2(AbstractApiService apiService) {
		this.apiService = apiService;
	}

	@Override
	public void write(Chunk<? extends ApiRequestVO> chunk) throws Exception {
		List<? extends ApiRequestVO> items = chunk.getItems();
		ApiResponseVO responseVO = apiService.service(items);
		System.out.println("responseVO = " + responseVO);
	}
}
