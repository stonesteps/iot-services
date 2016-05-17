package com.bwg.iot;

/**
 * Created by triton on 5/5/16.
 */
import com.bwg.iot.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RepositoryRestController
@RequestMapping("/")
public class SpaRegistrationController {
    private final static Logger log = LoggerFactory.getLogger(SpaRegistrationController.class);

    private UserRepository userRepository;

    private String clientId;
    private String clientSecret;
    private String tokenUrl;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    SpaRepository spaRepository;

    @Autowired
    AddressRepository addressRepository;

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
        // TODO: why doesn't this work?
        clientId = environment.getProperty(PropertyNames.MOBILE_CLIENT_ID);
        clientSecret = environment.getProperty(PropertyNames.MOBILE_CLIENT_SECRET);
        tokenUrl = environment.getProperty(PropertyNames.TOKEN_ENDPOINT);
    }

    public SpaRegistrationController() {
        super();
    }

    @RequestMapping(value = "/registerSpaToUser", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> registerSpaToUser(@RequestBody SpaRegistrationRequest request) throws IOException {

        // validate user inputs (do we have reg_key, spaId?)
        String spaId = request.getSpaId();
        String regKey = request.getRegKey();
        String accessToken = request.getAccess_token();
        User user = request.getUser();

        if (regKey == null || spaId == null) {
            return new ResponseEntity<String>("Missing required parameter(s).",HttpStatus.BAD_REQUEST);
        }

        // validate regKey
        Spa mySpa = mongoOps.findById(spaId, Spa.class);
        if (mySpa == null || mySpa.getRegKey() == null || !mySpa.getRegKey().equals(regKey)) {
//            return new ResponseEntity<String>("Invalid spaId or regKey does not match", HttpStatus.BAD_REQUEST);
        }

        // if existing user, validate auth token
        // if new user, create new user
        GluuToken token = new GluuToken();
        if (accessToken != null){
            // TODO: validate token, get userId
            token.setAccess_token(accessToken);
        } else if(user != null) {
            // Unathenticated User passed in create a new spa system user
            Address address = addressRepository.save(user.getAddress());
            user.setAddress(address);
            user = userRepository.save(user);
            // TODO: Create a new user account in GLUU IDM
            // Get and access token to return to the new user
            token = authenticateUser(user);
        } else {
            // Neither auth token or new user passed in, bad request
            return new ResponseEntity<String>("Missing either new user object or existing user access token.", HttpStatus.BAD_REQUEST);
        }

        // set lat/lon if not null
        if (request.getLon() != null && request.getLat() != null) {
            mySpa.setLocation(new double[] {request.getLon().doubleValue(), request.getLat().doubleValue()});
        }

        // assign spa to user
        mySpa.setOwner(user);
        spaRepository.save(mySpa);
        user.setSpaId(spaId);
        userRepository.save(user);

        SpaRegistrationResponse response = new SpaRegistrationResponse(spaId, token);
        return new ResponseEntity<SpaRegistrationResponse>(response, HttpStatus.CREATED);
    }

    private GluuToken authenticateUser(User user) throws IOException {
        clientId = environment.getProperty(PropertyNames.MOBILE_CLIENT_ID);
        clientSecret = environment.getProperty(PropertyNames.MOBILE_CLIENT_SECRET);
        tokenUrl = environment.getProperty(PropertyNames.TOKEN_ENDPOINT);

        RestTemplate restTemplate = new BasicAuthRestTemplate(clientId, clientSecret);
        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        restTemplate.setMessageConverters(Arrays.asList(formHttpMessageConverter, stringHttpMessageConverter));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        // TODO: once Gluu user is properly created, use new user's creds
        params.add("username", "oosborn");
        params.add("password", "oosborn");
        params.add("scope", "openid user_name");
        params.add("grant_type", "password");

        String tokenString = restTemplate.postForObject(tokenUrl, params, String.class);
        GluuToken myToken = objectMapper.readValue(tokenString, GluuToken.class);
        return myToken;
    }
}
