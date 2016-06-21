package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

@RepositoryRestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserRepository userRepository;

    @Autowired
    SpaRepository spaRepository;

    @Autowired
    OemRepository oemRepository;

    @Autowired
    DealerRepository dealerRepository;

    @Autowired
    MongoOperations mongoOps;


    @Autowired
    EntityLinks entityLinks;

    @Autowired
    public AuthenticationController(UserRepository repo, Environment env){
        userRepository = repo;
        this.env = env;
    }

    Environment env;

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
    public ResponseEntity<?> getUserByHeaderUsername(HttpServletRequest request){
        String username = request.getHeader(env.getProperty(PropertyNames.USER_TOKEN_HEADER));
        logger.info("/whoami: " + username);
        if (StringUtils.isEmpty(username)) {
            return new ResponseEntity<Object> (HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<Object> (HttpStatus.FORBIDDEN);
        }
        user.add(entityLinks.linkToSingleResource(User.class, user.get_id()).withSelfRel());
        user.add(entityLinks.linkToSingleResource(User.class, user.get_id()).withRel("user"));

        if (user.hasRole(User.Role.OWNER.toString())) {
            Spa spa = spaRepository.findByUsername(user.getUsername());
            if (spa != null) {
                Link link = entityLinks.linkToSingleResource(Spa.class, spa.get_id()).withRel("spa");
                user.add(link);
            }
        }

        Link logoLink = findMyLogo(user);
        if (logoLink != null) {
            user.add(logoLink);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    private Link findMyLogo(User user) {
        Link logoLink = null;
        if (user.getDealerId() != null) {
            Dealer dealer = dealerRepository.findOne(user.getDealerId());
            if (dealer != null && dealer.getLogo() != null) {
                logoLink = entityLinks.linkToSingleResource(Attachment.class, dealer.getLogo().get_id()).withRel("logo");
            }
        }
        if (logoLink == null && user.getOemId() != null) {
            Oem oem = oemRepository.findOne(user.getOemId());
            if (oem != null && oem.getLogo() != null) {
                logoLink = entityLinks.linkToSingleResource(Attachment.class, oem.getLogo().get_id()).withRel("logo");
            }
        }
        return logoLink;
    }
}
