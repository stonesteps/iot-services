package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.FaultLogDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    private PagedResourcesAssembler<FaultLog> pagedAssembler;

    private final Map<String, FaultLogDescription> cache = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/faultLogs")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<FaultLog>>> getFaultLogs(@PathVariable("spaId") final String spaId,
                                                                       final Pageable pageable,
                                                                       PersistentEntityResourceAssembler entityAssembler) {

        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));

        final Page<FaultLog> faultLogs = faultLogRepository.findBySpaId(spaId, pageRequest);
        if (faultLogs != null) {
            for (final FaultLog faultLog : faultLogs) {
                appendDescription(faultLog);
            }
        }
        pagedAssembler.setForceFirstAndLastRels(true);
        return ResponseEntity.ok(pagedAssembler.toResource(faultLogs, (ResourceAssembler)entityAssembler));
    }

    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "timestamp");
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
