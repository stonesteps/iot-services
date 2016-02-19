package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.SpaCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/control")
public class SpaCommandController {

    @Autowired
    SpaCommandRepository spaCommandRepository;

    @Autowired
    SpaRepository spaRepository;

    @RequestMapping(value = "/{spaId}/setDesiredTemp", method = RequestMethod.POST, produces = "application/json")
    public SpaCommand setDesiredTemp(@RequestBody SpaCommand command){
        // TODO: create generator id if not provided
        // TODO: verify temperature setting within range
        // TODO: store command to db

        return command;
    }

}
