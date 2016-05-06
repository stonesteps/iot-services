package com.bwg.iot.model;

public enum SpaRunState {
    INIT(0),
    POWER_UP(1),
    NORMAL(2),
    FLOW(3),
    FROZEN(4),
    COOL(5),
    ICE_JETS(6),
    ICE_HEAT(7),
    SUMMER(8),
    HOT(9),
    SOUTHWEST(10),
    CIRCULATE(11),
    OVERHEAT(12),
    WATCHDOG(13),
    WATCHDOG_PROTECT(14),
    CONFIG_ERROR(15);

    private final int code;

    private SpaRunState(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}