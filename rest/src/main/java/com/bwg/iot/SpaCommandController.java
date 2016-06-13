package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.SpaCommand;
import com.bwg.iot.model.SpaCommand.RequestType;
import com.bwg.iot.model.util.SpaRequestUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.item.validator.ValidationException;
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

    @Autowired
    SpaCommandHelper helper;
    
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
            originatorId = body.get(SpaCommand.REQUEST_ORIGINATOR);
        }
        SpaCommand command = helper.buildCommand(spaId, originatorId, requestCode, new HashMap<>(), true);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{spaId}/setDesiredTemp", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setDesiredTemp(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setDesiredTemp(spaId, body, SpaCommand.RequestType.HEATER.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setJetState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setJetState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.PUMP.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setCircPumpState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setCircPumpState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.CIRCULATION_PUMP.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setLightState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setLightState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {

        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.LIGHT.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setBlowerState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setBlowerState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.BLOWER.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setMisterState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setMisterState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.MISTER.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setOzoneState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setOzoneState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.OZONE.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setMicrosilkState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setMicrosilkState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.MICROSILK.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setAuxState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setAuxState(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setButtonCommand(spaId, body, SpaCommand.RequestType.AUX.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.OK);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RequestMapping(value = "/{spaId}/setAgentSettings", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setAgentSettings(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        if (spaId == null) {
            return new ResponseEntity<>("Spa Id not provided", HttpStatus.BAD_REQUEST);
        }

        final String originatorId = body.get(SpaCommand.REQUEST_ORIGINATOR);
        SpaCommand command = helper.buildCommand(spaId, originatorId, RequestType.UPDATE_AGENT_SETTINGS.getCode(), body, true);

        return new ResponseEntity<SpaCommand>(command, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{spaId}/setFilterCycleIntervals", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> setFilterCycleIntervals(@PathVariable String spaId, @RequestBody HashMap<String, String> body) {
        ResponseEntity<?> response;
        try {
            SpaCommand command = helper.setFilerCycleIntervals(spaId, body, SpaCommand.RequestType.FILTER.getCode(), true);
            response = new ResponseEntity<Object>(command, HttpStatus.ACCEPTED);
        } catch (ValidationException ve) {
            response = new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
