package com.bwg.iot;

/**
 * Created by triton on 6/08/16.
 */

import com.bwg.iot.model.Spa;
import com.bwg.iot.model.SpaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RepositoryRestController
@RequestMapping("/spaTemplates")
public class SpaTemplateController {
    private final static Logger log = LoggerFactory.getLogger(SpaTemplateController.class);

    private final SpaTemplateRepository spaTemplateRepository;

    @Autowired
    SpaRepository spaRepository;

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
