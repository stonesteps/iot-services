package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.SpaCommand;
import com.bwg.iot.model.util.SpaRequestUtil;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
        if (spaId == null){
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        String desiredTemp = body.get("desiredTemp");
        if (desiredTemp == null){
            return new ResponseEntity<>("Desired Temperature not provided", HttpStatus.BAD_REQUEST);
        }
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

    @RequestMapping(value = "/{spaId}/setAuxState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setAuxState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.AUX.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setFilterCycleState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setFilterCycleState(@PathVariable String spaId, @RequestBody HashMap<String,String> body){

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.FILTER.getCode());
        return response;
    }

    private ResponseEntity<?> setButtonCommand(String spaId, HashMap<String, String> body, int requestCode) {
        if (spaId == null){
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        String desiredState = body.get("desiredState");
        if (desiredState == null || !SpaRequestUtil.validState(requestCode, desiredState)){
            return new ResponseEntity<>("Desired State Invalid", HttpStatus.BAD_REQUEST);
        }

        String deviceNumber = body.get("deviceNumber");
        if (deviceNumber == null && SpaRequestUtil.portRequired(requestCode)) {
            return new ResponseEntity<>("Device Number Required", HttpStatus.BAD_REQUEST);
        }

        if (deviceNumber != null && !NumberUtils.isNumber(deviceNumber) && SpaRequestUtil.portRequired(requestCode)) {
            return new ResponseEntity<>("Device Number Invalid", HttpStatus.BAD_REQUEST);
        }

        if (deviceNumber != null && NumberUtils.isNumber(deviceNumber) && SpaRequestUtil.portRequired(requestCode) && !SpaRequestUtil.validPort(requestCode, NumberUtils.createInteger(deviceNumber))) {
            return new ResponseEntity<>("Device Number Invalid, out of range", HttpStatus.BAD_REQUEST);
        }

        HashMap<String,String> values = new HashMap<>();
        values.put("desiredState", desiredState);
        if (deviceNumber != null) {
            values.put("port", deviceNumber);
        }

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
