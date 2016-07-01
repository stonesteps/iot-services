package com.bwg.iot.model;

/**
 * Carry the mote information that is associated to another component in the system
 */
public class AssociatedSensor {
    private String moteId;
    private String sensorId;

    /**
     * get the moteId, which is just a component id that is of type MOTE
     * @return
     */
    public String getMoteId() {
        return moteId;
    }

    public void setMoteId(String moteId) {
        this.moteId = moteId;
    }

    /**
     * get the sensorId, this is a value that the firmware on the mote/gateway generates and reports.
     * This value is not globally unique in itself, it is only unique per moteId.
     * Mote and/or gateway software will generate or allow for config of the sensorId to each
     * sensor that is plugged into the mote or gateway.
     *
     * @return
     */
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorIdentity) {
        this.sensorId = sensorIdentity;
    }
}
