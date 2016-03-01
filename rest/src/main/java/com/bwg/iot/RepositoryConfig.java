package com.bwg.iot;

import com.bwg.iot.model.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
public class RepositoryConfig extends RepositoryRestMvcConfiguration {
    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Spa.class, Owner.class, Alert.class, User.class, SpaCommand.class,
                Dealer.class, Oem.class, TacUserAgreement.class, TermsAndConditions.class);
    }
}