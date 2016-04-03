package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RepositoryRestController
@RequestMapping("/idm")
public class IdentityManagementController {

    private final String tokenEndpoint;

    @Autowired
    public IdentityManagementController(Environment env) {
        this.tokenEndpoint = env.getProperty(PropertyNames.TOKEN_ENDPOINT);
    };

    @RequestMapping(method = RequestMethod.GET, value = "/tokenEndpoint")
    public @ResponseBody ResponseEntity<?> getTokenEnpoint() {
        Link link = new Link(tokenEndpoint);
        return new ResponseEntity<Link>(link, HttpStatus.OK);
    }

}
