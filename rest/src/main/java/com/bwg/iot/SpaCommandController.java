package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.SpaCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/control")
public class SpaCommandController {

    @Autowired
    SpaCommandRepository spaCommandRepository;

    @Autowired
    SpaRepository spaRepository;

    @RequestMapping(value = "/{spaId}/setDesiredTemp", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setDesiredTemp(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        // TODO: confirm spa exists
        if (spaId == null){
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        String desiredTemp = body.get("desiredTemp");
        if (desiredTemp == null){
            return new ResponseEntity<>("Desired Temperature not provided", HttpStatus.BAD_REQUEST);
        }
        // TODO: validate temperature
        BigInteger tempInt = new BigInteger(body.get("desiredTemp"));
        HashMap<String,String> values = new HashMap<String,String>();
        values.put("desiredTemp", desiredTemp);

        SpaCommand command = new SpaCommand();
        String originatorId = body.get("originatorId");
        if (originatorId == null){
            originatorId = UUID.randomUUID().toString();
        }
        command.setSpaId(spaId);
        command.setOriginatorId(originatorId);
        command.setRequestTypeId("ABCD");
        command.setValues(values);
        command.setSentTimestamp(LocalDateTime.now().toString());
        spaCommandRepository.save(command);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

}
