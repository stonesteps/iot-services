package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by triton on 2/17/16.
 */

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class SpaState {
    String runMode; // rest, ready
    String desiredTemp;
    String targetDesiredTemp;
    String currentTemp;
    boolean filterCycle1Active;
    boolean filterCycle2Active;
    int errorCode;
    boolean cleanupCycle;
    int messageSeverity;
    List<ComponentState> components;
    List<Measurement> measurements;

    Date uplinkTimestamp = null;

    SetupParams setupParams;
    SystemInfo systemInfo;

    // Controller params
    int heaterMode;
    int hour;
    int minute;
    int uiCode;
    int uiSubCode;
    boolean invert;
    boolean allSegsOn;
    boolean panelLock;
    boolean military;
    boolean celsius;
    TempRange tempRange;
    boolean primingMode;
    boolean soundAlarm;
    boolean repeat;
    PanelMode panelMode;
    SwimSpaMode swimSpaMode;
    boolean swimSpaModeChanging;
    boolean heaterCooling;
    boolean latchingMessage;
    boolean demoMode;
    boolean timeNotSet;
    boolean lightCycle;
    boolean elapsedTimeDisplay;
    int tvLiftState;
    boolean settingsLock;
    boolean spaOverheatDisabled;
    boolean specialTimeouts;
    boolean ABDisplay;
    boolean stirring;
    boolean ecoMode;
    boolean soakMode;
    int bluetoothStatus;
    boolean overrangeEnabled;
    boolean heatExternallyDisabled;
    boolean testMode;
    boolean tempLock;

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

    public Date getUplinkTimestamp() {
        return uplinkTimestamp;
    }

    public void setUplinkTimestamp(Date uplinkTimestamp) {
        this.uplinkTimestamp = uplinkTimestamp;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isCleanupCycle() {
        return cleanupCycle;
    }

    public void setCleanupCycle(boolean cleanupCycle) {
        this.cleanupCycle = cleanupCycle;
    }

    public int getMessageSeverity() {
        return messageSeverity;
    }

    public void setMessageSeverity(int messageSeverity) {
        this.messageSeverity = messageSeverity;
    }

    public SetupParams getSetupParams() { return setupParams; }

    public void setSetupParams(SetupParams setupParams) { this.setupParams = setupParams; }

    public SystemInfo getSystemInfo() { return systemInfo; }

    public void setSystemInfo(SystemInfo systemInfo) { this.systemInfo = systemInfo; }

    public int getHeaterMode() { return heaterMode; }

    public void setHeaterMode(int heaterMode) { this.heaterMode = heaterMode; }

    public int getHour() { return hour; }

    public void setHour(int hour) { this.hour = hour; }

    public int getMinute() { return minute; }

    public void setMinute(int minute) { this.minute = minute; }

    public int getUiCode() { return uiCode; }

    public void setUiCode(int uiCode) { this.uiCode = uiCode; }

    public int getUiSubCode() { return uiSubCode; }

    public void setUiSubCode(int uiSubCode) { this.uiSubCode = uiSubCode; }

    public boolean isInvert() { return invert; }

    public void setInvert(boolean invert) { this.invert = invert; }

    public boolean isAllSegsOn() { return allSegsOn; }

    public void setAllSegsOn(boolean allSegsOn) { this.allSegsOn = allSegsOn; }

    public boolean isPanelLock() { return panelLock; }

    public void setPanelLock(boolean panelLock) { this.panelLock = panelLock; }

    public boolean isMilitary() { return military; }

    public void setMilitary(boolean military) { this.military = military; }

    public boolean isCelsius() { return celsius; }

    public void setCelsius(boolean celsius) { this.celsius = celsius; }

    public TempRange getTempRange() { return tempRange; }

    public void setTempRange(TempRange tempRange) { this.tempRange = tempRange; }

    public boolean isPrimingMode() { return primingMode; }

    public void setPrimingMode(boolean primingMode) { this.primingMode = primingMode; }

    public boolean isSoundAlarm() { return soundAlarm; }

    public void setSoundAlarm(boolean soundAlarm) { this.soundAlarm = soundAlarm; }

    public boolean isRepeat() { return repeat; }

    public void setRepeat(boolean repeat) { this.repeat = repeat; }

    public PanelMode getPanelMode() { return panelMode; }

    public void setPanelMode(PanelMode panelMode) { this.panelMode = panelMode; }

    public SwimSpaMode getSwimSpaMode() { return swimSpaMode; }

    public void setSwimSpaMode(SwimSpaMode swimSpaMode) { this.swimSpaMode = swimSpaMode; }

    public boolean isSwimSpaModeChanging() { return swimSpaModeChanging; }

    public void setSwimSpaModeChanging(boolean swimSpaModeChanging) { this.swimSpaModeChanging = swimSpaModeChanging; }

    public boolean isHeaterCooling() { return heaterCooling; }

    public void setHeaterCooling(boolean heaterCooling) { this.heaterCooling = heaterCooling; }

    public boolean isLatchingMessage() { return latchingMessage; }

    public void setLatchingMessage(boolean latchingMessage) { this.latchingMessage = latchingMessage; }

    public boolean isDemoMode() { return demoMode; }

    public void setDemoMode(boolean demoMode) { this.demoMode = demoMode; }

    public boolean isTimeNotSet() { return timeNotSet; }

    public void setTimeNotSet(boolean timeNotSet) { this.timeNotSet = timeNotSet; }

    public boolean isLightCycle() { return lightCycle; }

    public void setLightCycle(boolean lightCycle) { this.lightCycle = lightCycle; }

    public boolean isElapsedTimeDisplay() { return elapsedTimeDisplay; }

    public void setElapsedTimeDisplay(boolean elapsedTimeDisplay) { this.elapsedTimeDisplay = elapsedTimeDisplay; }

    public int getTvLiftState() { return tvLiftState; }

    public void setTvLiftState(int tvLiftState) { this.tvLiftState = tvLiftState; }

    public boolean isSettingsLock() { return settingsLock; }

    public void setSettingsLock(boolean settingsLock) { this.settingsLock = settingsLock; }

    public boolean isSpaOverheatDisabled() { return spaOverheatDisabled; }

    public void setSpaOverheatDisabled(boolean spaOverheatDisabled) { this.spaOverheatDisabled = spaOverheatDisabled; }

    public boolean isSpecialTimeouts() { return specialTimeouts; }

    public void setSpecialTimeouts(boolean specialTimeouts) { this.specialTimeouts = specialTimeouts; }

    public boolean isABDisplay() { return ABDisplay; }

    public void setABDisplay(boolean ABDisplay) { this.ABDisplay = ABDisplay; }

    public boolean isStirring() { return stirring; }

    public void setStirring(boolean stirring) { this.stirring = stirring; }

    public boolean isEcoMode() { return ecoMode; }

    public void setEcoMode(boolean ecoMode) { this.ecoMode = ecoMode; }

    public boolean isSoakMode() { return soakMode; }

    public void setSoakMode(boolean soakMode) { this.soakMode = soakMode; }

    public int getBluetoothStatus() { return bluetoothStatus; }

    public void setBluetoothStatus(int bluetoothStatus) { this.bluetoothStatus = bluetoothStatus; }

    public boolean isOverrangeEnabled() { return overrangeEnabled; }

    public void setOverrangeEnabled(boolean overrangeEnabled) { this.overrangeEnabled = overrangeEnabled; }

    public boolean isHeatExternallyDisabled() { return heatExternallyDisabled; }

    public void setHeatExternallyDisabled(boolean heatExternallyDisabled) { this.heatExternallyDisabled = heatExternallyDisabled; }

    public boolean isTestMode() { return testMode; }

    public void setTestMode(boolean testMode) { this.testMode = testMode; }

    public boolean isTempLock() { return tempLock; }

    public void setTempLock(boolean tempLock) { this.tempLock = tempLock; }

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
