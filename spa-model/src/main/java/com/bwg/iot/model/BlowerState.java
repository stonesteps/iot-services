package com.bwg.iot.model;

/**
 * Used in commands and in spa state representation.
 */
public enum BlowerState {
    OFF(0), LOW(1), MED(2), HIGH(3);

    private final int code;

    private BlowerState(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
