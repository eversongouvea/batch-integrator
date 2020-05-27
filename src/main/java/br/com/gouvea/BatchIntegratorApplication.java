package br.com.gouvea;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@EnableBatchProcessing
@SpringBootApplication
public class BatchIntegratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchIntegratorApplication.class, args);
	}

}
