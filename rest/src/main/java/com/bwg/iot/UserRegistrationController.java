/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.bwg.iot;

import com.bwg.iot.model.User;
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
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
  
  @Autowired
  UserRegistrationHelper gluuHelper;
  
  
  @RequestMapping(method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<?> createUser(@RequestBody User request) {
    
    try {
      gluuHelper.createUser();
    } catch (Throwable t) {
      t.printStackTrace();
    }
    User user = new User();
    
    ResponseEntity<?> response = new ResponseEntity<User>(user, HttpStatus.OK);
    return response;
  }
  
}
