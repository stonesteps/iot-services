package com.bwg.iot.model;

/**
 * Used in commands and in spa state representation.
 */
public enum HeaterState {
    OFF(0), ON(1), WAITING(2), UNDEFINED(3);

    private final int code;

    private HeaterState(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
