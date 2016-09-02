package com.bwg.iot;

import com.bwg.iot.model.Alert;
import com.bwg.iot.model.ComponentState;
import com.bwg.iot.model.Spa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by holow on 8/5/2016.
 */
@RestController
@RequestMapping("/alerts")
public class CustomAlertController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAlertController.class);

    private final static String ALERT_AGGREGATE_MAP_FUNCTION = "function () { emit(this.severityLevel, 1); }";
    private final static String ALERT_AGGREGATE_REDUCE_FUNCTION = "function (key, values) { var sum = 0; for (var i = 0; i < values.length; i++) sum += values[i]; return sum; }";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private SpaRepository spaRepository;

    @Autowired
    private ComponentRepository componentRepository;

    @RequestMapping(value = "/{alertId}/clear", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> clearAlert(@RequestHeader(name = "remote_user", required = false) final String remoteUser, @PathVariable final String alertId) {
        final Alert alert = alertRepository.findOne(alertId);
        if (alert == null) {
            return new ResponseEntity<>("Alert does not exist", HttpStatus.NOT_FOUND);
        }

        alert.setClearedByUserId(remoteUser);
        alert.setClearedDate(new Date());
        alertRepository.save(alert);

        updateSpaAlertState(alert);

        return new ResponseEntity<>(alert, HttpStatus.OK);
    }

    private void updateSpaAlertState(final Alert alert) {
        final String highestActiveAlertSeverity = findHighestActiveAlertSeverityForSpa(alert.getSpaId());
        final Spa spa = spaRepository.findOne(alert.getSpaId());
        if (spa != null && spa.getCurrentState() != null) {
            boolean modified = false;
            if (!Objects.equals(spa.getCurrentState().getAlertState(), highestActiveAlertSeverity)) {
                spa.getCurrentState().setAlertState(highestActiveAlertSeverity);
                modified = true;
            }
            if (spa.getCurrentState().getComponents() != null && spa.getCurrentState().getComponents().size() > 0) {
                final String highestActiveAlertSeverityForComponent = findHighestActiveAlertSeverityForSpaAndComponentAndPort(alert.getSpaId(), alert.getComponent(), alert.getPortNo());
                for (final ComponentState componentState : spa.getCurrentState().getComponents()) {
                    if (Objects.equals(componentState.getComponentType(), alert.getComponent())) {
                        componentState.setAlertState(highestActiveAlertSeverityForComponent);
                        modified = true;
                    }
                }
            }
            if (modified) spaRepository.save(spa);
        }
    }

    private String findHighestActiveAlertSeverityForSpa(final String spaId) {
        final Query query = Query.query(Criteria.where("spaId").is(spaId)).addCriteria(Criteria.where("clearedDate").is(null));
        final MapReduceResults<ValueObject> results = mongoTemplate.mapReduce(query, "alert", ALERT_AGGREGATE_MAP_FUNCTION, ALERT_AGGREGATE_REDUCE_FUNCTION, ValueObject.class);

        return getHighestAlertSeverity(results);
    }

    private String findHighestActiveAlertSeverityForSpaAndComponentAndPort(final String spaId, final String component, final Integer portNo) {
        final Query query = Query.query(Criteria.where("spaId").is(spaId)).addCriteria(Criteria.where("component").is(component)).addCriteria(Criteria.where("clearedDate").is(null));
        if (portNo != null) query.addCriteria(Criteria.where("portNo").is(portNo));

        final MapReduceResults<ValueObject> results = mongoTemplate.mapReduce(query, "alert", ALERT_AGGREGATE_MAP_FUNCTION, ALERT_AGGREGATE_REDUCE_FUNCTION, ValueObject.class);

        return getHighestAlertSeverity(results);
    }

    private String getHighestAlertSeverity(final MapReduceResults<ValueObject> results) {
        final Map<String, Integer> resultMap = new HashMap<>();
        for (final ValueObject valueObject : results) {
            resultMap.put(valueObject.getId(), valueObject.getValue());
        }

        Integer alertCount = resultMap.get(Alert.SeverityLevelEnum.SEVERE.name());
        if (alertCount != null && alertCount.intValue() > 0) return Alert.SeverityLevelEnum.SEVERE.name();

        alertCount = resultMap.get(Alert.SeverityLevelEnum.ERROR.name());
        if (alertCount != null && alertCount.intValue() > 0) return Alert.SeverityLevelEnum.ERROR.name();

        alertCount = resultMap.get(Alert.SeverityLevelEnum.WARNING.name());
        if (alertCount != null && alertCount.intValue() > 0) return Alert.SeverityLevelEnum.WARNING.name();

        alertCount = resultMap.get(Alert.SeverityLevelEnum.INFO.name());
        if (alertCount != null && alertCount.intValue() > 0) return Alert.SeverityLevelEnum.INFO.name();

        return Alert.SeverityLevelEnum.NONE.name();
    }

    /**
     * Class used just for aggregate map reduce results.
     */
    private static final class ValueObject {
        private String id;
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}