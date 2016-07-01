package com.bwg.iot.model;

import org.springframework.hateoas.ResourceSupport;

/**
 * Carry the mote and sensor that is associated to another component in the system such as a PUMP.
 * This tracks the associations intentionally made by a person on a per-spa instance. These associations should only
 * be created/updated by actions of a person saying 'this sensor belongs to this spa component'.
 *
 * The associations can only be known at spa assembly/planning/instantiation time, when
 * mote/sensors are planned for attachment to other specific spa components.
 */
public class AssociatedSensor extends ResourceSupport {
    private String moteId;
    private String sensorId;

    /**
     * Default Constructor
     */
    public AssociatedSensor() {}

    /**
     * Constructor
     *
     * @param moteId
     * @param sensorId
     */
    public AssociatedSensor(String moteId, String sensorId) {
        this.moteId = moteId;
        this.sensorId = sensorId;
    }

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
     * The spa planner would obtain the sensorId from the packaging for the IOT current sensor mote product.
     * For example, a current measuring mote, would come with a label that states the sensorId value
     * that the mote will report for each sensor slot on the mote has a live sensor plugged into it.
     *
     * This allows for a single mote to have multiple sensors of even the same type, as long as each sensor
     * is given a unique sensorId value they can be uniquely tracked and assigned later.
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
