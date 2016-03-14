package com.bwg.iot.model.util;

import com.bwg.iot.model.*;
import org.apache.commons.lang3.EnumUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for spa requests (commands and mqtt requests).
 */
public final class SpaRequestUtil {

    private static final StateValidator LIGHT_STATE_VALIDATOR = new LightStateValidator();
    private static final StateValidator PUMP_STATE_VALIDATOR = new PumpStateValidator();
    private static final StateValidator BLOWER_STATE_VALIDATOR = new BlowerStateValidator();
    private static final StateValidator ON_OFF_STATE_VALIDATOR = new OnOffStateValidator();

    private static final Map<Integer, Range> codeRangeMap = new HashMap<>();

    static {
        codeRangeMap.put(SpaCommand.RequestType.PUMPS.getCode(), new Range(0, 7));
        codeRangeMap.put(SpaCommand.RequestType.LIGHTS.getCode(), new Range(0, 3));
        codeRangeMap.put(SpaCommand.RequestType.BLOWER.getCode(), new Range(0, 1));
        codeRangeMap.put(SpaCommand.RequestType.MISTER.getCode(), new Range(0, 2));
        codeRangeMap.put(SpaCommand.RequestType.AUX.getCode(), new Range(0, 3));
    }

    private static final Map<Integer, StateValidator> stateValidatorMap = new HashMap<>();

    static {
        stateValidatorMap.put(SpaCommand.RequestType.PUMPS.getCode(), PUMP_STATE_VALIDATOR);
        stateValidatorMap.put(SpaCommand.RequestType.LIGHTS.getCode(), LIGHT_STATE_VALIDATOR);
        stateValidatorMap.put(SpaCommand.RequestType.BLOWER.getCode(), BLOWER_STATE_VALIDATOR);
        stateValidatorMap.put(SpaCommand.RequestType.MISTER.getCode(), ON_OFF_STATE_VALIDATOR);
        stateValidatorMap.put(SpaCommand.RequestType.FILTER.getCode(), ON_OFF_STATE_VALIDATOR);
        stateValidatorMap.put(SpaCommand.RequestType.OZONE.getCode(), ON_OFF_STATE_VALIDATOR);
        stateValidatorMap.put(SpaCommand.RequestType.MICROSILK.getCode(), ON_OFF_STATE_VALIDATOR);
        stateValidatorMap.put(SpaCommand.RequestType.AUX.getCode(), ON_OFF_STATE_VALIDATOR);
    }

    private SpaRequestUtil() {
        // utility class
    }

    public static boolean portRequired(final int requestType) {
        return codeRangeMap.get(requestType) != null;
    }

    public static boolean validPort(final int requestType, final int port) {
        boolean valid = false;

        final Range range = codeRangeMap.get(requestType);
        if (range == null) {
            // means no port needed
            valid = true;
        } else {
            valid = range.inRange(port);
        }

        return valid;
    }

    public static boolean validState(final int requestType, final String state) {
        boolean valid = false;

        final StateValidator stateValidator = stateValidatorMap.get(requestType);
        if (stateValidator != null) {
            valid = stateValidator.validState(state);
        }

        return valid;
    }


    private static final class Range {
        final int min;
        final int max;

        public Range(final int min, final int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public boolean inRange(final int value) {
            return value >= min && value <= max;
        }
    }

    private static final class LightStateValidator implements StateValidator {
        @Override
        public boolean validState(String state) {
            return EnumUtils.isValidEnum(LightState.class, state);
        }
    }

    private static final class BlowerStateValidator implements StateValidator {
        @Override
        public boolean validState(String state) {
            return EnumUtils.isValidEnum(BlowerState.class, state);
        }
    }

    private static final class PumpStateValidator implements StateValidator {
        @Override
        public boolean validState(String state) {
            return EnumUtils.isValidEnum(PumpState.class, state);
        }
    }

    private static final class OnOffStateValidator implements StateValidator {
        @Override
        public boolean validState(String state) {
            return EnumUtils.isValidEnum(OnOffState.class, state);
        }
    }
}
