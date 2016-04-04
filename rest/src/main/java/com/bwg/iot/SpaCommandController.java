package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.SpaCommand;
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
@RequestMapping("/control")
public class SpaCommandController {

    @Autowired
    SpaCommandRepository spaCommandRepository;

    @Autowired
    SpaRepository spaRepository;

    @RequestMapping(value = "/{spaId}/restartAgent", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> restartAgent(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setPlainButtonCommand(spaId, body, SpaCommand.RequestType.RESTART_AGENT.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/rebootGateway", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> rebootGateway(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setPlainButtonCommand(spaId, body, SpaCommand.RequestType.REBOOT_GATEWAY.getCode());
        return response;
    }

    private ResponseEntity<?> setPlainButtonCommand(String spaId, HashMap<String, String> body, int requestCode) {
        if (spaId == null) {
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        String originatorId = null;
        if (body != null) {
            originatorId = body.get("originatorId");
        }
        SpaCommand command = buildAndSaveCommand(spaId, originatorId, requestCode, new HashMap<>());

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{spaId}/setDesiredTemp", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setDesiredTemp(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        if (spaId == null) {
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        String desiredTemp = body.get("desiredTemp");
        if (desiredTemp == null) {
            return new ResponseEntity<>("Desired Temperature not provided", HttpStatus.BAD_REQUEST);
        }
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("DESIREDTEMP", desiredTemp);

        String originatorId = body.get("originatorId");
        SpaCommand command = buildAndSaveCommand(spaId, originatorId, SpaCommand.RequestType.HEATER.getCode(), values);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{spaId}/setJetState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setJetState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.PUMPS.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setCircPumpState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setCircPumpState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.CIRCULATION_PUMP.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setLightState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setLightState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.LIGHTS.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setBlowerState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setBlowerState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.BLOWER.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setMisterState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setMisterState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.MISTER.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setOzoneState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setOzoneState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.OZONE.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setMicrosilkState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setMicrosilkState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.MICROSILK.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setAuxState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setAuxState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response = setButtonCommand(spaId, body, SpaCommand.RequestType.AUX.getCode());
        return response;
    }

    @RequestMapping(value = "/{spaId}/setFilterCycleIntervals", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setFilterCycleIntervals(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        if (spaId == null) {
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        final HashMap<String, String> values = new HashMap<String, String>();

        final String deviceNumber = body.get("deviceNumber");
        if (deviceNumber == null) {
            return new ResponseEntity<>("Device Number Required", HttpStatus.BAD_REQUEST);
        }
        if (deviceNumber != null && !NumberUtils.isNumber(deviceNumber)) {
            return new ResponseEntity<>("Device Number Invalid", HttpStatus.BAD_REQUEST);
        }
        values.put("PORT", deviceNumber);

        final String intervalNumber = body.get("intervalNumber");
        if (intervalNumber == null) {
            return new ResponseEntity<>("Interval Number not provided", HttpStatus.BAD_REQUEST);
        }
        if (!NumberUtils.isNumber(intervalNumber)) {
            return new ResponseEntity<>("Interval Number is not a number", HttpStatus.BAD_REQUEST);
        }
        values.put("FILTER_DURATION_15MINUTE_INTERVALS", intervalNumber);

        final String originatorId = body.get("originatorId");
        SpaCommand command = buildAndSaveCommand(spaId, originatorId, SpaCommand.RequestType.FILTER.getCode(), values);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?> setButtonCommand(String spaId, HashMap<String, String> body, int requestCode) {
        if (spaId == null) {
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        String desiredState = body.get("desiredState");
        if (desiredState == null || !SpaRequestUtil.validState(requestCode, desiredState)) {
            return new ResponseEntity<>("Desired State Invalid", HttpStatus.BAD_REQUEST);
        }

        String deviceNumber = body.get("deviceNumber");
        if (deviceNumber == null && SpaRequestUtil.portRequired(requestCode)) {
            return new ResponseEntity<>("Device Number Required", HttpStatus.BAD_REQUEST);
        }

        if (deviceNumber != null && SpaRequestUtil.portRequired(requestCode) && !NumberUtils.isNumber(deviceNumber)) {
            return new ResponseEntity<>("Device Number Invalid", HttpStatus.BAD_REQUEST);
        }

        if (deviceNumber != null && SpaRequestUtil.portRequired(requestCode) && !SpaRequestUtil.validPort(requestCode, NumberUtils.createInteger(deviceNumber))) {
            return new ResponseEntity<>("Device Number Invalid, out of range", HttpStatus.BAD_REQUEST);
        }

        HashMap<String, String> values = new HashMap<>();
        values.put("DESIREDSTATE", desiredState);
        if (deviceNumber != null) {
            values.put("PORT", deviceNumber);
        }

        String originatorId = body.get("originatorId");
        SpaCommand command = buildAndSaveCommand(spaId, originatorId, requestCode, values);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    private SpaCommand buildAndSaveCommand(String spaId, String originatorId, int requestCode, HashMap<String, String> values) {
        SpaCommand command = new SpaCommand();
        if (originatorId == null) {
            originatorId = UUID.randomUUID().toString();
        }
        command.setSpaId(spaId);
        command.setOriginatorId(originatorId);
        command.setRequestTypeId(requestCode);
        command.setValues(values);
        command.setSentTimestamp(new Date());
        spaCommandRepository.save(command);

        return command;
    }
}
