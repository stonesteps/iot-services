package com.bwg.iot;

import com.bwg.iot.model.MeasurementReading;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@RepositoryRestController
public class MeasurementReadingController {

    @Autowired
    private MeasurementReadingRepository measurementReadingRepository;

    @Autowired
    private PagedResourcesAssembler<MeasurementReading> pagedAssembler;

    @RequestMapping(method = RequestMethod.GET, value = {"/spas/{spaId}/measurements"})
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurementsBySpa(@PathVariable("spaId") final String spaId,
                                                                                    @RequestParam(value = "measurementType", required = false) final Optional<String> type,
                                                                                    final Pageable pageable,
                                                                                    PersistentEntityResourceAssembler entityAssembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        pagedAssembler.setForceFirstAndLastRels(true);

        final Page<MeasurementReading> readings;
        // this gets all measurements of a specific type that came from a single spa, includes all sensors per spa regardless of mote
        if (type.isPresent()) {
            readings = measurementReadingRepository.findBySpaIdAndType(spaId, type.get(), pageRequest);
        } else {
            readings = measurementReadingRepository.findBySpaId(spaId, pageRequest);
        }

        return ResponseEntity.ok(pagedAssembler.toResource(readings, (ResourceAssembler) entityAssembler));
    }

    @RequestMapping(method = RequestMethod.GET, value="/spas/measurements", params="moteId")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurementsByMoteId(@RequestParam(value = "moteId", required = true) final String moteId,
                                                                                    @RequestParam(value = "measurementType", required = false) final String type,
                                                                                    final Pageable pageable,
                                                                                    PersistentEntityResourceAssembler entityAssembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        pagedAssembler.setForceFirstAndLastRels(true);

        final Page<MeasurementReading> readings;

        // this gets all measurements of a specific type that came from a single spa and mote, includes all sensors per spa and mote
        // if multiple sensors of same type attached to mote, then they will all be included
        if (StringUtils.isBlank(type)) {
            readings = measurementReadingRepository.findByMoteId(moteId, pageRequest);
        } else {
            readings = measurementReadingRepository.findByMoteIdAndType(moteId, type, pageRequest);
        }

        return ResponseEntity.ok(pagedAssembler.toResource(readings, (ResourceAssembler) entityAssembler));
    }

    @RequestMapping(method = RequestMethod.GET, value="/spas/measurements", params="sensorId")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurementsBySensorId(@RequestParam(value = "sensorId", required = true) final String sensorId,
                                                                                            final Pageable pageable,
                                                                                            PersistentEntityResourceAssembler entityAssembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        pagedAssembler.setForceFirstAndLastRels(true);

        final Page<MeasurementReading> readings;

        // this gets all measurements per spa per mote and per single sensor
        readings = measurementReadingRepository.findBySensorId(sensorId, pageRequest);
        return ResponseEntity.ok(pagedAssembler.toResource(readings, (ResourceAssembler) entityAssembler));
    }

    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "timestamp");
    }
}
