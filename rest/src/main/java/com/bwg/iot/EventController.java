package com.bwg.iot;

import com.bwg.iot.model.Event;
import com.bwg.iot.model.QEvent;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;


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
}
