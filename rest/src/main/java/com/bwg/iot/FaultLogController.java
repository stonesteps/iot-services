package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class FaultLogController {

    @Autowired
    private FaultLogRepository faultLogRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/faultLogs")
    @ResponseBody
    public ResponseEntity<?> getFaultLogs(@PathVariable("spaId") final String spaId) {
        final List<FaultLog> faultLogs = faultLogRepository.findBySpaId(spaId);
        final Resources<FaultLog> resources = new Resources<>(faultLogs);

        resources.add(linkTo(methodOn(FaultLogController.class).getFaultLogs(spaId)).withSelfRel());

        return ResponseEntity.ok(resources);
    }
}
