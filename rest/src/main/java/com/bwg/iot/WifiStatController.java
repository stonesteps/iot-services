package com.bwg.iot;

import com.bwg.iot.model.WifiStat;
import org.jboss.seam.annotations.intercept.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class WifiStatController {

    @Autowired
    private WifiStatRepository wifiStatRepository;

    @Autowired
    private PagedResourcesAssembler<WifiStat> pagedAssembler;

    @RequestMapping(method = RequestMethod.GET, value = "/spas/{spaId}/wifiStats")
    @ResponseBody
    public HttpEntity<PagedResources<Resource<WifiStat>>> getWifiStats(@PathVariable("spaId") final String spaId,
                                                                       final Pageable pageable,
                                                                       PersistentEntityResourceAssembler entityAssembler) {
        pagedAssembler.setForceFirstAndLastRels(true);
        final Page<WifiStat> wifiStats = wifiStatRepository.findBySpaId(spaId, pageable);
        return ResponseEntity.ok(pagedAssembler.toResource(wifiStats, (ResourceAssembler)entityAssembler));
    }
}
