/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.bwg.iot;

import com.bwg.iot.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  
  @Autowired
  UserRegistrationHelper gluuHelper;
  
  
  @RequestMapping(method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<?> createUser(@RequestBody HashMap<String, String> request) {
    
    User user = new User();
    try {
      String firstName = request.get("firstName");
      String lastName = request.get("lastName");
      String username = request.get("id");
      String email = request.get("email");
      
      
      user.setUsername(username);
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setEmail(email);
      
      if (StringUtils.isNotBlank(username)) {
        JsonNode json = gluuHelper.createUser(user);
        log.info("New user added with id " + json.get("id").textValue());
      } else {
        log.error("username is undefined, aborting user creation");
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    
    ResponseEntity<?> response = new ResponseEntity<User>(user, HttpStatus.OK);
    return response;
  }
  
}
