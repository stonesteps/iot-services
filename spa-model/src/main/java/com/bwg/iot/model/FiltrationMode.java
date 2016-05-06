package com.bwg.iot.model;

public enum FiltrationMode {
    AWAY(0),
    LIGHT(1),
    HEAVY(2);

    private final int code;

    private FiltrationMode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
