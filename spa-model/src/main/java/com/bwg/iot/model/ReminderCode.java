package com.bwg.iot.model;

public enum ReminderCode {
    FILTER1(1),
    WATER_REFRESH(2),
    CLEARRAY(3),
    FILTER2(21);

    private final int code;

    private ReminderCode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}