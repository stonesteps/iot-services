package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.GluuUser;
import com.bwg.iot.model.Spa;
import com.bwg.iot.model.SpaTemplate;
import com.bwg.iot.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@RepositoryRestController
@RequestMapping("/spaTemplates")
public class SpaTemplateController {
    private final static Logger log = LoggerFactory.getLogger(SpaTemplateController.class);

    private final SpaTemplateRepository spaTemplateRepository;

    @Autowired
    SpaRepository spaRepository;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public SpaTemplateController(SpaTemplateRepository repo) {
        spaTemplateRepository = repo;
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> removeSpaTemplate(@PathVariable String id) throws IOException {
        ResponseEntity<?> response;

        // get spaTemplate, verify it exists
        SpaTemplate existingTemplate = spaTemplateRepository.findOne(id);
        if (existingTemplate == null) {
            return new ResponseEntity<String>("Spa Template '" + id + "' not found.",HttpStatus.NOT_FOUND);
        }

        // see if spa template is used in any spas
        List<Spa> existingSpas = spaRepository.findByTemplateId(id);
        if (existingSpas == null || existingSpas.isEmpty()) {
            // spa template not used.  Delete it
            spaTemplateRepository.delete(id);
            response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            existingTemplate.setActive(false);
            spaTemplateRepository.save(existingTemplate);
            response = new ResponseEntity<SpaTemplate>(existingTemplate, HttpStatus.OK);
        }

        return response;
    }

}
