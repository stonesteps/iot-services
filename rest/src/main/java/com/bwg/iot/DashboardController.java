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
        if (dashboardInfo == null) {
            return new ResponseEntity<>("No Landing Page for this type of user", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<DashboardInfo>(dashboardInfo, HttpStatus.OK);
    }

    private DashboardInfo getDashboardInfo(User user) {
        Query alertQuery = new Query();
        alertQuery.addCriteria(Criteria.where("clearedDate").is(null));
        Query warningAlertQuery = new Query();
        warningAlertQuery.addCriteria(Criteria.where("clearedDate").is(null));
        Query errorAlertQuery = new Query();
        errorAlertQuery.addCriteria(Criteria.where("clearedDate").is(null));
        Query severeAlertQuery = new Query();
        severeAlertQuery.addCriteria(Criteria.where("clearedDate").is(null));
        Query spaQuery = new Query();
        Query unsoldSpaQuery = new Query();
        Query onlineSpaQuery = new Query();

        String slash = "";

        if (user.hasRole(User.Role.BWG.name())) {
        } else if (user.hasRole(User.Role.OEM.name())) {
            alertQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            warningAlertQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            errorAlertQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            severeAlertQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            spaQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            unsoldSpaQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            onlineSpaQuery.addCriteria(Criteria.where("oemId").is(user.getOemId()));
            slash = "/search/findByOemId?oemId=" + user.getOemId();
        } else if (user.hasRole(User.Role.DEALER.name())) {
            alertQuery.addCriteria(Criteria.where("dealerId").is(user.getDealerId()));
            warningAlertQuery.addCriteria(Criteria.where("dealerId").is(user.getDealerId()));
            errorAlertQuery.addCriteria(Criteria.where("dealerId").is(user.getDealerId()));
            severeAlertQuery.addCriteria(Criteria.where("dealerId").is(user.getDealerId()));
            spaQuery.addCriteria(Criteria.where("dealerId").is(user.getDealerId()));
            unsoldSpaQuery.addCriteria(Criteria.where("dealerId").is(user.getDealerId()));
            onlineSpaQuery.addCriteria(Criteria.where("dealerId").is(user.getDealerId()));
            slash = "/search/findByDealerId?dealerId=" + user.getDealerId();
        } else if (user.hasRole(User.Role.OWNER.name())) {
            alertQuery.addCriteria(Criteria.where("spaId").is(user.getSpaId()));
            warningAlertQuery.addCriteria(Criteria.where("spaId").is(user.getSpaId()));
            errorAlertQuery.addCriteria(Criteria.where("spaId").is(user.getSpaId()));
            severeAlertQuery.addCriteria(Criteria.where("spaId").is(user.getSpaId()));
            spaQuery.addCriteria(Criteria.where("_id").is(user.getSpaId()));
            unsoldSpaQuery.addCriteria(Criteria.where("_id").is(user.getSpaId()));
            onlineSpaQuery.addCriteria(Criteria.where("_id").is(user.getSpaId()));
            slash = "/" + user.getSpaId();
        } else {
            return null;
        }
        warningAlertQuery.addCriteria(Criteria.where("severityLevel").is(Alert.SeverityLevelEnum.WARNING.name()));
        errorAlertQuery.addCriteria(Criteria.where("severityLevel").is(Alert.SeverityLevelEnum.ERROR.name()));
        severeAlertQuery.addCriteria(Criteria.where("severityLevel").is(Alert.SeverityLevelEnum.SEVERE.name()));
        unsoldSpaQuery.addCriteria(Criteria.where("owner").exists(false));
        onlineSpaQuery.addCriteria(Criteria.where("owner").exists(true));

        Date anHourAgo = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        onlineSpaQuery.addCriteria(Criteria.where("currentState.uplinkTimestamp").gt(anHourAgo));

        DashboardInfo dashboardInfo = new DashboardInfo();
        long alertCount = mongoOps.count(alertQuery, Alert.class);
        long warningAlertCount = mongoOps.count(warningAlertQuery, Alert.class);
        long errorAlertCount = mongoOps.count(errorAlertQuery, Alert.class);
        long severeAlertCount = mongoOps.count(severeAlertQuery, Alert.class);
        long spaCount = mongoOps.count(spaQuery, Spa.class);
        long unsoldSpaCount = mongoOps.count(unsoldSpaQuery, Spa.class);
        long onlineSpaCount = mongoOps.count(onlineSpaQuery, Spa.class);

        Map<String, Long> alertCountMap = ImmutableMap.of("totalAlertCount", alertCount, "severeAlertCount", severeAlertCount,
                "errorAlertCount", errorAlertCount, "warningAlertCount", warningAlertCount,
                "infoAlertCount", alertCount-severeAlertCount-errorAlertCount-warningAlertCount);
        Map<String, Long> spaCountMap = ImmutableMap.of("totalSpaCount", spaCount, "soldSpaCount", spaCount-unsoldSpaCount, "onlineSpaCount", onlineSpaCount);
        dashboardInfo.setAlertCounts(alertCountMap);
        dashboardInfo.setSpaCounts(spaCountMap);

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
