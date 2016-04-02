package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.Spa;
import com.bwg.iot.model.TacUserAgreement;
import com.bwg.iot.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.HashMap;

@RepositoryRestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserRepository userRepository;

    private final String headerName = "oidc_claim_user_name";

    @Autowired
    SpaRepository spaRepository;

    @Autowired
    MongoOperations mongoOps;


    @Autowired
    EntityLinks entityLinks;

    @Autowired
    public AuthenticationController(UserRepository repo){
        userRepository = repo;
    }

    /**
     * Temporary login page for development testing.
     * Will become obsolete once an external identity manager is in place.
     *
     * @param body contains 2 fields.  Username and password (in clear text)
     * @return
     */
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

    @RequestMapping(value = "/whoami", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getUserByHeaderUsername(@RequestHeader(headerName) String username){
        logger.info("/whoami: " + username);
        User user = userRepository.findByUsername(username);
        user.add(entityLinks.linkToSingleResource(User.class, user.get_id()).withSelfRel());
        user.add(entityLinks.linkToSingleResource(User.class, user.get_id()).withRel("user"));

        if (user.hasRole(User.Role.OWNER.toString())) {
            Spa spa = spaRepository.findByUsername(user.getUsername());
            Link link = entityLinks.linkToSingleResource(Spa.class, spa.get_id()).withRel("spa");
            user.add(link);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
