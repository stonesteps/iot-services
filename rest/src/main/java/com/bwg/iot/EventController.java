package com.bwg.iot;

import com.bwg.iot.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PagedResourcesAssembler<Event> pagedAssembler;

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/events")
    @ResponseBody
    public ResponseEntity<?> getEvents(@PathVariable("spaId") final String spaId,
                                       final Pageable pageable,
                                       PersistentEntityResourceAssembler entityAssembler) {

        final Page<Event> events = eventRepository.findBySpaId(spaId, pageable);
        return ResponseEntity.ok(pagedAssembler.toResource(events, (ResourceAssembler)entityAssembler));
    }
}
