package com.bwg.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.convert.ThreeTenBackPortConverters;

@EnableAutoConfiguration
@SpringBootApplication
public class IotServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotServicesApplication.class, args);
	}
}
