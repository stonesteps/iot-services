package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Date;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Pojo for representing system info of spa pack controller
 */
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class SystemInfo {

    private Integer heaterPower;
    private Integer mfrSSID;
    private Integer modelSSID;
    private int versionSSID;
    private int minorVersion;
    private Integer swSignature;
    private Integer heaterType;
    private Integer currentSetup;
    private Date lastUpdateTimestamp;
    private Collection<DipSwitch> dipSwitches = newArrayList();
    private Integer packMinorVersion;
    private Integer packMajorVersion;
    private Long serialNumber;

    public Integer getPackMajorVersion() {
        return packMajorVersion;
    }

    public void setPackMajorVersion(Integer packMajorVersion) {
        this.packMajorVersion = packMajorVersion;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getPackMinorVersion() {
        return packMinorVersion;
    }

    public void setPackMinorVersion(Integer packMinorVersion) {
        this.packMinorVersion = packMinorVersion;
    }

    public Collection<DipSwitch> getDipSwitches() {
        return dipSwitches;
    }

    public int getVersionSSID() {
        return versionSSID;
    }

    public void setVersionSSID(int versionSSID) {
        this.versionSSID = versionSSID;
    }

    public Integer getHeaterPower() {
        return heaterPower;
    }

    public void setHeaterPower(Integer heaterPower) {
        this.heaterPower = heaterPower;
    }

    public Integer getMfrSSID() {
        return mfrSSID;
    }

    public void setMfrSSID(Integer mfrSSID) {
        this.mfrSSID = mfrSSID;
    }

    public Integer getModelSSID() {
        return modelSSID;
    }

    public void setModelSSID(Integer modelSSID) {
        this.modelSSID = modelSSID;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Integer getSwSignature() {
        return swSignature;
    }

    public void setSwSignature(Integer swSignature) {
        this.swSignature = swSignature;
    }

    public Integer getHeaterType() {
        return heaterType;
    }

    public void setHeaterType(Integer heaterType) {
        this.heaterType = heaterType;
    }

    public Integer getCurrentSetup() {
        return currentSetup;
    }

    public void setCurrentSetup(Integer currentSetup) {
        this.currentSetup = currentSetup;
    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }
}
