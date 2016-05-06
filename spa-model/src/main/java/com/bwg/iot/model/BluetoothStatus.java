package com.bwg.iot.model;

public enum BluetoothStatus {
    NOT_PRESENT(0),
    OFF(1),
    POWERING_UP(2),
    DISCOVERABLE(3),
    CONNECTED(4),
    LINEIN_MODE(5),
    MODE_CHANGING(6),
    POWERING_DOWN(7),
    IDLE(8),
    AMPLIFIER_COMMS_LOST(15);

    private final int code;

    private BluetoothStatus(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
