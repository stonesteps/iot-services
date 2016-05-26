package com.bwg.iot.model;

public class WifiConnectionDiagnostics {

    private String frequency;
    private String rawDataRate;
    private Long dataRate;
    private Long deltaDataRate;
    private Long linkQualityPercentage; //0 - 100
    private Long deltaLinkQualityPercentage;
    private String linkQualityRaw;
    private Long signalLevelUnits; //usually it's dBm, but no gaurantee
    private String signalLevelUnitsRaw;
    private Long deltaSignalLevelUnits;
    private Long rxOtherAPPacketCount;
    private Long deltaRxOtherAPPacketCount;
    private Long rxInvalidCryptPacketCount;
    private Long deltaRxInvalidCryptPacketCount;
    private Long rxInvalidFragPacketCount;
    private Long deltaRxInvalidFragPacketCount;
    private Long txExcessiveRetries;
    private Long deltaTxExcessiveRetries;
    private Long lostBeaconCount;
    private Long deltaLostBeaconCount;
    private Long noiseLevel;
    private Long deltaNoiseLevel;
    private String noiseLevelRaw;

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRawDataRate() {
        return rawDataRate;
    }

    public void setRawDataRate(String rawDataRate) {
        this.rawDataRate = rawDataRate;
    }

    public Long getDataRate() {
        return dataRate;
    }

    public void setDataRate(Long dataRate) {
        this.dataRate = dataRate;
    }

    public Long getDeltaDataRate() {
        return deltaDataRate;
    }

    public void setDeltaDataRate(Long deltaDataRate) {
        this.deltaDataRate = deltaDataRate;
    }

    public Long getLinkQualityPercentage() {
        return linkQualityPercentage;
    }

    public void setLinkQualityPercentage(Long linkQualityPercentage) {
        this.linkQualityPercentage = linkQualityPercentage;
    }

    public Long getDeltaLinkQualityPercentage() {
        return deltaLinkQualityPercentage;
    }

    public void setDeltaLinkQualityPercentage(Long deltaLinkQualityPercentage) {
        this.deltaLinkQualityPercentage = deltaLinkQualityPercentage;
    }

    public String getLinkQualityRaw() {
        return linkQualityRaw;
    }

    public void setLinkQualityRaw(String linkQualityRaw) {
        this.linkQualityRaw = linkQualityRaw;
    }

    public Long getSignalLevelUnits() {
        return signalLevelUnits;
    }

    public void setSignalLevelUnits(Long signalLevelUnits) {
        this.signalLevelUnits = signalLevelUnits;
    }

    public Long getDeltaSignalLevelUnits() {
        return deltaSignalLevelUnits;
    }

    public void setDeltaSignalLevelUnits(Long deltaSignalLevelUnits) {
        this.deltaSignalLevelUnits = deltaSignalLevelUnits;
    }

    public Long getRxOtherAPPacketCount() {
        return rxOtherAPPacketCount;
    }

    public void setRxOtherAPPacketCount(Long rxOtherAPPacketCount) {
        this.rxOtherAPPacketCount = rxOtherAPPacketCount;
    }

    public Long getDeltaRxOtherAPPacketCount() {
        return deltaRxOtherAPPacketCount;
    }

    public void setDeltaRxOtherAPPacketCount(Long deltaRxOtherAPPacketCount) {
        this.deltaRxOtherAPPacketCount = deltaRxOtherAPPacketCount;
    }

    public Long getRxInvalidCryptPacketCount() {
        return rxInvalidCryptPacketCount;
    }

    public void setRxInvalidCryptPacketCount(Long rxInvalidCryptPacketCount) {
        this.rxInvalidCryptPacketCount = rxInvalidCryptPacketCount;
    }

    public Long getDeltaRxInvalidCryptPacketCount() {
        return deltaRxInvalidCryptPacketCount;
    }

    public void setDeltaRxInvalidCryptPacketCount(Long deltaRxInvalidCryptPacketCount) {
        this.deltaRxInvalidCryptPacketCount = deltaRxInvalidCryptPacketCount;
    }

    public Long getRxInvalidFragPacketCount() {
        return rxInvalidFragPacketCount;
    }

    public void setRxInvalidFragPacketCount(Long rxInvalidFragPacketCount) {
        this.rxInvalidFragPacketCount = rxInvalidFragPacketCount;
    }

    public Long getDeltaRxInvalidFragPacketCount() {
        return deltaRxInvalidFragPacketCount;
    }

    public void setDeltaRxInvalidFragPacketCount(Long deltaRxInvalidFragPacketCount) {
        this.deltaRxInvalidFragPacketCount = deltaRxInvalidFragPacketCount;
    }

    public Long getTxExcessiveRetries() {
        return txExcessiveRetries;
    }

    public void setTxExcessiveRetries(Long txExcessiveRetries) {
        this.txExcessiveRetries = txExcessiveRetries;
    }

    public Long getDeltaTxExcessiveRetries() {
        return deltaTxExcessiveRetries;
    }

    public void setDeltaTxExcessiveRetries(Long deltaTxExcessiveRetries) {
        this.deltaTxExcessiveRetries = deltaTxExcessiveRetries;
    }

    public Long getLostBeaconCount() {
        return lostBeaconCount;
    }

    public void setLostBeaconCount(Long lostBeaconCount) {
        this.lostBeaconCount = lostBeaconCount;
    }

    public Long getDeltaLostBeaconCount() {
        return deltaLostBeaconCount;
    }

    public void setDeltaLostBeaconCount(Long deltaLostBeaconCount) {
        this.deltaLostBeaconCount = deltaLostBeaconCount;
    }

    public Long getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Long noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public Long getDeltaNoiseLevel() {
        return deltaNoiseLevel;
    }

    public void setDeltaNoiseLevel(Long deltaNoiseLevel) {
        this.deltaNoiseLevel = deltaNoiseLevel;
    }

    public String getSignalLevelUnitsRaw() {
        return signalLevelUnitsRaw;
    }

    public void setSignalLevelUnitsRaw(String signalLevelUnitsRaw) {
        this.signalLevelUnitsRaw = signalLevelUnitsRaw;
    }

    public String getNoiseLevelRaw() {
        return noiseLevelRaw;
    }

    public void setNoiseLevelRaw(String noiseLevelRaw) {
        this.noiseLevelRaw = noiseLevelRaw;
    }
}
