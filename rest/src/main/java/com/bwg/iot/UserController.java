package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.HashMap;

@RepositoryRestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    public UserController(UserRepository repo){
        userRepository = repo;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> registerUser(@RequestBody HashMap<String,String> body){
        return new ResponseEntity<User>(HttpStatus.OK);
    }
}
