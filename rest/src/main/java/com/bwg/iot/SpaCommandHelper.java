package com.bwg.iot;

import com.bwg.iot.model.Spa;
import com.bwg.iot.model.SpaCommand;
import com.bwg.iot.model.util.SpaRequestUtil;
import com.bwg.iot.util.DateTimeUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Component
public class SpaCommandHelper {

    @Autowired
    private SpaCommandRepository spaCommandRepository;

    @Autowired
    private SpaRepository spaRepository;

    public SpaCommand setButtonCommand(String spaId, HashMap<String, String> body, int requestCode, HashMap<String, String> metadata, boolean save) throws ValidationException {
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
        values.put(SpaCommand.COMMAND_DESIRED_STATE, desiredState);
        if (deviceNumber != null) {
            values.put(SpaCommand.COMMAND_DEVICE_NUMBER, deviceNumber);
        }

        String originatorId = body.get(SpaCommand.REQUEST_ORIGINATOR);
        SpaCommand command = buildCommand(spaId, originatorId, requestCode, values, metadata, save);
        return command;
    }

    public SpaCommand setDesiredTemp(String spaId, HashMap<String, String> body, int requestCode, HashMap<String, String> metadata, boolean save) throws ValidationException {
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
        SpaCommand command = buildCommand(spaId, originatorId, SpaCommand.RequestType.HEATER.getCode(), values, metadata, save);
        return command;
    }

    public SpaCommand setFilerCycleIntervals(String spaId, HashMap<String, String> body, int requestCode, HashMap<String, String> metadata, boolean save) throws ValidationException {
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
        SpaCommand command = buildCommand(spaId, originatorId, SpaCommand.RequestType.FILTER.getCode(), values, metadata, save);
        return command;
    }

    public SpaCommand setTime(String spaId, HashMap<String, String> body, HashMap<String, String> metadata, boolean save) throws ValidationException {
        if (spaId == null) {
            throw new ValidationException("Spa Id not provided");
        }

        final HashMap<String, String> values = new HashMap<String, String>();

        final String time = body.get(SpaCommand.REQUEST_TIME);
        if (time == null) {
            throw new ValidationException("Time required");
        }
        try {
            final Map<Integer, Integer> parsedTime = DateTimeUtil.parseTime(time);
            values.put(SpaCommand.COMMAND_TIME_HOUR, parsedTime.get(Calendar.HOUR_OF_DAY).toString());
            values.put(SpaCommand.COMMAND_TIME_MINUTE, parsedTime.get(Calendar.MINUTE).toString());
            values.put(SpaCommand.COMMAND_TIME_SECOND, parsedTime.get(Calendar.SECOND).toString());
        } catch (final ParseException e) {
            throw new ValidationException("Time format invalid");
        }

        if (isJacuzzi(spaId)) {
            // date obligatory only for JACUZZI
            final String date = body.get(SpaCommand.REQUEST_DATE);
            if (date == null) {
                throw new ValidationException("Date required");
            }
            try {
                final Map<Integer, Integer> parsedDate = DateTimeUtil.parseDate(date);
                values.put(SpaCommand.COMMAND_DATE_DAY, parsedDate.get(Calendar.DAY_OF_MONTH).toString());
                values.put(SpaCommand.COMMAND_DATE_MONTH, parsedDate.get(Calendar.MONTH).toString());
                values.put(SpaCommand.COMMAND_DATE_YEAR, parsedDate.get(Calendar.YEAR).toString());
            } catch (final ParseException e) {
                throw new ValidationException("Date format invalid");
            }
        }

        final String originatorId = body.get(SpaCommand.REQUEST_ORIGINATOR);
        SpaCommand command = buildCommand(spaId, originatorId, SpaCommand.RequestType.SET_TIME.getCode(), values, metadata, save);
        return command;
    }

    private boolean isJacuzzi(final String spaId) {
        final Spa spa = spaRepository.findOne(spaId);
        return spa != null && spa.getCurrentState() != null && spa.getCurrentState().getControllerType().equals("JACUZZI");
    }

    public SpaCommand buildCommand(String spaId, String originatorId, int requestCode, HashMap<String, String> values, HashMap<String, String> metadata, boolean save) {
        SpaCommand command = new SpaCommand();
        command.setSpaId(spaId);
        command.setRequestTypeId(requestCode);
        command.setValues(values);
        command.setMetadata(metadata);

        if (save) {
            command.setOriginatorId(UUID.randomUUID().toString());
            command.setSentTimestamp(new Date());
            command = spaCommandRepository.insert(command);
        }

        return command;
    }

}
