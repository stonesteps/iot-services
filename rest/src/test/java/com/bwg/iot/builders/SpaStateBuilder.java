package com.bwg.iot.builders;

import com.bwg.iot.model.ComponentState;
import com.bwg.iot.model.Measurement;
import com.bwg.iot.model.SpaState;

import java.util.Date;

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
    public SpaStateBuilder uplinkTimestamp(Date timestamp){
        spaState.setUplinkTimestamp(timestamp);
        return this;
    }
    public SpaStateBuilder targetDesiredTemp(String temp) {
        spaState.setTargetDesiredTemp(temp);
        return this;
    }
    public SpaStateBuilder desiredTemp(String temp) {
        spaState.setDesiredTemp(temp);
        return this;
    }
    public SpaStateBuilder currentTemp(String temp) {
        spaState.setCurrentTemp(temp);
        return this;
    }
    public SpaStateBuilder runMode(String mode) {
        spaState.setRunMode(mode);
        return this;
    }
}
