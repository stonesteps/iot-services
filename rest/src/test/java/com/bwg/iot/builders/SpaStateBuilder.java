package com.bwg.iot.builders;

import com.bwg.iot.model.Component;
import com.bwg.iot.model.ComponentState;
import com.bwg.iot.model.Measurement;
import com.bwg.iot.model.SpaState;

/**
 * Created by triton on 3/3/16.
 */
public class SpaStateBuilder {
    protected SpaState spaState;

    public SpaStateBuilder(){
        spaState = new SpaState();
    }
    public SpaState build(){
        return spaState;
    }

    public SpaStateBuilder component(ComponentState cs){
        spaState.getComponents().add(cs);
        return this;
    }
    public SpaStateBuilder measurement(Measurement measurement){
        spaState.getMeasurements().add(measurement);
        return this;
    }
    public SpaStateBuilder uplinkTimestamp(String timestamp){
        spaState.setUplinkTimestamp(timestamp);
        return this;
    }
    public SpaStateBuilder runMode(String mode) {
        spaState.setRunMode(mode);
        return this;
    }
}
