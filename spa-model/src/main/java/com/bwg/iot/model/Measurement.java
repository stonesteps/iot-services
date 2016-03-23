package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;


public class Measurement {

    @Id
    private String _id;
    private String spaId;
    private String originatorId;
    private Date measurementTimestamp;
    private Date processedTimestamp;
    private String sourceDeviceId;
    private HashMap<String, String> values;

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

    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public Date getMeasurementTimestamp() {
        return measurementTimestamp;
    }

    public void setMeasurementTimestamp(Date measurementTimestamp) {
        this.measurementTimestamp = measurementTimestamp;
    }

    public Date getProcessedTimestamp() {
        return processedTimestamp;
    }

    public void setProcessedTimestamp(Date processedTimestamp) {
        this.processedTimestamp = processedTimestamp;
    }

    public String getSourceDeviceId() {
        return sourceDeviceId;
    }

    public void setSourceDeviceId(String sourceDeviceId) {
        this.sourceDeviceId = sourceDeviceId;
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        if (!_id.equals(that._id)) return false;
        if (!spaId.equals(that.spaId)) return false;
        if (!originatorId.equals(that.originatorId)) return false;
        if (!measurementTimestamp.equals(that.measurementTimestamp)) return false;
        if (!sourceDeviceId.equals(that.sourceDeviceId)) return false;
        return values.equals(that.values);

    }

    @Override
    public int hashCode() {
        int result = _id.hashCode();
        result = 31 * result + spaId.hashCode();
        result = 31 * result + originatorId.hashCode();
        result = 31 * result + measurementTimestamp.hashCode();
        result = 31 * result + sourceDeviceId.hashCode();
        result = 31 * result + values.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "_id='" + _id + '\'' +
                ", spaId='" + spaId + '\'' +
                ", originatorId='" + originatorId + '\'' +
                ", measurementTimestamp='" + measurementTimestamp + '\'' +
                ", processedTimestamp='" + processedTimestamp + '\'' +
                ", sourceDeviceId='" + sourceDeviceId + '\'' +
                ", values=" + values +
                '}';
    }
}
