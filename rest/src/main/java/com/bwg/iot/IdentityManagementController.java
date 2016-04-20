package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@RepositoryRestController
@RequestMapping("/idm")
public class IdentityManagementController {

    private final Environment environment;

    @Autowired
    public IdentityManagementController(Environment env) {
        this.environment = env;
    };

    @RequestMapping(method = RequestMethod.GET, value = "/tokenEndpoint")
    public @ResponseBody ResponseEntity<?> getTokenEnpoint() {
        IdmPayload payload = new IdmPayload(environment);
        return new ResponseEntity<IdmPayload>(payload, HttpStatus.OK);
    }

    public class IdmPayload extends ResourceSupport {
        private String mobileClientId;
        private String mobileClientSecret;

        public IdmPayload(Environment env) {
            mobileClientId = env.getProperty(PropertyNames.MOBILE_CLIENT_ID);
            mobileClientSecret = env.getProperty(PropertyNames.MOBILE_CLIENT_SECRET);
            this.add(new Link(env.getProperty(PropertyNames.TOKEN_ENDPOINT)).withRel("tokenEndpoint"));
            this.add(new Link(env.getProperty(PropertyNames.REFRESH_ENDPOINT)).withRel("refreshEndpoint"));
            this.add(linkTo(AuthenticationController.class).slash("/whoami/").withRel("whoami"));
        }

        public String getMobileClientId() {
            return mobileClientId;
        }

        public void setMobileClientId(String mobileClientId) {
            this.mobileClientId = mobileClientId;
        }

        public String getMobileClientSecret() {
            return mobileClientSecret;
        }

        public void setMobileClientSecret(String mobileClientSecret) {
            this.mobileClientSecret = mobileClientSecret;
        }
    }
}
