package com.bwg.iot.model;

/**
 * Used in commands and in spa state representation.
 */
public enum PumpState {
    OFF(0), LOW(1), HIGH(3);

    private final int code;

    private PumpState(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
