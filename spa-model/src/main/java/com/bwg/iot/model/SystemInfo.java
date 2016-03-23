package com.bwg.iot.model;

import java.util.Date;

/**
 * Created by holow on 3/22/2016.
 */
public class SystemInfo {

    private int heaterPower;
    private int mfrSSID;
    private int modelSSID;
    private int versionSSID;
    private int minorVersion;
    private int swSignature;
    private int heaterType;
    private int currentSetup;
    private Date lastUpdateTimestamp;

    public int getVersionSSID() {
        return versionSSID;
    }

    public void setVersionSSID(int versionSSID) {
        this.versionSSID = versionSSID;
    }

    public int getHeaterPower() {
        return heaterPower;
    }

    public void setHeaterPower(int heaterPower) {
        this.heaterPower = heaterPower;
    }

    public int getMfrSSID() {
        return mfrSSID;
    }

    public void setMfrSSID(int mfrSSID) {
        this.mfrSSID = mfrSSID;
    }

    public int getModelSSID() {
        return modelSSID;
    }

    public void setModelSSID(int modelSSID) {
        this.modelSSID = modelSSID;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public int getSwSignature() {
        return swSignature;
    }

    public void setSwSignature(int swSignature) {
        this.swSignature = swSignature;
    }

    public int getHeaterType() {
        return heaterType;
    }

    public void setHeaterType(int heaterType) {
        this.heaterType = heaterType;
    }

    public int getCurrentSetup() {
        return currentSetup;
    }

    public void setCurrentSetup(int currentSetup) {
        this.currentSetup = currentSetup;
    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }
}
