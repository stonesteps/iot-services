package com.bwg.iot.model;

import org.springframework.hateoas.ResourceSupport;

/**
 * Carry the sensor mongodb id that is associated to another component in the system such as a PUMP.
 * This tracks the associations intentionally made by a person on a per-spa instance. These associations should only
 * be created/updated by actions of a person saying 'this sensor belongs to this spa component'.
 *
 * The associations can only be known at spa assembly/planning/instantiation time, when
 * mote/sensors are planned for attachment to other specific spa components.
 *
 * A mote component can optionally be created as a component first, then a sensor component must be created that either contained
 * by a gateway or mote component by setting its Component.parentComponentId to that of moteId or gatewayId
 */
public class AssociatedSensor extends ResourceSupport {
    private String sensorId;

    /**
     * Default Constructor
     */
    public AssociatedSensor() {}

    /**
     * Constructor
     *
     * @param sensorId
     */
    public AssociatedSensor(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * get the sensor id, this is the mongodb id for the sensor component
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
