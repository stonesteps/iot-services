package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.SpaCommand;
import org.apache.commons.lang3.EnumUtils;
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

        String originatorId = body.get("originatorId");
        SpaCommand command = buildAndSaveCommand(spaId, originatorId, SpaCommand.RequestType.HEATER.getCode(), values);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{spaId}/setJetState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setJetState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){
        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.PUMPS.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setLightState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setLightState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.LIGHTS.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setBlowerState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setBlowerState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.BLOWER.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setMisterState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setMisterState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.MISTER.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setOzoneState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setOzoneState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.OZONE.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setMicrosilkState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setMicrosilkState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.MICROSILK.getCode());
        return response;
    }

    private ResponseEntity<?> setButtonCommand(String spaId, HashMap<String, String> body, int requestCode) {
        // TODO: confirm spa exists
        if (spaId == null){
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        String desiredState = body.get("desiredState");
        if (desiredState == null || !EnumUtils.isValidEnum(SpaCommand.OnOff.class, desiredState)){
            return new ResponseEntity<>("Desired Light State Invalid", HttpStatus.BAD_REQUEST);
        }

        HashMap<String,String> values = new HashMap<String,String>();
        values.put("port", body.get("deviceNumber"));
        values.put("desiredState", desiredState);

        String originatorId = body.get("originatorId");
        SpaCommand command = buildAndSaveCommand(spaId, originatorId, requestCode, values);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    private SpaCommand buildAndSaveCommand(String spaId, String originatorId, int requestCode, HashMap values) {
        SpaCommand command = new SpaCommand();
        if (originatorId == null){
            originatorId = UUID.randomUUID().toString();
        }
        command.setSpaId(spaId);
        command.setOriginatorId(originatorId);
        command.setRequestTypeId(requestCode);
        command.setValues(values);
        command.setSentTimestamp(LocalDateTime.now().toString());
        spaCommandRepository.save(command);

        return command;
    }
}
