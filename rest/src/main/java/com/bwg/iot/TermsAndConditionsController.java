package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.TacUserAgreement;
import com.bwg.iot.model.TermsAndConditions;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RepositoryRestController
@RequestMapping("/tac")
public class TermsAndConditionsController {

    private final TermsAndConditionsRepository termsAndConditionsRepository;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    TacUserAgreementRepository tacUserAgreementRepository;

    @Autowired
    public TermsAndConditionsController(TermsAndConditionsRepository repo){
        termsAndConditionsRepository = repo;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/findMostRecent")
    public @ResponseBody ResponseEntity<?> getCurrent() {
        TermsAndConditions tac = termsAndConditionsRepository.findByCurrent(true);

        return new ResponseEntity<TermsAndConditions>(tac, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createTermsAndConditions(@RequestBody HashMap<String,String> body){

        // mark any others as NOT current
        WriteResult wr = mongoOps.updateMulti(
                query(where("current").is(Boolean.TRUE)),
                new Update().set("current", Boolean.FALSE),
                TermsAndConditions.class);
        System.out.println(" *** " + wr.getN() + "TaCs marked as old");

        TermsAndConditions tac = new TermsAndConditions();
        tac.setText(body.get("text"));
        tac.setVersion(body.get("version"));
        tac.setCreatedTimestamp(LocalDateTime.now().toString());
        tac.setCurrent(Boolean.TRUE);
        tac = termsAndConditionsRepository.save(tac);

        // invalidate all existing user aggreements
        wr = mongoOps.updateMulti(
                query(where("current").is(Boolean.TRUE)),
                new Update().set("current", Boolean.FALSE),
                TacUserAgreement.class);
        System.out.println(" *** " + wr.getN() + "TaC Agreements reset");

        return new ResponseEntity<TermsAndConditions>(tac, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/agree", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createAgreement(@RequestBody HashMap<String,String> body){
        TacUserAgreement agreement = new TacUserAgreement();
        agreement.setUserId(body.get("userId"));
        agreement.setVersion(body.get("version"));
        agreement.setCurrent(Boolean.TRUE);
        agreement.setDateAgreed(LocalDateTime.now().toString());
        agreement = tacUserAgreementRepository.save(agreement);
        return new ResponseEntity<TacUserAgreement>(agreement, HttpStatus.OK);
    }

}
