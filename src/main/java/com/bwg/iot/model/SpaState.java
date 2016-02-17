package com.bwg.iot.model;

import java.util.Date;

/**
 * Created by triton on 2/17/16.
 */
public class SpaState {
    String runMode = null; // rest, ready
    String currentTemp = null;  // units?  C or F, type BigDecimal??
    String desiredTemp = null;
    String alert = null;  // green, yellow, red?
    String lights1 = null; // off, low, med, hi
    String lights2 = null;
    String pump1 = null; // (on, off) or amps?
    String pump2 = null; // on, off
    String filter1 = null;
    String filter2 = null;
    String aux1 = null; //on, off;
    String panelLock = null; // on, off;
    String microsillk = null;
    String ozone = null;
    Date uplinkTimestamp = null;

    public SpaState(){};

    public String getRunMode() {
        return runMode;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getDesiredTemp() {
        return desiredTemp;
    }

    public void setDesiredTemp(String desiredTemp) {
        this.desiredTemp = desiredTemp;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getLights1() {
        return lights1;
    }

    public void setLights1(String lights1) {
        this.lights1 = lights1;
    }

    public String getLights2() {
        return lights2;
    }

    public void setLights2(String lights2) {
        this.lights2 = lights2;
    }

    public String getPump1() {
        return pump1;
    }

    public void setPump1(String pump1) {
        this.pump1 = pump1;
    }

    public String getPump2() {
        return pump2;
    }

    public void setPump2(String pump2) {
        this.pump2 = pump2;
    }

    public String getFilter1() {
        return filter1;
    }

    public void setFilter1(String filter1) {
        this.filter1 = filter1;
    }

    public String getFilter2() {
        return filter2;
    }

    public void setFilter2(String filter2) {
        this.filter2 = filter2;
    }

    public String getAux1() {
        return aux1;
    }

    public void setAux1(String aux1) {
        this.aux1 = aux1;
    }

    public String getPanelLock() {
        return panelLock;
    }

    public void setPanelLock(String panelLock) {
        this.panelLock = panelLock;
    }

    public String getMicrosillk() {
        return microsillk;
    }

    public void setMicrosillk(String microsillk) {
        this.microsillk = microsillk;
    }

    public String getOzone() {
        return ozone;
    }

    public void setOzone(String ozone) {
        this.ozone = ozone;
    }

    public Date getUplinkTimestamp() {
        return uplinkTimestamp;
    }

    public void setUplinkTimestamp(Date uplinkTimestamp) {
        this.uplinkTimestamp = uplinkTimestamp;
    }

    public void setRunMode(String runMode) {
        this.runMode = runMode;
    }
}
