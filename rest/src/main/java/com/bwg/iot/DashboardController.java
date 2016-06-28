package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.Alert;
import com.bwg.iot.model.DashboardInfo;
import com.bwg.iot.model.Spa;
import com.bwg.iot.model.User;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RepositoryRestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final AlertRepository alertRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoOperations mongoOps;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    public DashboardController(AlertRepository repo){
        alertRepository = repo;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public @ResponseBody ResponseEntity<?> getDealerDashboardInfo(@RequestHeader(name="remote_user")String remote_user) {
        User remoteUser = userRepository.findByUsername(remote_user);
        if (remoteUser == null) {
            return new ResponseEntity<String>("User does not exist", HttpStatus.NOT_FOUND);
        }

        DashboardInfo dashboardInfo = getDashboardInfo(remoteUser);

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    private DashboardInfo getDashboardInfo(User user) {
        Query alertQuery = new Query();
        Query redAlertQuery = new Query();
        Query spaQuery = new Query();
        Query unsoldSpaQuery = new Query();
        Query onlineSpaQuery = new Query();

        String slash = "";

        if (user.hasRole(User.Role.OEM.name())) {
            alertQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            redAlertQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            spaQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            unsoldSpaQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            onlineSpaQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            slash = "/search/findByOemId?oemId=" + user.getOemId();
        } else if (user.hasRole(User.Role.DEALER.name())) {
            alertQuery.addCriteria(Criteria.where("dealerId").is(user.getOemId()));
            redAlertQuery.addCriteria(Criteria.where("dealerId").is(user.getOemId()));
            spaQuery.addCriteria(Criteria.where("dealerId").is(user.getOemId()));
            unsoldSpaQuery.addCriteria(Criteria.where("dealerId").is(user.getOemId()));
            onlineSpaQuery.addCriteria(Criteria.where("dealerId").is(user.getOemId()));
            slash = "/search/findByDealerId?dealerId=" + user.getDealerId();
        }
        redAlertQuery.addCriteria(Criteria.where("severityLevel").is(Alert.SeverityLevelEnum.red.name()));
        unsoldSpaQuery.addCriteria(Criteria.where("owner").exists(false));
        onlineSpaQuery.addCriteria(Criteria.where("owner").exists(true));

        Date anHourAgo = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        onlineSpaQuery.addCriteria(Criteria.where("currentState.uplinkTimestamp").gt(anHourAgo));


        DashboardInfo dashboardInfo = new DashboardInfo();
        long alertCount = mongoOps.count(alertQuery, Alert.class);
        long redAlertCount = mongoOps.count(redAlertQuery, Alert.class);
        long spaCount = mongoOps.count(spaQuery, Spa.class);
        long unsoldSpaCount = mongoOps.count(unsoldSpaQuery, Spa.class);
        long onlineSpaCount = mongoOps.count(onlineSpaQuery, Spa.class);

        Map<String, Long> alertCountMap = ImmutableMap.of("totalAlertCount", alertCount, "redAlertCount", redAlertCount, "yellowAlertCount", alertCount-redAlertCount);
        Map<String, Long> spaCountMap = ImmutableMap.of("totalSpaCount", spaCount, "soldSpaCount", spaCount-unsoldSpaCount, "onlineSpaCount", onlineSpaCount);
        dashboardInfo.setAlertCounts(alertCountMap);
        dashboardInfo.setSpaCounts(spaCountMap);

        // support deprecated fields until UI changes
        Map<String, Long> messageCountMap = ImmutableMap.of("totalMessageCount", Long.valueOf(0), "newMessageCount", Long.valueOf(0));
        dashboardInfo.setMessageCounts(messageCountMap);

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Alert.class)
                .slash(slash)
                .withRel("alertList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash(slash)
                .withRel("spaList"));

        dashboardInfo.add(entityLinks.linkFor(com.bwg.iot.model.User.class)
                .slash(slash)
                .withRel("ownerList"));

        dashboardInfo.add(linkTo(DashboardController.class)
                .withSelfRel());

        return dashboardInfo;
    }


}
