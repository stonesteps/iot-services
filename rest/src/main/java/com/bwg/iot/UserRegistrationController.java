/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.bwg.iot;

import com.bwg.iot.model.GluuUser;
import com.bwg.iot.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author entando
 */
@RestController
@RequestMapping("/user-registration")
public class UserRegistrationController {
  
  private static final Logger log = LoggerFactory.getLogger(UserRegistrationController.class);
  
  private final UserRepository userRepository;
  
  private String scimUser;
  private String scimPassword;
  private String scimUrl;
  
  @Autowired
  UserRegistrationHelper gluuHelper;
  
  @Autowired
  MongoOperations mongoOps;
  
  @Autowired
  ObjectMapper objectMapper;
  
  @Autowired
  Environment environment;
  
  @Autowired
  public UserRegistrationController(UserRepository repo) {
    userRepository = repo;
  }
  
  @PostConstruct
  public void initialize() {
    scimUser = environment.getProperty(PropertyNames.SCIM_USER);
    scimPassword = environment.getProperty(PropertyNames.SCIM_PWD);
    scimUrl = environment.getProperty(PropertyNames.SCIM_URL);
    log.info("user regsitration controller initialized succesfully");
  }
  
  @RequestMapping(method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<?> createUser(@RequestBody com.bwg.iot.model.User user) {
    try {
      log.info("creating new user: " + user.getUsername());
      if (StringUtils.isNotBlank(user.getUsername())) {
        log.info("About to create new gluu user");
        JsonNode json = gluuHelper.createUser(user);
        log.info("New user added with Gluu id " + json.get("id").textValue());
      } else {
        log.error("username is undefined, aborting user creation");
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    // save
    userRepository.save(user);
    
    ResponseEntity<?> response = new ResponseEntity<User>(user, HttpStatus.OK);
    return response;
  }
  
  /*
  @RequestMapping("/foo")
  void handleFoo(HttpServletResponse response) {
    response.sendRedirect("some-url");
  }
  */
 
  
}
