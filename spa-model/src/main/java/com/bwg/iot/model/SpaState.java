package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by triton on 2/17/16.
 */
public class SpaState {

    @Id
    String _id;
    String spaId;
    String runMode = null; // rest, ready
    String desiredTemp = null;
    String targetDesiredTemp;

    List<ComponentState> components;
    List<Measurement> measurements;

    String uplinkTimestamp = null;

    public SpaState(){
        components = new ArrayList<ComponentState>();
        measurements = new ArrayList<Measurement>();
    };

    public String getRunMode() {
        return runMode;
    }

    public void setRunMode(String runMode) {
        this.runMode = runMode;
    }

    public String getDesiredTemp() {
        return desiredTemp;
    }

    public void setDesiredTemp(String desiredTemp) {
        this.desiredTemp = desiredTemp;
    }

    public String getTargetDesiredTemp() {
        return targetDesiredTemp;
    }

    public void setTargetDesiredTemp(String targetDesiredTemp) {
        this.targetDesiredTemp = targetDesiredTemp;
    }

    public List<ComponentState> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentState> components) {
        this.components = components;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public String getUplinkTimestamp() {
        return uplinkTimestamp;
    }

    public void setUplinkTimestamp(String uplinkTimestamp) {
        this.uplinkTimestamp = uplinkTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaState spaState = (SpaState) o;

        if (spaId != null ? !spaId.equals(spaState.spaId) : spaState.spaId != null) return false;
        return uplinkTimestamp != null ? uplinkTimestamp.equals(spaState.uplinkTimestamp) : spaState.uplinkTimestamp == null;

    }

    @Override
    public int hashCode() {
        int result = spaId != null ? spaId.hashCode() : 0;
        result = 31 * result + (uplinkTimestamp != null ? uplinkTimestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpaState{" +
                "_id='" + _id + '\'' +
                ", spaId='" + spaId + '\'' +
                ", runMode='" + runMode + '\'' +
                ", desiredTemp='" + desiredTemp + '\'' +
                ", targetDesiredTemp='" + targetDesiredTemp + '\'' +
                ", components=" + components +
                ", measurements=" + measurements +
                ", uplinkTimestamp='" + uplinkTimestamp + '\'' +
                '}';
    }
}
