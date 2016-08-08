package com.bwg.iot;

import com.bwg.iot.model.Alert;
import com.bwg.iot.model.ComponentState;
import com.bwg.iot.model.Spa;
import com.bwg.iot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

/**
 * Created by holow on 8/5/2016.
 */
@RestController
@RequestMapping("/spas")
public class CustomAlertController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private SpaRepository spaRepository;

    @Autowired
    private ComponentRepository componentRepository;

    @RequestMapping(value = "/{alertId}/clear", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> clearAlert(@RequestHeader(name = "remote_user") final String remoteUserName, @PathVariable final String alertId) {
        final User remoteUser = userRepository.findByUsername(remoteUserName);
        if (remoteUser == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }

        final Alert alert = alertRepository.findOne(alertId);
        if (alert == null) {
            return new ResponseEntity<>("Alert does not exist", HttpStatus.NOT_FOUND);
        }

        alert.setClearedBy(remoteUser.get_id());
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
                final String highestActiveAlertSeverityForComponent = findHighestActiveAlertSeverityForSpaAndComponent(alert.getSpaId(), alert.getComponent());
                for (final ComponentState componentState: spa.getCurrentState().getComponents()) {
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
        Long alertCount = alertRepository.countBySpaIdAndSeverityLevelAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.SEVERE.name());
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.SEVERE.name();

        alertCount = alertRepository.countBySpaIdAndSeverityLevelAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.ERROR.name());
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.ERROR.name();

        alertCount = alertRepository.countBySpaIdAndSeverityLevelAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.WARNING.name());
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.WARNING.name();

        alertCount = alertRepository.countBySpaIdAndSeverityLevelAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.INFO.name());
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.INFO.name();

        return null;
    }

    private String findHighestActiveAlertSeverityForSpaAndComponent(final String spaId, final String component) {
        Long alertCount = alertRepository.countBySpaIdAndSeverityLevelAndComponentAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.SEVERE.name(), component);
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.SEVERE.name();

        alertCount = alertRepository.countBySpaIdAndSeverityLevelAndComponentAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.ERROR.name(), component);
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.ERROR.name();

        alertCount = alertRepository.countBySpaIdAndSeverityLevelAndComponentAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.WARNING.name(), component);
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.WARNING.name();

        alertCount = alertRepository.countBySpaIdAndSeverityLevelAndComponentAndClearedDateIsNull(spaId, Alert.SeverityLevelEnum.INFO.name(), component);
        if (alertCount != null && alertCount.longValue() > 0) return Alert.SeverityLevelEnum.INFO.name();

        return null;
    }
}