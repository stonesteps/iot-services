package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Document
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WifiStat extends ResourceSupport {

    @Id
    private String _id;

    private String spaId;
    private String ownerId;
    private String dealerId;
    private String oemId;

    private Date recordedDate;
    private Long elapsedDeltaMilliseconds;
    private WifiConnectionHealth wifiConnectionHealth;
    private Double txPowerDbm;
    private String apMacAddress;
    private String mode;
    private String retryLimitPhraseConfig;
    private String retryLimitValueConfig;
    private String rtsConfig;
    private String fragConfig;
    private String powerMgmtConfig;
    private String SSID;
    private WifiConnectionDiagnostics connectedDiag;
    private String sensitivity;
    private Boolean ethernetPortPluggedIn;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId;
    }

    public Date getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Long getElapsedDeltaMilliseconds() {
        return elapsedDeltaMilliseconds;
    }

    public void setElapsedDeltaMilliseconds(Long elapsedDeltaMilliseconds) {
        this.elapsedDeltaMilliseconds = elapsedDeltaMilliseconds;
    }

    public WifiConnectionHealth getWifiConnectionHealth() {
        return wifiConnectionHealth;
    }

    public void setWifiConnectionHealth(WifiConnectionHealth wifiConnectionHealth) {
        this.wifiConnectionHealth = wifiConnectionHealth;
    }

    public Double getTxPowerDbm() {
        return txPowerDbm;
    }

    public void setTxPowerDbm(Double txPowerDbm) {
        this.txPowerDbm = txPowerDbm;
    }

    public String getApMacAddress() {
        return apMacAddress;
    }

    public void setApMacAddress(String apMacAddress) {
        this.apMacAddress = apMacAddress;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRetryLimitPhraseConfig() {
        return retryLimitPhraseConfig;
    }

    public void setRetryLimitPhraseConfig(String retryLimitPhraseConfig) {
        this.retryLimitPhraseConfig = retryLimitPhraseConfig;
    }

    public String getRetryLimitValueConfig() {
        return retryLimitValueConfig;
    }

    public void setRetryLimitValueConfig(String retryLimitValueConfig) {
        this.retryLimitValueConfig = retryLimitValueConfig;
    }

    public String getRtsConfig() {
        return rtsConfig;
    }

    public void setRtsConfig(String rtsConfig) {
        this.rtsConfig = rtsConfig;
    }

    public String getFragConfig() {
        return fragConfig;
    }

    public void setFragConfig(String fragConfig) {
        this.fragConfig = fragConfig;
    }

    public String getPowerMgmtConfig() {
        return powerMgmtConfig;
    }

    public void setPowerMgmtConfig(String powerMgmtConfig) {
        this.powerMgmtConfig = powerMgmtConfig;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public WifiConnectionDiagnostics getConnectedDiag() {
        return connectedDiag;
    }

    public void setConnectedDiag(WifiConnectionDiagnostics connectedDiag) {
        this.connectedDiag = connectedDiag;
    }

    public String getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public Boolean isEthernetPortPluggedIn() {
        return ethernetPortPluggedIn;
    }

    public void setEthernetPortPluggedIn(Boolean ethernetPortPluggedIn) {
        this.ethernetPortPluggedIn = ethernetPortPluggedIn;
    }
}
