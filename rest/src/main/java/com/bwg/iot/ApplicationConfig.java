package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan
class ApplicationConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Autowired
    private FaultLogDescriptionRepository faultLogDescriptionRepository;

    @Bean
    public ResourceProcessor<Resource<FaultLog>> faultLogProcessor() {
        return new FaultLogResourceProcessor(faultLogDescriptionRepository);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}