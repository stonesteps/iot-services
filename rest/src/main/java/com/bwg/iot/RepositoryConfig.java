package com.bwg.iot;

import com.bwg.iot.listeners.UserEventHandler;
import com.bwg.iot.model.*;
import com.bwg.iot.validator.BeforeCreateUserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class RepositoryConfig extends RepositoryRestMvcConfiguration {
    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Spa.class, Alert.class, User.class, SpaCommand.class,
                Dealer.class, Oem.class, TacUserAgreement.class, TermsAndConditions.class,
                Material.class, SpaTemplate.class, Recipe.class, RecipeDTO.class, Attachment.class);
    }

    @Override
    protected void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", new BeforeCreateUserValidator());
        validatingListener.addValidator("beforeSave", new BeforeCreateUserValidator());
    }

    @Bean
    UserEventHandler userEventHandler() {
        return new UserEventHandler();
    }

    @Bean
    public Filter loggingFilter(){
        AbstractRequestLoggingFilter f = new AbstractRequestLoggingFilter() {
        private final Logger logger = LoggerFactory.getLogger(RepositoryConfig.class);

            @Override
            protected void beforeRequest(HttpServletRequest request, String message)
            {
                logger.info(message);
                String remoteUser = request.getRemoteUser();
                logger.info("REMOTE_USER: " + remoteUser);
                // print all headers: for debug
                if (logger.isDebugEnabled()) {
                    Enumeration headerNames = request.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        String headerName = (String) headerNames.nextElement();
                        logger.debug("header: " + headerName + ":" + request.getHeader(headerName));
                    }
                }
            }

            @Override
            protected void afterRequest(HttpServletRequest request, String message) {
                logger.info(message);
            }
        };
        f.setIncludeClientInfo(true);
        f.setIncludePayload(true);
        f.setIncludeQueryString(true);

        f.setBeforeMessagePrefix("BEFORE REQUEST  [");
        f.setAfterMessagePrefix("AFTER REQUEST    [");
        f.setAfterMessageSuffix("]\n");
        return f;
    }
}

