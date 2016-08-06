package com.bwg.iot;

import com.bwg.iot.model.Alert;
import com.bwg.iot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

        final Date now = new Date();
        alert.setAcknowledgedUserId(remoteUser.get_id());
        alert.setAcknowledgedDate(now);
        alert.setClearedDate(now);
        alertRepository.save(alert);
        
        updateAlertState(alert);

        return new ResponseEntity<>(alert, HttpStatus.OK);
    }

    private void updateAlertState(final Alert alert) {
        Alert.SeverityLevelEnum highestActiveAlertSeverity = findHighestActiveAlertSeverity(alert.getSpaId());
    }

    private Alert.SeverityLevelEnum findHighestActiveAlertSeverity(final String spaId) {
        Alert.SeverityLevelEnum highestSeverity = null;

        return highestSeverity;
    }
}