package com.bwg.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class IotServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotServicesApplication.class, args);
	}
}
