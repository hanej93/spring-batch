package io.springbatch.springbatchlecture.lecture.section15_batch_core.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiInfo;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiResponseVO;

@Service
public class ApiService3 extends AbstractApiService{

	@Override
	protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8083/api/product/3", apiInfo, String.class);
		int statusCodeValue = responseEntity.getStatusCode().value();
		
		return ApiResponseVO.builder()
			.status(statusCodeValue)
			.msg(responseEntity.getBody())
			.build();
	}
}
