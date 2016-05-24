package com.bwg.iot;

import com.bwg.iot.model.WifiStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class WifiStatController {

    @Autowired
    private WifiStatRepository wifiStatRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/wifiStats")
    @ResponseBody
    public ResponseEntity<?> getWifiStats(@PathVariable("spaId") final String spaId,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") final int page,
                                          @RequestParam(value = "size", required = false, defaultValue = "20") final int size) {

        final Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "recordedDate"));

        final Page<WifiStat> wifiStats = wifiStatRepository.findBySpaId(spaId, pageable);

        final Resources<WifiStat> resources = new Resources<>(wifiStats);
        resources.add(linkTo(methodOn(WifiStatController.class).getWifiStats(spaId, page, size)).withSelfRel());
        return ResponseEntity.ok(resources);
    }
}
