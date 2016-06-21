package com.bwg.iot;

import com.bwg.iot.model.MeasurementReading;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class MeasurementReadingController {

    @Autowired
    private MeasurementReadingRepository measurementReadingRepository;

    @Autowired
    private PagedResourcesAssembler<MeasurementReading> pagedAssembler;

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/measurements")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<MeasurementReading>>> getMeasurements(@PathVariable("spaId") final String spaId,
                                                                                    @RequestParam("measurementType") final String type,
                                                                                    final Pageable pageable,
                                                                                    PersistentEntityResourceAssembler entityAssembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        pagedAssembler.setForceFirstAndLastRels(true);

        final Page<MeasurementReading> readings = measurementReadingRepository.findBySpaIdAndType(spaId, type, pageRequest);
        return ResponseEntity.ok(pagedAssembler.toResource(readings, (ResourceAssembler) entityAssembler));
    }

    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "timestamp");
    }
}
