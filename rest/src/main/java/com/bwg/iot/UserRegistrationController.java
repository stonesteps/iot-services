/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.bwg.iot;

import com.bwg.iot.model.GluuUser;
import com.bwg.iot.model.User;
import com.bwg.iot.validator.BeforeCreateUserValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author entando
 */
@RestController
@RequestMapping("/user-registration")
public class UserRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserRepository userRepository;

    UserRegistrationHelper gluuHelper;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Environment environment;

    @Autowired
    public UserRegistrationController(UserRepository repo, UserRegistrationHelper helper){
        userRepository = repo;
        gluuHelper = helper;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new BeforeCreateUserValidator());
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createUser(@Valid @RequestBody com.bwg.iot.model.User user) {
        try {
            log.info("creating new user: " + user.getUsername());
            if (StringUtils.isNotBlank(user.getUsername())) {
                JsonNode json = gluuHelper.createUser(user);
                log.info("New user added with Gluu id " + json.get("id").textValue());
            } else {
                log.error("username is undefined, aborting user creation");
            }
        } catch (Throwable t) {
            log.error("exception in gluuHelper: " + t.getMessage());
            log.error("stacktrace: ", t);
            throw new RuntimeException("Error creating Gluu user", t);
        }
        // save
        log.info("Saving new user in mongo");
        userRepository.save(user);

        ResponseEntity<?> response = new ResponseEntity<User>(user, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> editUser(@PathVariable("id") String id, @Valid @RequestBody com.bwg.iot.model.User user) {
        User currentUser = userRepository.findOne(id);
        if (currentUser == null) {
            log.info("User with id " + id + " not found");
            return new ResponseEntity<String>("User with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        currentUser.setUsername(user.getUsername());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setAddress(user.getAddress());
        currentUser.setSpaId(user.getSpaId());
        currentUser.setDealerId(user.getDealerId());
        currentUser.setOemId(user.getOemId());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhone(user.getPhone());
        currentUser.setNotes(user.getNotes());
        currentUser.setRoles(user.getRoles());
        currentUser.setModifiedDate(new Date());

        //todo: update gluu user  (probably not necessary) maybe if email changes??

        currentUser = userRepository.save(currentUser);
        currentUser.add(entityLinks.linkToSingleResource(User.class, currentUser.get_id()).withSelfRel());
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }


}
