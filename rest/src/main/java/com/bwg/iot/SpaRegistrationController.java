package com.bwg.iot;

/**
 * Created by triton on 5/5/16.
 */
import com.bwg.iot.model.SpaRegistrationRequest;
import com.bwg.iot.model.SpaRegistrationResponse;
import com.bwg.iot.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RepositoryRestController
@RequestMapping("/")
public class SpaRegistrationController {
    private final static Logger log = LoggerFactory.getLogger(SpaRegistrationController.class);

    private final UserRepository userRepository;

    private String scimUser;
    private String scimPassword;
    private String scimUrl;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    Environment environment;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public SpaRegistrationController(UserRepository repo) {
        userRepository = repo;
    }

    public void afterPropertiesSet() {
        scimUser = environment.getProperty(PropertyNames.SCIM_USER);
        scimPassword = environment.getProperty(PropertyNames.SCIM_PWD);
        scimUrl = environment.getProperty(PropertyNames.SCIM_URL);
    }

    @RequestMapping(value = "/registerSpaToUser", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> registerSpaToUser(@RequestBody SpaRegistrationRequest request){

        // validate user inputs (do we have reg_key, spaId?)
        String spaId = request.getSpaId();
        String regKey = request.getRegKey();

        Map<String, Object> token = authenticateUser(request.getUser());
        SpaRegistrationResponse response = new SpaRegistrationResponse(spaId, token);
        return new ResponseEntity<SpaRegistrationResponse>(response, HttpStatus.CREATED);
    }

    private Map<String, Object> authenticateUser(User user){
        // stub:
        Map<String, Object> token = new HashMap<String, Object>();
        token.put("access_token", "57ca1b11-b236-47a6-b719-c88f8595dc6a");
        token.put("token_type", "bearer");
        token.put("expires_in", Integer.valueOf(3599));
        token.put("refresh_token", "c567551f-54fe-4fab-bfaf-ccabe4aa8087");
        token.put("scope", "user_name openid");
        token.put("id_token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjdlMjFjYWNhLTg3YzMtNGMyYS1hZTFjLWY2Y2U2ZWE4ZmU2YiJ9.eyJpc3MiOiJodHRwczovL2lvdGRldjA1LmJpLmxvY2FsIiwiYXVkIjoiQCEwRUVDLkY2NjEuRkQ4Qy5GMzk5ITAwMDEhM0Y2Ri5CNDAzITAwMDghQzE5Ni5BNEM4IiwiZXhwIjoxNDYyNDc3MjcxLCJpYXQiOjE0NjI0NzM2NzEsIm94VmFsaWRhdGlvblVSSSI6Imh0dHBzOi8vaW90ZGV2MDUuYmkubG9jYWwvb3hhdXRoL29waWZyYW1lIiwib3hPcGVuSURDb25uZWN0VmVyc2lvbiI6Im9wZW5pZGNvbm5lY3QtMS4wIiwidXNlcl9uYW1lIjoiaW90LXNlcnZpY2VzIiwiaW51bSI6IkAhMEVFQy5GNjYxLkZEOEMuRjM5OSEwMDAxITNGNkYuQjQwMyEwMDAwITJGQjkuQTc2MSIsInN1YiI6ImlvdC1zZXJ2aWNlcyJ9.ekD1CxVnGDNhwdqMI6zLMQ9JM7U8WmrG3R2DxmEvdYoJ8mLBQneQ-VTLpSuaRjqCqyYBLrUJYm6D4hIb7UvyTGTRKlZa-aFuA7ox4UntMu9SFe__j8o27oLuY_ixYuBAPgrKHiayHyhFDkg_rOMwHfUIAfe47ifSQM2EV9OFU7LdrWAIR418jVhDMYnlD6JZHW-kRHRuSFIMWvGtfs2a_10-00Pg3xt7z8hreocsINHg-HgoU-fqG71MqqraTCgmu95ru-zOzhxbEAsQ1vfzlnOz7HMUq8nShesMNRHJLmJqyMwKMh69PZopRGC0PqPgjbMbXwzWKo9y-3Bj_XmskQ");

        return token;
    }
}
