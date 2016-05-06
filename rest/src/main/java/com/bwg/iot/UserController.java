package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.GluuUser;
import com.bwg.iot.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;


@RepositoryRestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

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
    public UserController(UserRepository repo) {
        userRepository = repo;
    }

    public void afterPropertiesSet() {
        scimUser = environment.getProperty(PropertyNames.SCIM_USER);
        scimPassword = environment.getProperty(PropertyNames.SCIM_PWD);
        scimUrl = environment.getProperty(PropertyNames.SCIM_URL);
    }

    public ResponseEntity<?> registerUser(@RequestBody User user){
        if (user == null) {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        scimUser = environment.getProperty(PropertyNames.SCIM_USER);
        scimPassword = environment.getProperty(PropertyNames.SCIM_PWD);
        scimUrl = environment.getProperty(PropertyNames.SCIM_URL);

//        HttpHeaders requestHeaders = new HttpHeaders();
//        String plainCreds = scimUser + ":" + scimPassword;
//        byte[] plainCredsBytes = plainCreds.getBytes();
//        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//        String base64Creds = new String(base64CredsBytes);
//        requestHeaders.add("Authorization", "Basic " + base64Creds);
//        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

//        CloseableHttpClient httpClient =
//                HttpClients.custom()
//                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                        .build();
//        HttpComponentsClientHttpRequestFactory requestFactory =
//                new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        HttpEntity<GluuUser> entity = new HttpEntity<GluuUser>(new GluuUser(user), requestHeaders);

        RestTemplate restTemplate = new BasicAuthRestTemplate(scimUser, scimPassword);

        HttpEntity<GluuUser> entity = new HttpEntity<GluuUser>(new GluuUser(user));
        ResponseEntity<?> responseEntity = restTemplate.exchange(scimUrl, HttpMethod.POST, entity, String.class);

        return new ResponseEntity<User>(HttpStatus.CREATED);
    }
}
