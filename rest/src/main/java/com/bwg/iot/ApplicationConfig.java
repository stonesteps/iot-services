package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.FaultLogDescription;
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
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ResourceProcessor<Resource<FaultLog>> faultLogProcessor() {
        return new ResourceProcessor<Resource<FaultLog>>() {
            @Override
            public Resource<FaultLog> process(final Resource<FaultLog> resource) {
                final FaultLog faultLog = resource.getContent();
                final FaultLogDescription description = faultLogDescriptionRepository.findFirstByCodeAndControllerType(
                        faultLog.getCode(), faultLog.getControllerType());
                faultLog.setFaultLogDescription(description);
                return resource;
            }
        };
    }
}