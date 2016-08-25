package com.bwg.iot;

import com.bwg.iot.model.Event;
import com.bwg.iot.model.QEvent;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CommonHelper commonHelper;

    @RequestMapping(method = RequestMethod.GET, value = "/events")
    @ResponseBody
    public ResponseEntity<?>  getAllEvents(@RequestHeader(name="remote_user")String remote_user,
                             @QuerydslPredicate(root = Event.class) Predicate predicate,
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
        Page<Event> events = eventRepository.findAll(bb.getValue(), pageRequest);

        return ResponseEntity.ok(assembler.toResource(events));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/events")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<Event>>> getEvents(@PathVariable("spaId") final String spaId,
                         @QuerydslPredicate(root = Event.class) Predicate predicate,
                         final Pageable pageable,
                         PagedResourcesAssembler assembler) {

        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        assembler.setForceFirstAndLastRels(true);

        QEvent qEvent = QEvent.event;
        BooleanBuilder bb = new BooleanBuilder(qEvent.spaId.eq(spaId)).and(predicate);
        Page<Event> events = eventRepository.findAll(bb.getValue(), pageRequest);

        return ResponseEntity.ok(assembler.toResource(events));
    }

    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "eventOccuredTimestamp");
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
                bb.and(QEvent.event.oemId.eq(userQualifier.getValue()));
                break;
            case "dealerId":
                bb.and(QEvent.event.dealerId.eq(userQualifier.getValue()));
                break;
            case "spaId":
                bb.and(QEvent.event.spaId.eq(userQualifier.getValue()));
                break;
            default:
                throw new IllegalStateException("invalid user qualifier: " + userQualifier.getKey());
        }
        return bb.getValue();
    }
}
