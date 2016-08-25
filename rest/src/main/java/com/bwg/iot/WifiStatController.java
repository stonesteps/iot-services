package com.bwg.iot;

import com.bwg.iot.model.QWifiStat;
import com.bwg.iot.model.WifiStat;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WifiStatController {

    @Autowired
    private WifiStatRepository wifiStatRepository;

    @Autowired
    private CommonHelper commonHelper;

    @RequestMapping(method = RequestMethod.GET, value = "/wifi")
    @ResponseBody
    public ResponseEntity<?>  getAllWifiStats(@RequestHeader(name="remote_user")String remote_user,
                                           @QuerydslPredicate(root = WifiStat.class) Predicate predicate,
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
        Page<WifiStat> events = wifiStatRepository.findAll(bb.getValue(), pageRequest);

        return ResponseEntity.ok(assembler.toResource(events));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/wifiStats")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<WifiStat>>> getWifiStats(@PathVariable("spaId") final String spaId,
                           @QuerydslPredicate(root = WifiStat.class) Predicate predicate,
                           final Pageable pageable,
                           PagedResourcesAssembler assembler) {
        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        assembler.setForceFirstAndLastRels(true);

        QWifiStat qWifiStat = QWifiStat.wifiStat;
        BooleanBuilder bb = new BooleanBuilder(qWifiStat.spaId.eq(spaId)).and(predicate);
        final Page<WifiStat> wifiStats = wifiStatRepository.findAll(bb.getValue(), pageRequest);

        return ResponseEntity.ok(assembler.toResource(wifiStats));
    }

    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "recordedDate");
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
                bb.and(QWifiStat.wifiStat.oemId.eq(userQualifier.getValue()));
                break;
            case "dealerId":
                bb.and(QWifiStat.wifiStat.dealerId.eq(userQualifier.getValue()));
                break;
            case "spaId":
                bb.and(QWifiStat.wifiStat.spaId.eq(userQualifier.getValue()));
                break;
            default:
                throw new IllegalStateException("invalid user qualifier: " + userQualifier.getKey());
        }
        return bb.getValue();
    }    
}
