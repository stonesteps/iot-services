package com.bwg.iot;

import com.bwg.iot.model.WifiStat;
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
public class WifiStatController {

    @Autowired
    private WifiStatRepository wifiStatRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/wifiStats")
    @ResponseBody
    public ResponseEntity<?> getWifiStats(@PathVariable("spaId") final String spaId, final Pageable pageable) {

        final Page<WifiStat> wifiStats = wifiStatRepository.findBySpaId(spaId, pageable);

        final Resources<WifiStat> resources = new Resources<>(wifiStats);
        resources.add(linkTo(methodOn(WifiStatController.class).getWifiStats(spaId, pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }
}
