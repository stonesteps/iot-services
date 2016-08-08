package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by triton on 2/17/16.
 */

@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class SpaState {

    // required props of all spas
    String runMode; // rest, ready
    String desiredTemp;
    String targetDesiredTemp;
    String currentTemp;
    String controllerType;
    int errorCode;
    boolean cleanupCycle;
    Date uplinkTimestamp = null;
    String heaterMode;
    int hour;
    int minute;
    boolean celsius;
    boolean demoMode;
    boolean timeNotSet;
    boolean settingsLock;
    boolean spaOverheatDisabled;
    String bluetoothStatus;
    int updateIntervalSeconds;
    int wifiUpdateIntervalSeconds;
    List<ComponentState> components;
    SetupParams setupParams;
    SystemInfo systemInfo;
    WifiConnectionHealth wifiConnectionHealth;
    Boolean ethernetPluggedIn;
    Boolean rs485ConnectionActive;
    Integer rs485AcquiredAddress;

    // optional params of spas, these can come from different controllers, but an effort
    // is made to coalesce any controller into the same properties first, and if not lined up
    // the a new property is broken out here
    //
    // sourced from NGSC
    Integer messageSeverity;
    Integer uiCode;
    Integer uiSubCode;
    Boolean invert;
    Boolean allSegsOn;
    Boolean panelLock;
    Boolean military;
    TempRange tempRange;
    Boolean primingMode;
    Boolean soundAlarm;
    Boolean repeat;
    PanelMode panelMode;
    SwimSpaMode swimSpaMode;
    Boolean swimSpaModeChanging;
    Boolean heaterCooling;
    Boolean latchingMessage;
    Boolean lightCycle;
    Boolean elapsedTimeDisplay;
    Integer tvLiftState;
    Boolean specialTimeouts;
    Boolean ABDisplay;
    Boolean stirring;
    Boolean ecoMode;
    Boolean soakMode;
    Boolean overrangeEnabled;
    Boolean heatExternallyDisabled;
    Boolean testMode;
    Boolean tempLock;
    //
    // sourced from Jacuzzi
    FiltrationMode secondaryFiltrationMode;
    SpaRunState spaRunState;
    Integer ambientTemp;
    Integer day;
    Integer month;
    Integer year;
    ReminderCode reminderCode;
    Integer reminderDaysClearRay;
    Integer reminderDaysWater;
    Integer reminderDaysFilter1;
    Integer reminderDaysFilter2;
    Boolean blowout;
    Boolean waterLevel1;
    Boolean waterLevel2;
    Boolean flowSwitchClosed;
    Boolean changeUV;
    Integer hiLimitTemp;
    Boolean registrationLockout;
    Boolean engineeringMode;
    Boolean accessLocked;
    Boolean maintenanceLocked;

    String alertState;

    public SpaState(){
        components = new ArrayList<ComponentState>();
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
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

    public Integer getMessageSeverity() {
        return messageSeverity;
    }

    public void setMessageSeverity(Integer messageSeverity) {
        this.messageSeverity = messageSeverity;
    }

    public SetupParams getSetupParams() { return setupParams; }

    public void setSetupParams(SetupParams setupParams) { this.setupParams = setupParams; }

    public SystemInfo getSystemInfo() { return systemInfo; }

    public void setSystemInfo(SystemInfo systemInfo) { this.systemInfo = systemInfo; }

    public String getHeaterMode() { return heaterMode; }

    public void setHeaterMode(String heaterMode) { this.heaterMode = heaterMode; }

    public int getHour() { return hour; }

    public void setHour(int hour) { this.hour = hour; }

    public int getMinute() { return minute; }

    public void setMinute(int minute) { this.minute = minute; }

    public Integer getUiCode() { return uiCode; }

    public void setUiCode(Integer uiCode) { this.uiCode = uiCode; }

    public Integer getUiSubCode() { return uiSubCode; }

    public void setUiSubCode(Integer uiSubCode) { this.uiSubCode = uiSubCode; }

    public Boolean isInvert() { return invert; }

    public void setInvert(Boolean invert) { this.invert = invert; }

    public Boolean isAllSegsOn() { return allSegsOn; }

    public void setAllSegsOn(Boolean allSegsOn) { this.allSegsOn = allSegsOn; }

    public Boolean isPanelLock() { return panelLock; }

    public void setPanelLock(Boolean panelLock) { this.panelLock = panelLock; }

    public Boolean isMilitary() { return military; }

    public void setMilitary(Boolean military) { this.military = military; }

    public boolean isCelsius() { return celsius; }

    public void setCelsius(boolean celsius) { this.celsius = celsius; }

    public TempRange getTempRange() { return tempRange; }

    public void setTempRange(TempRange tempRange) { this.tempRange = tempRange; }

    public Boolean isPrimingMode() { return primingMode; }

    public void setPrimingMode(Boolean primingMode) { this.primingMode = primingMode; }

    public Boolean isSoundAlarm() { return soundAlarm; }

    public void setSoundAlarm(Boolean soundAlarm) { this.soundAlarm = soundAlarm; }

    public Boolean isRepeat() { return repeat; }

    public void setRepeat(Boolean repeat) { this.repeat = repeat; }

    public PanelMode getPanelMode() { return panelMode; }

    public void setPanelMode(PanelMode panelMode) { this.panelMode = panelMode; }

    public SwimSpaMode getSwimSpaMode() { return swimSpaMode; }

    public void setSwimSpaMode(SwimSpaMode swimSpaMode) { this.swimSpaMode = swimSpaMode; }

    public Boolean isSwimSpaModeChanging() { return swimSpaModeChanging; }

    public void setSwimSpaModeChanging(Boolean swimSpaModeChanging) { this.swimSpaModeChanging = swimSpaModeChanging; }

    public Boolean isHeaterCooling() { return heaterCooling; }

    public void setHeaterCooling(Boolean heaterCooling) { this.heaterCooling = heaterCooling; }

    public Boolean isLatchingMessage() { return latchingMessage; }

    public void setLatchingMessage(Boolean latchingMessage) { this.latchingMessage = latchingMessage; }

    public boolean isDemoMode() { return demoMode; }

    public void setDemoMode(boolean demoMode) { this.demoMode = demoMode; }

    public boolean isTimeNotSet() { return timeNotSet; }

    public void setTimeNotSet(boolean timeNotSet) { this.timeNotSet = timeNotSet; }

    public Boolean isLightCycle() { return lightCycle; }

    public void setLightCycle(Boolean lightCycle) { this.lightCycle = lightCycle; }

    public Boolean isElapsedTimeDisplay() { return elapsedTimeDisplay; }

    public void setElapsedTimeDisplay(Boolean elapsedTimeDisplay) { this.elapsedTimeDisplay = elapsedTimeDisplay; }

    public Integer getTvLiftState() { return tvLiftState; }

    public void setTvLiftState(Integer tvLiftState) { this.tvLiftState = tvLiftState; }

    public boolean isSettingsLock() { return settingsLock; }

    public void setSettingsLock(boolean settingsLock) { this.settingsLock = settingsLock; }

    public boolean isSpaOverheatDisabled() { return spaOverheatDisabled; }

    public void setSpaOverheatDisabled(boolean spaOverheatDisabled) { this.spaOverheatDisabled = spaOverheatDisabled; }

    public Boolean isSpecialTimeouts() { return specialTimeouts; }

    public void setSpecialTimeouts(Boolean specialTimeouts) { this.specialTimeouts = specialTimeouts; }

    public Boolean isABDisplay() { return ABDisplay; }

    public void setABDisplay(Boolean ABDisplay) { this.ABDisplay = ABDisplay; }

    public Boolean isStirring() { return stirring; }

    public void setStirring(Boolean stirring) { this.stirring = stirring; }

    public Boolean isEcoMode() { return ecoMode; }

    public void setEcoMode(Boolean ecoMode) { this.ecoMode = ecoMode; }

    public Boolean isSoakMode() { return soakMode; }

    public void setSoakMode(Boolean soakMode) { this.soakMode = soakMode; }

    public String getBluetoothStatus() { return bluetoothStatus; }

    public void setBluetoothStatus(String bluetoothStatus) { this.bluetoothStatus = bluetoothStatus; }

    public Boolean isOverrangeEnabled() { return overrangeEnabled; }

    public void setOverrangeEnabled(Boolean overrangeEnabled) { this.overrangeEnabled = overrangeEnabled; }

    public Boolean isHeatExternallyDisabled() { return heatExternallyDisabled; }

    public void setHeatExternallyDisabled(Boolean heatExternallyDisabled) { this.heatExternallyDisabled = heatExternallyDisabled; }

    public Boolean isTestMode() { return testMode; }

    public void setTestMode(Boolean testMode) { this.testMode = testMode; }

    public Boolean isTempLock() { return tempLock; }

    public void setTempLock(Boolean tempLock) { this.tempLock = tempLock; }

    public int getUpdateIntervalSeconds() { return updateIntervalSeconds; }

    public void setUpdateIntervalSeconds(int updateIntervalSeconds) { this.updateIntervalSeconds = updateIntervalSeconds; }

    public int getWifiUpdateIntervalSeconds() { return wifiUpdateIntervalSeconds; }

    public void setWifiUpdateIntervalSeconds(int wifiUpdateIntervalSeconds) { this.wifiUpdateIntervalSeconds = wifiUpdateIntervalSeconds; }

    public Integer getReminderDaysClearRay() {
        return reminderDaysClearRay;
    }

    public void setReminderDaysClearRay(Integer reminderDaysClearRay) {
        this.reminderDaysClearRay = reminderDaysClearRay;
    }

    public FiltrationMode getSecondaryFiltrationMode() {
        return secondaryFiltrationMode;
    }

    public void setSecondaryFiltrationMode(FiltrationMode secondaryFiltrationMode) {
        this.secondaryFiltrationMode = secondaryFiltrationMode;
    }

    public SpaRunState getSpaRunState() {
        return spaRunState;
    }

    public void setSpaRunState(SpaRunState spaRunState) {
        this.spaRunState = spaRunState;
    }

    public Integer getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(Integer ambientTemp) {
        this.ambientTemp = ambientTemp;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ReminderCode getReminderCode() {
        return reminderCode;
    }

    public void setReminderCode(ReminderCode reminderCode) {
        this.reminderCode = reminderCode;
    }

    public Integer getReminderDaysWater() {
        return reminderDaysWater;
    }

    public void setReminderDaysWater(Integer reminderDaysWater) {
        this.reminderDaysWater = reminderDaysWater;
    }

    public Integer getReminderDaysFilter1() {
        return reminderDaysFilter1;
    }

    public void setReminderDaysFilter1(Integer reminderDaysFilter1) {
        this.reminderDaysFilter1 = reminderDaysFilter1;
    }

    public Integer getReminderDaysFilter2() {
        return reminderDaysFilter2;
    }

    public void setReminderDaysFilter2(Integer reminderDaysFilter2) {
        this.reminderDaysFilter2 = reminderDaysFilter2;
    }

    public Boolean getBlowout() {
        return blowout;
    }

    public void setBlowout(Boolean blowout) {
        this.blowout = blowout;
    }

    public Boolean getWaterLevel1() {
        return waterLevel1;
    }

    public void setWaterLevel1(Boolean waterLevel1) {
        this.waterLevel1 = waterLevel1;
    }

    public Boolean getWaterLevel2() {
        return waterLevel2;
    }

    public void setWaterLevel2(Boolean waterLevel2) {
        this.waterLevel2 = waterLevel2;
    }

    public Boolean getFlowSwitchClosed() {
        return flowSwitchClosed;
    }

    public void setFlowSwitchClosed(Boolean flowSwitchClosed) {
        this.flowSwitchClosed = flowSwitchClosed;
    }

    public Boolean getChangeUV() {
        return changeUV;
    }

    public void setChangeUV(Boolean changeUV) {
        this.changeUV = changeUV;
    }

    public Integer getHiLimitTemp() {
        return hiLimitTemp;
    }

    public void setHiLimitTemp(Integer hiLimitTemp) {
        this.hiLimitTemp = hiLimitTemp;
    }

    public Boolean getRegistrationLockout() {
        return registrationLockout;
    }

    public void setRegistrationLockout(Boolean registrationLockout) {
        this.registrationLockout = registrationLockout;
    }

    public Boolean getEngineeringMode() {
        return engineeringMode;
    }

    public void setEngineeringMode(Boolean engineeringMode) {
        this.engineeringMode = engineeringMode;
    }

    public Boolean getAccessLocked() {
        return accessLocked;
    }

    public void setAccessLocked(Boolean accessLocked) {
        this.accessLocked = accessLocked;
    }

    public Boolean getMaintenanceLocked() {
        return maintenanceLocked;
    }

    public void setMaintenanceLocked(Boolean maintenanceLocked) {
        this.maintenanceLocked = maintenanceLocked;
    }

    public WifiConnectionHealth getWifiConnectionHealth() {
        return wifiConnectionHealth;
    }

    public void setWifiConnectionHealth(WifiConnectionHealth wifiConnectionHealth) {
        this.wifiConnectionHealth = wifiConnectionHealth;
    }

    public Boolean getEthernetPluggedIn() {
        return ethernetPluggedIn;
    }

    public void setEthernetPluggedIn(Boolean ethernetPluggedIn) {
        this.ethernetPluggedIn = ethernetPluggedIn;
    }

    public Boolean getRs485ConnectionActive() {
        return rs485ConnectionActive;
    }

    public void setRs485ConnectionActive(Boolean rs485ConnectionActive) {
        this.rs485ConnectionActive = rs485ConnectionActive;
    }

    public Integer getRs485AcquiredAddress() {
        return rs485AcquiredAddress;
    }

    public void setRs485AcquiredAddress(Integer rs485AcquiredAddress) {
        this.rs485AcquiredAddress = rs485AcquiredAddress;
    }

    public String getAlertState() {
        return alertState;
    }

    public void setAlertState(String alertState) {
        this.alertState = alertState;
    }

    @Override
    public String toString() {
        return "SpaState{" +
                "  runMode='" + runMode + '\'' +
                ", desiredTemp='" + desiredTemp + '\'' +
                ", targetDesiredTemp='" + targetDesiredTemp + '\'' +
                ", currentTemp='" + currentTemp + '\'' +
                ", components=" + components +
                ", uplinkTimestamp='" + uplinkTimestamp + '\'' +
                '}';
    }
}
