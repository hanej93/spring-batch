package io.springbatch.springbatchlecture;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Spring Batch 5에서는 기본 설정이 백오프이고, DefaultBatchConfiguration로 백오프를 설정해줘야 한다.
// @EnableBatchProcessing(isolationLevelForCreate = "ISOLATION_READ_COMMITTED", tablePrefix = "SYSTEM_")
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
