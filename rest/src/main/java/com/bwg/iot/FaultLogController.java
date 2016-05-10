package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.FaultLogDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class FaultLogController {

    @Autowired
    private FaultLogRepository faultLogRepository;

    @Autowired
    private FaultLogDescriptionRepository faultLogDescriptionRepository;

    private final Map<String, FaultLogDescription> cache = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/faultLogs")
    @ResponseBody
    public ResponseEntity<?> getFaultLogs(@PathVariable("spaId") final String spaId) {
        final List<FaultLog> faultLogs = faultLogRepository.findBySpaId(spaId);
        if (faultLogs != null) {
            for (final FaultLog faultLog : faultLogs) {
                appendDescription(faultLog);
            }
        }

        final Resources<FaultLog> resources = new Resources<>(faultLogs);
        resources.add(linkTo(methodOn(FaultLogController.class).getFaultLogs(spaId)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    public void appendDescription(final FaultLog faultLog) {
        final int code = faultLog.getCode();
        final String controllerType = faultLog.getControllerType();

        final String cacheKey = controllerType + code;
        FaultLogDescription description = cache.get(cacheKey);

        if (description == null && !cache.containsKey(cacheKey)) {
            description = faultLogDescriptionRepository.findFirstByCodeAndControllerType(code, controllerType);
            cache.put(cacheKey, description);
        }

        faultLog.setFaultLogDescription(description);
    }
}
