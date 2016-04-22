package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.DashboardInfo;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RepositoryRestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final AlertRepository alertRepository;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    public DashboardController(AlertRepository repo){
        alertRepository = repo;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public @ResponseBody ResponseEntity<?> getDealerDashboardInfo(@Param("role") String role, @Param("dealerId") String dealerId) {
        // test stub
        DashboardInfo dashboardInfo = getDummyInfo();

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Alert.class)
                .slash("/search/findByDealerId?dealerId=" + dealerId)
                .withRel("alertList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/search/findByDealerId?dealerId=" + dealerId)
                .withRel("spaList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.User.class)
                .slash("/search/findByDealerId?dealerId=" + dealerId)
                .withRel("ownerList"));

        //TODO: replace when message entity implemented
        dashboardInfo.add(new Link("http:/localhost:8080/messages/search/findByDealerId?dealerId=" + dealerId)
                .withRel("messageList"));

        dashboardInfo.add(linkTo(DashboardController.class)
                .slash("/dealer?dealerId=" + dealerId)
                .withSelfRel());

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dealer")
    public @ResponseBody ResponseEntity<?> getDealerDashboardInfo(@Param("dealerId") String dealerId) {
        // test stub
        DashboardInfo dashboardInfo = getDummyInfo();

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Alert.class)
                .slash("/search/findByDealerId?dealerId=" + dealerId)
                .withRel("alertList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/search/findByDealerId?dealerId=" + dealerId)
                .withRel("spaList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.User.class)
                .slash("/search/findByDealerId?dealerId=" + dealerId)
                .withRel("ownerList"));

        //TODO: replace when message entity implemented
        dashboardInfo.add(new Link("http:/localhost:8080/messages/search/findByDealerId?dealerId=" + dealerId)
                .withRel("messageList"));

        dashboardInfo.add(linkTo(DashboardController.class)
                .slash("/dealer?dealerId=" + dealerId)
                .withSelfRel());

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/oem")
    public @ResponseBody ResponseEntity<?> getOemOemInfo(@Param("oemId") String oemId) {
        // test stub
        DashboardInfo dashboardInfo = getDummyInfo();

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Alert.class)
                .slash("/search/findByOemId?oemId=" + oemId)
                .withRel("alertList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/search/findByOemId?oemId=" + oemId)
                .withRel("spaList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.User.class)
                .slash("/search/findByOemId?oemId=" + oemId)
                .withRel("ownerList"));

        //TODO: replace when message entity implemented
        dashboardInfo.add(new Link("http:/localhost:8080/messages/search/findByOemId?oemId=" + oemId)
                .withRel("messageList"));

        dashboardInfo.add(linkTo(DashboardController.class)
                .slash("/oem?oemId=" + oemId)
                .withSelfRel());

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/associate")
    public @ResponseBody ResponseEntity<?> getAssociateDashboardInfo(@Param("associateId") String associateId) {
        // test stub
        DashboardInfo dashboardInfo = getDummyInfo();

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Alert.class)
                .slash("/search/findByAssociateId?associateId=" + associateId)
                .withRel("alertList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/search/findByAssociateId?associateId=" + associateId)
                .withRel("spaList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.User.class)
                .slash("/search/findByAssociateId?associateId=" + associateId)
                .withRel("ownerList"));

        //TODO: replace when message entity implemented
        dashboardInfo.add(new Link("http:/localhost:8080/messages/search/findByAssociateId?associateId=" + associateId)
                .withRel("messageList"));

        dashboardInfo.add(linkTo(DashboardController.class)
                .slash("/associate?associateId=" + associateId)
                .withSelfRel());

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/technician")
    public @ResponseBody ResponseEntity<?> getTechnicianDashboardInfo(@Param("technicianId") String technicianId) {
        // test stub
        DashboardInfo dashboardInfo = getDummyInfo();

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Alert.class)
                .slash("/search/findByTechnicianId?technicianId=" + technicianId)
                .withRel("alertList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/search/findByTechnicianId?technicianId=" + technicianId)
                .withRel("spaList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.User.class)
                .slash("/search/findByTechnicianId?technicianId=" + technicianId)
                .withRel("ownerList"));

        //TODO: replace when message entity implemented
        dashboardInfo.add(new Link("http:/localhost:8080/messages/search/findByTechnicianId?technicianId=" + technicianId)
                .withRel("messageList"));

        dashboardInfo.add(linkTo(DashboardController.class)
                .slash("/technician?technicianId=" + technicianId)
                .withSelfRel());

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/bwg")
    public @ResponseBody ResponseEntity<?> getBwgDashboardInfo() {
        // test stub
        DashboardInfo dashboardInfo = getDummyInfo();

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Alert.class)
                .withRel("alertList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .withRel("spaList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.User.class)
                .withRel("ownerList"));

        //TODO: replace when message entity implemented
        dashboardInfo.add(new Link("http:/localhost:8080/messages/")
                .withRel("messageList"));

        dashboardInfo.add(linkTo(DashboardController.class)
                .slash("/bwg")
                .withSelfRel());

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    private DashboardInfo getDummyInfo() {
        DashboardInfo dashboardInfo = new DashboardInfo();
        Map<String, Integer> alertCountMap = ImmutableMap.of("totalAlertCount", 42, "redAlertCount", 20, "yellowAlertCount", 22);
        Map<String, Integer> spaCountMap = ImmutableMap.of("totalSpaCount", 720, "soldSpaCount", 100, "onlineSpaCount", 67);
        Map<String, Integer> messageCountMap = ImmutableMap.of("totalMessageCount", 20, "newMessageCount", 5);
        dashboardInfo.setAlertCounts(alertCountMap);
        dashboardInfo.setSpaCounts(spaCountMap);
        dashboardInfo.setMessageCounts(messageCountMap);
        return dashboardInfo;
    }


    private DashboardInfo getCounts(String queryField, String queryValue){
        DashboardInfo dashboardInfo = new DashboardInfo();

       // TODO: implement

       return dashboardInfo;
    }

}
