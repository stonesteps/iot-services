package com.bwg.iot.model;

public enum HeaterMode {
        REST(1),
        READY(2),
        AUTO(0x10),
        ECONOMY(0x11),
        DAY(0x12);

    private final int code;

    private HeaterMode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
