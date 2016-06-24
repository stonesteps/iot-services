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

        String desiredState = body.get(SpaCommand.REQUEST_DESIRED_STATE);
        if (desiredState == null || !SpaRequestUtil.validState(requestCode, desiredState)) {
            throw new ValidationException("Desired State Invalid");
        }

        String deviceNumber = body.get(SpaCommand.REQUEST_DEVICE_NUMBER);
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
        values.put(SpaCommand.REQUESTED_BY, body.get(SpaCommand.REQUESTED_BY));
        values.put(SpaCommand.REQUEST_PATH, body.get(SpaCommand.REQUEST_PATH));
        values.put(SpaCommand.COMMAND_DESIRED_STATE, desiredState);
        if (deviceNumber != null) {
            values.put(SpaCommand.COMMAND_DEVICE_NUMBER, deviceNumber);
        }

        String originatorId = body.get(SpaCommand.REQUEST_ORIGINATOR);
        SpaCommand command = buildCommand(spaId, originatorId, requestCode, values, save);
        return command;
    }

    public SpaCommand setDesiredTemp(String spaId, HashMap<String, String> body, int requestCode, boolean save) throws ValidationException {
        if (spaId == null) {
            throw new ValidationException("Spa Id not provided");
        }

        String desiredTemp = body.get(SpaCommand.REQUEST_DESIRED_TEMP);
        if (desiredTemp == null) {
            throw new ValidationException("Desired Temperature not provided");
        }
        HashMap<String, String> values = new HashMap<String, String>();
        values.put(SpaCommand.COMMAND_DESIRED_TEMP, desiredTemp);

        String originatorId = body.get(SpaCommand.REQUEST_ORIGINATOR);
        SpaCommand command = buildCommand(spaId, originatorId, SpaCommand.RequestType.HEATER.getCode(), values, save);
        return command;
    }
    
    public SpaCommand setFilerCycleIntervals(String spaId, HashMap<String, String> body, int requestCode, boolean save) throws ValidationException {
        if (spaId == null) {
            throw new ValidationException("Spa Id not provided");
        }

        final HashMap<String, String> values = new HashMap<String, String>();

        final String deviceNumber = body.get(SpaCommand.REQUEST_DEVICE_NUMBER);
        if (deviceNumber == null) {
            throw new ValidationException("Device Number Required");
        }
        if (deviceNumber != null && !NumberUtils.isNumber(deviceNumber)) {
            throw new ValidationException("Device Number Invalid");
        }
        values.put(SpaCommand.COMMAND_DEVICE_NUMBER, deviceNumber);

        final String intervalNumber = body.get(SpaCommand.REQUEST_FILTER_INTERVAL);
        if (intervalNumber == null) {
            throw new ValidationException("Interval Number not provided");
        }
        if (!NumberUtils.isNumber(intervalNumber)) {
            throw new ValidationException("Interval Number is not a number");
        }
        values.put(SpaCommand.COMMAND_FILTER_INTERVAL, intervalNumber);

        final String originatorId = body.get(SpaCommand.REQUEST_ORIGINATOR);
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
            command = spaCommandRepository.insert(command);
        }

        return command;
    }

}
