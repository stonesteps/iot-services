package com.bwg.iot;

import com.bwg.iot.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/events")
    @ResponseBody
    public ResponseEntity<?> getEvents(@PathVariable("spaId") final String spaId, final Pageable pageable) {

        final Page<Event> events = eventRepository.findBySpaId(spaId, pageable);

        final Resources<Event> resources = new Resources<>(events);
        resources.add(linkTo(methodOn(EventController.class).getEvents(spaId, pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }
}
