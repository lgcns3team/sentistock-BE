package com.example.SentiStock_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SentiStockBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentiStockBackendApplication.class, args);
	}

}
