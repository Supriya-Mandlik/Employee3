package com.Employees.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EmployeeAppConfig {
	
	@Value("${addressservice.base.url}")
	private String addressBaseURL;
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@LoadBalanced   // it will do load balancing in round robbin fission in eureka
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public WebClient webclient() {
		return WebClient
				.builder()
				.baseUrl(addressBaseURL)
				.build();
	}

}
