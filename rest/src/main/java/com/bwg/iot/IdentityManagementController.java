package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;


@RepositoryRestController
@RequestMapping("/idm")
public class IdentityManagementController {

    @Autowired UserRepository userRepository;

    @Autowired
    MongoOperations mongoOps;

//    String tokenEndpoint;

//    @Autowired
    public IdentityManagementController() {};

    @RequestMapping(method = RequestMethod.GET, value = "/mobileTokenEndpoint")
    public @ResponseBody ResponseEntity<?> getTokenEnpoint() {
        Link link = new Link("https://iotdev05.bi.local/oxauth/seam/resource/restv1/oxauth/token");
        return new ResponseEntity<Link>(link, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/introspection")
    public @ResponseBody ResponseEntity<?> inspectToken(@RequestHeader(value="Authorization") String auth, @Param("token") String token) {
        IdmResponse resource = new IdmResponse();
        resource.setActive(true);
        resource.setExp(new BigInteger("1459488768826"));
        resource.setIat(new BigInteger("1459488768826"));

        System.out.println("auth: "+auth);
        System.out.println("token: "+token);

        return new ResponseEntity<IdmResponse>(resource, HttpStatus.OK);
    }


    class IdmResponse extends ResourceSupport {
        private boolean active = true;
        private BigInteger exp;
        private BigInteger iat;
        private String acr_values;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public BigInteger getExp() {
            return exp;
        }

        public void setExp(BigInteger exp) {
            this.exp = exp;
        }

        public BigInteger getIat() {
            return iat;
        }

        public void setIat(BigInteger iat) {
            this.iat = iat;
        }

        public String getAcr_values() {
            return acr_values;
        }

        public void setAcr_values(String acr_values) {
            this.acr_values = acr_values;
        }
    }
}
