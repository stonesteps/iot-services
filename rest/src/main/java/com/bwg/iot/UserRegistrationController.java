/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.bwg.iot;

import com.bwg.iot.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    Boolean isExceptionDetected = false;
    
    try {
      log.info("creating new user: " + user.getUsername());
      if (StringUtils.isNotBlank(user.getUsername())) {
        JsonNode json = gluuHelper.createUser(user);
        log.info("New user added with Gluu id " + json.get("id").textValue());
        gluuHelper.sendGmailRegistrationMail(user);
      } else {
        log.error("username is undefined, aborting user creation");
      }
    } catch (Throwable t) {
      isExceptionDetected = true;
      log.error("exception in gluuHelper: " + t.getMessage());
      log.error("stacktrace: ",t);
    }
    // save
    log.info("Saving new user in mongo");
    userRepository.save(user);
    HttpStatus status = !isExceptionDetected ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
    
    // fixme
    ResponseEntity<?> response = new ResponseEntity<User>(user, status);
    return response;
  }
  

}
