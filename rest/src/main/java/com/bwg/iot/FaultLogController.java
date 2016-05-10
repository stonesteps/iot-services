package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.FaultLogDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<?> getFaultLogs(@PathVariable("spaId") final String spaId,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") final int page,
                                          @RequestParam(value = "size", required = false, defaultValue = "20") final int size) {

        final Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "timestamp"));

        final Page<FaultLog> faultLogs = faultLogRepository.findBySpaId(spaId, pageable);
        if (faultLogs != null) {
            for (final FaultLog faultLog : faultLogs) {
                appendDescription(faultLog);
            }
        }

        final Resources<FaultLog> resources = new Resources<>(faultLogs);
        resources.add(linkTo(methodOn(FaultLogController.class).getFaultLogs(spaId, page, size)).withSelfRel());
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
