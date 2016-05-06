package com.bwg.iot.model;

/**
 * pojo for controller dip switches
 *
 */
public class DipSwitch {
    int slotNumber;
    boolean on;

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isOn() {
        return on;
    }

    public DipSwitch(int slotNumber, boolean on) {
        this.slotNumber = slotNumber;
        this.on = on;
    }
}
