package com.bwg.iot;

import com.bwg.iot.model.MeasurementReading;
import com.bwg.iot.model.QMeasurementReading;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Controller
public class MeasurementReadingController {

    @Autowired
    private MeasurementReadingRepository measurementReadingRepository;

    @Autowired
    private CommonHelper commonHelper;

    @RequestMapping(method = RequestMethod.GET, value = {"/spas/{spaId}/measurements"})
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurementsBySpa(@PathVariable("spaId") final String spaId,
                            @QuerydslPredicate(root = MeasurementReading.class) Predicate predicate,
                            @RequestParam(value = "measurementType", required = false) final Optional<String> type,
                            final Pageable pageable,
                            PagedResourcesAssembler assembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));

        // this gets all measurements of a specific type that came from a single spa, includes all sensors per spa regardless of mote
        QMeasurementReading qMeasurementReading = QMeasurementReading.measurementReading;
        BooleanBuilder bb = new BooleanBuilder(qMeasurementReading.spaId.eq(spaId).and(predicate));
        if (type.isPresent()) {
            bb.and(qMeasurementReading.type.eq(type.get()));
        }
        final Page<MeasurementReading> readings = measurementReadingRepository.findAll(bb.getValue(), pageRequest);

        assembler.setForceFirstAndLastRels(true);
        return ResponseEntity.ok(assembler.toResource(readings));
    }

    @RequestMapping(method = RequestMethod.GET, value="/spas/measurements", params="moteId")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurementsByMoteId(@RequestParam(value = "moteId", required = true) final String moteId,
                            @QuerydslPredicate(root = MeasurementReading.class) Predicate predicate,
                            @RequestParam(value = "measurementType", required = false) final String type,
                            final Pageable pageable,
                            PagedResourcesAssembler assembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));

        // this gets all measurements of a specific type that came from a single spa and mote, includes all sensors per spa and mote
        // if multiple sensors of same type attached to mote, then they will all be included
        QMeasurementReading qMeasurementReading = QMeasurementReading.measurementReading;
        BooleanBuilder bb = new BooleanBuilder(qMeasurementReading.moteId.eq(moteId).and(predicate));
        if (!StringUtils.isBlank(type)) {
            bb.and(qMeasurementReading.type.eq(type));
        }
        final Page<MeasurementReading> readings = measurementReadingRepository.findAll(bb.getValue(), pageRequest);

        assembler.setForceFirstAndLastRels(true);
        return ResponseEntity.ok(assembler.toResource(readings));
    }

    @RequestMapping(method = RequestMethod.GET, value="/spas/measurements", params="sensorId")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurementsBySensorId(@RequestParam(value = "sensorId", required = true) final String sensorId,
                          @QuerydslPredicate(root = MeasurementReading.class) Predicate predicate,
                          final Pageable pageable, PagedResourcesAssembler assembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));

        // this gets all measurements per spa per mote and per single sensor
        QMeasurementReading qMeasurementReading = QMeasurementReading.measurementReading;
        BooleanBuilder bb = new BooleanBuilder(qMeasurementReading.sensorId.eq(sensorId)).and(predicate);
        final Page<MeasurementReading> readings = measurementReadingRepository.findAll(bb.getValue(), pageRequest);
        assembler.setForceFirstAndLastRels(true);
        return ResponseEntity.ok(assembler.toResource(readings));
    }

    @RequestMapping(method = RequestMethod.GET, value="/measurements")
    @ResponseBody
    public ResponseEntity<?> getMeasurements(@RequestHeader(name="remote_user") String remote_user,
                                                                                              @QuerydslPredicate(root = MeasurementReading.class) Predicate predicate,
                                                                                              final Pageable pageable, PagedResourcesAssembler assembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));

        Predicate userQualifiers;
        try {
            userQualifiers = getUserQualifiers(remote_user);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }

        BooleanBuilder bb = new BooleanBuilder(userQualifiers).and(predicate);
        final Page<MeasurementReading> readings = measurementReadingRepository.findAll(bb.getValue(), pageRequest);

        assembler.setForceFirstAndLastRels(true);
        return ResponseEntity.ok(assembler.toResource(readings));
    }

    private Predicate getUserQualifiers(String remote_user) {
        BooleanBuilder bb = new BooleanBuilder();
        Pair<String, String> userQualifier = commonHelper.getUserQualifier(remote_user);
        // Root case, no qualifiers
        if (userQualifier == null) {
            return null;
        }
        switch (userQualifier.getKey()) {
            case "oemId":
                bb.and(QMeasurementReading.measurementReading.oemId.eq(userQualifier.getValue()));
                break;
            case "dealerId":
                bb.and(QMeasurementReading.measurementReading.dealerId.eq(userQualifier.getValue()));
                break;
            case "spaId":
                bb.and(QMeasurementReading.measurementReading.spaId.eq(userQualifier.getValue()));
                break;
            default:
                throw new IllegalStateException("invalid user qualifier: " + userQualifier.getKey());
        }
        return bb.getValue();
    }
    
    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "timestamp");
    }
}
