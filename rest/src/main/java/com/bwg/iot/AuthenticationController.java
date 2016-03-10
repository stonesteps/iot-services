package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.TacUserAgreement;
import com.bwg.iot.model.TermsAndConditions;
import com.bwg.iot.model.User;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RepositoryRestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserRepository userRepository;

    @Autowired
    MongoOperations mongoOps;


    @Autowired
    EntityLinks entityLinks;

    @Autowired
    public AuthenticationController(UserRepository repo){
        userRepository = repo;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> login(@RequestBody HashMap<String,String> body){
        String username = body.get("username");
        String password = body.get("password");

        User user = userRepository.findByUsername(username);

        if (user == null || !user.doesPasswordMatch(password)){
            return new ResponseEntity<TacUserAgreement>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
