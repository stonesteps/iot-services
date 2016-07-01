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

    @RequestMapping(method = RequestMethod.GET, value = {"/spas/{spaId}/measurements", "/spas/measurements"})
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurements(@PathVariable("spaId") final Optional<String> spaId,
                                                                                    @RequestParam(value = "measurementType", required = false) final Optional<String> type,
                                                                                    @RequestParam(value = "moteId", required = false) final Optional<String> moteId,
                                                                                    @RequestParam(value = "sensorId", required = false) final Optional<String> sensorId,
                                                                                    final Pageable pageable,
                                                                                    PersistentEntityResourceAssembler entityAssembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        pagedAssembler.setForceFirstAndLastRels(true);

        final Page<MeasurementReading> readings;
        if (moteId.isPresent() && StringUtils.isNotBlank(moteId.get())) {
            if (sensorId.isPresent() && StringUtils.isNotBlank(sensorId.get())) {
                // this gets all measurements per spa per mote and per single sensor
                readings = measurementReadingRepository.findByMoteIdAndSensorIdentity(moteId.get(), sensorId.get(), pageRequest);
            } else {
                // this gets all measurements of a specific type that came from a single spa and mote, includes all sensors per spa and mote
                // if multiple sensors of same type attached to mote, then they will all be included
                if (!type.isPresent()) {
                    readings = measurementReadingRepository.findByMoteId(moteId.get(), pageRequest);
                } else {
                    readings = measurementReadingRepository.findByMoteIdAndType(moteId.get(), type.get(), pageRequest);
                }
            }
        } else if (spaId.isPresent()){
            // this gets all measurements of a specific type that came from a single spa, includes all sensors per spa regardless of mote
            readings = measurementReadingRepository.findBySpaIdAndType(spaId.get(), type.get(), pageRequest);
        } else {
            readings = new PageImpl<>(newArrayList());
        }
        return ResponseEntity.ok(pagedAssembler.toResource(readings, (ResourceAssembler) entityAssembler));
    }

    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "timestamp");
    }
}
