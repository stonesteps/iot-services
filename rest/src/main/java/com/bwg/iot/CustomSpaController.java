package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.SellSpaRequest;
import com.bwg.iot.model.Spa;
import com.bwg.iot.model.SpaCommand;
import com.bwg.iot.model.SpaCommand.RequestType;
import com.bwg.iot.model.User;
import com.bwg.iot.model.util.SpaRequestUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/spas")
public class CustomSpaController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpaRepository spaRepository;

    @RequestMapping(value = "/{spaId}/sellSpa", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> restartAgent(@PathVariable String spaId, @RequestBody SellSpaRequest request) {
        String ownerId = request.getOwnerId();
        String associateId = request.getAssociateId();
        String technicianId = request.getTechnicianId();
        Date salesDate = request.getSalesDate();

        User owner = userRepository.findOne(ownerId);
        Spa spa = spaRepository.findOne(spaId);

        if (owner == null) {
            return new ResponseEntity<String>("Invalid Owner ID, owner not found",HttpStatus.BAD_REQUEST);
        }
        if (spa == null) {
            return new ResponseEntity<String>("Invalid Spa ID, spa not found",HttpStatus.BAD_REQUEST);
        }

        owner.setSpaId(spa.get_id());
        userRepository.save(owner);

        spa.setOwner(owner);
        spa.setAssociateId(associateId);
        if (technicianId != null) {
            spa.setTechnicianId(technicianId);
        }
        if (salesDate == null) {
            salesDate = new Date();
        }
        spa.setSalesDate(salesDate);
        spaRepository.save(spa);

        ResponseEntity<?> response = new ResponseEntity<Spa>(spa, HttpStatus.OK);
        return response;
    }

}
