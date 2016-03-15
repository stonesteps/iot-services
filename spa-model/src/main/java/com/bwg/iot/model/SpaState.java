package com.bwg.iot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by triton on 2/17/16.
 */
public class SpaState {
    String runMode; // rest, ready
    String desiredTemp;
    String targetDesiredTemp;
    String currentTemp;
    boolean filterCycle1Active;
    boolean filterCycle2Active;

    List<ComponentState> components;
    List<Measurement> measurements;

    String uplinkTimestamp = null;

    public SpaState(){
        components = new ArrayList<ComponentState>();
        measurements = new ArrayList<Measurement>();
    }

    public boolean isFilterCycle1Active() {
        return filterCycle1Active;
    }

    public void setFilterCycle1Active(boolean filterCycle1Active) {
        this.filterCycle1Active = filterCycle1Active;
    }

    public boolean isFilterCycle2Active() {
        return filterCycle2Active;
    }

    public void setFilterCycle2Active(boolean filterCycle2Active) {
        this.filterCycle2Active = filterCycle2Active;
    }

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

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
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
    public String toString() {
        return "SpaState{" +
                "  runMode='" + runMode + '\'' +
                ", desiredTemp='" + desiredTemp + '\'' +
                ", targetDesiredTemp='" + targetDesiredTemp + '\'' +
                ", currentTemp='" + currentTemp + '\'' +
                ", components=" + components +
                ", measurements=" + measurements +
                ", uplinkTimestamp='" + uplinkTimestamp + '\'' +
                '}';
    }
}
