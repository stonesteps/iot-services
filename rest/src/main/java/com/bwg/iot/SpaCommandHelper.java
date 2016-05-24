package com.bwg.iot;

import com.bwg.iot.model.SpaCommand;
import com.bwg.iot.model.util.SpaRequestUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SpaCommandHelper  {

    @Autowired
    private  SpaCommandRepository spaCommandRepository;

    public SpaCommand setButtonCommand(String spaId, HashMap<String, String> body, int requestCode, boolean save) throws ValidationException {
        if (spaId == null) {
            throw new ValidationException("Spa Id not provided");
        }

        String desiredState = body.get("desiredState");
        if (desiredState == null || !SpaRequestUtil.validState(requestCode, desiredState)) {
            throw new ValidationException("Desired State Invalid");
        }

        String deviceNumber = body.get("deviceNumber");
        if (deviceNumber == null && SpaRequestUtil.portRequired(requestCode)) {
            throw new ValidationException("Device Number Required");
        }

        if (deviceNumber != null && SpaRequestUtil.portRequired(requestCode) && !NumberUtils.isNumber(deviceNumber)) {
            throw new ValidationException("Device Number Invalid");
        }

        if (deviceNumber != null && SpaRequestUtil.portRequired(requestCode) && !SpaRequestUtil.validPort(requestCode, NumberUtils.createInteger(deviceNumber))) {
            throw new ValidationException("Device Number Invalid, out of range");
        }

        HashMap<String, String> values = new HashMap<>();
        values.put("DESIREDSTATE", desiredState);
        if (deviceNumber != null) {
            values.put("PORT", deviceNumber);
        }

        String originatorId = body.get("originatorId");
        SpaCommand command = buildCommand(spaId, originatorId, requestCode, values, save);
        return command;
    }

    public SpaCommand setDesiredTemp(String spaId, HashMap<String, String> body, int requestCode, boolean save) throws ValidationException {
        if (spaId == null) {
            throw new ValidationException("Spa Id not provided");
        }

        String desiredTemp = body.get("desiredTemp");
        if (desiredTemp == null) {
            throw new ValidationException("Desired Temperature not provided");
        }
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("DESIREDTEMP", desiredTemp);

        String originatorId = body.get("originatorId");
        SpaCommand command = buildCommand(spaId, originatorId, SpaCommand.RequestType.HEATER.getCode(), values, save);
        return command;
    }
    
    public SpaCommand setFilerCycleIntervals(String spaId, HashMap<String, String> body, int requestCode, boolean save) throws ValidationException {
        if (spaId == null) {
            throw new ValidationException("Spa Id not provided");
        }

        final HashMap<String, String> values = new HashMap<String, String>();

        final String deviceNumber = body.get("deviceNumber");
        if (deviceNumber == null) {
            throw new ValidationException("Device Number Required");
        }
        if (deviceNumber != null && !NumberUtils.isNumber(deviceNumber)) {
            throw new ValidationException("Device Number Invalid");
        }
        values.put("PORT", deviceNumber);

        final String intervalNumber = body.get("intervalNumber");
        if (intervalNumber == null) {
            throw new ValidationException("Interval Number not provided");
        }
        if (!NumberUtils.isNumber(intervalNumber)) {
            throw new ValidationException("Interval Number is not a number");
        }
        values.put("FILTER_DURATION_15MINUTE_INTERVALS", intervalNumber);

        final String originatorId = body.get("originatorId");
        SpaCommand command = buildCommand(spaId, originatorId, SpaCommand.RequestType.FILTER.getCode(), values, save);
        return command;
    }


        public SpaCommand buildCommand(String spaId, String originatorId, int requestCode, HashMap<String, String> values, boolean save) {
        SpaCommand command = new SpaCommand();
        command.setSpaId(spaId);
        command.setRequestTypeId(requestCode);
        command.setValues(values);

        if (save) {
            if (originatorId == null) {
                originatorId = UUID.randomUUID().toString();
            }
            command.setOriginatorId(originatorId);
            command.setSentTimestamp(new Date());
            command = spaCommandRepository.save(command);
        }

        return command;
    }

}
