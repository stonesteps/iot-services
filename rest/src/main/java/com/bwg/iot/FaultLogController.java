package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.FaultLogDescription;
import com.bwg.iot.model.QFaultLog;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
public class FaultLogController {

    @Autowired
    private FaultLogRepository faultLogRepository;

    @Autowired
    private FaultLogDescriptionRepository faultLogDescriptionRepository;

    private final Map<String, FaultLogDescription> cache = new HashMap<>();

    @Autowired
    private CommonHelper commonHelper;

    @RequestMapping(method = RequestMethod.GET, value = "/faults")
    @ResponseBody
    public ResponseEntity<?>  getAllWifiStats(@RequestHeader(name="remote_user")String remote_user,
                                              @QuerydslPredicate(root = FaultLog.class) Predicate predicate,
                                              final Pageable pageable,
                                              PagedResourcesAssembler assembler) {

        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));

        Predicate userQualifiers;
        try {
            userQualifiers = getUserQualifiers(remote_user);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }

        BooleanBuilder bb = new BooleanBuilder(userQualifiers).and(predicate);

        assembler.setForceFirstAndLastRels(true);
        Page<FaultLog> events = faultLogRepository.findAll(bb.getValue(), pageRequest);

        return ResponseEntity.ok(assembler.toResource(events));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/faultLogs")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<FaultLog>>> getFaultLogs(@PathVariable("spaId") final String spaId,
                           @QuerydslPredicate(root = FaultLog.class) Predicate predicate,
                           final Pageable pageable,
                           PagedResourcesAssembler assembler) {

        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));

        QFaultLog qFaultLog = QFaultLog.faultLog;
        BooleanBuilder bb = new BooleanBuilder(qFaultLog.spaId.eq(spaId)).and(predicate);
        final Page<FaultLog> faultLogs = faultLogRepository.findAll(bb.getValue(), pageRequest);

        if (faultLogs != null) {
            for (final FaultLog faultLog : faultLogs) {
                appendDescription(faultLog);
            }
        }
        assembler.setForceFirstAndLastRels(true);
        return ResponseEntity.ok(assembler.toResource(faultLogs));
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

    private Predicate getUserQualifiers(String remote_user) {
        BooleanBuilder bb = new BooleanBuilder();
        Pair<String, String> userQualifier = commonHelper.getUserQualifier(remote_user);
        // BWG case, no qualifiers
        if (userQualifier == null) {
            return null;
        }
        switch (userQualifier.getKey()) {
            case "oemId":
                bb.and(QFaultLog.faultLog.oemId.eq(userQualifier.getValue()));
                break;
            case "dealerId":
                bb.and(QFaultLog.faultLog.dealerId.eq(userQualifier.getValue()));
                break;
            case "spaId":
                bb.and(QFaultLog.faultLog.spaId.eq(userQualifier.getValue()));
                break;
            default:
                throw new IllegalStateException("invalid user qualifier: " + userQualifier.getKey());
        }
        return bb.getValue();
    }
}
