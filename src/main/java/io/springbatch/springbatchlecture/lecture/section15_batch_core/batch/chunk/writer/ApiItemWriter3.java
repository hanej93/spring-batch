package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiResponseVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.service.AbstractApiService;

public class ApiItemWriter3 extends FlatFileItemWriter<ApiRequestVO> {

	private final AbstractApiService apiService;

	public ApiItemWriter3(AbstractApiService apiService) {
		this.apiService = apiService;
	}

	@Override
	public void write(Chunk<? extends ApiRequestVO> chunk) throws Exception {
		List<? extends ApiRequestVO> items = chunk.getItems();
		ApiResponseVO responseVO = apiService.service(items);
		System.out.println("responseVO = " + responseVO);
		items.forEach(item -> item.setApiResponseVO(responseVO));

		super.setResource(new FileSystemResource("C:\\Users\\hanej\\Desktop\\Study\\정수원\\spring-batch\\springbatch\\src\\main\\resources\\product3.txt"));
		super.open(new ExecutionContext());
		super.setLineAggregator(new DelimitedLineAggregator<>());
		super.setAppendAllowed(true);
		super.write(chunk);
	}
}
