package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Document
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class FaultLog extends ResourceSupport {

    @Id
    private String _id;

    private String spaId;
    private String controllerType;
    private String ownerId;
    private String dealerId;
    private String oemId;
    private int code;
    private Date timestamp;
    private FaultLogSeverity severity;
    private int targetTemp;
    private int sensorATemp;
    private int sensorBTemp;
    private boolean celcius;

    @Transient
    private FaultLogDescription faultLogDescription;

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

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public FaultLogSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(FaultLogSeverity severity) {
        this.severity = severity;
    }

    public int getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(int targetTemp) {
        this.targetTemp = targetTemp;
    }

    public int getSensorATemp() {
        return sensorATemp;
    }

    public void setSensorATemp(int sensorATemp) {
        this.sensorATemp = sensorATemp;
    }

    public int getSensorBTemp() {
        return sensorBTemp;
    }

    public void setSensorBTemp(int sensorBTemp) {
        this.sensorBTemp = sensorBTemp;
    }

    public boolean isCelcius() {
        return celcius;
    }

    public void setCelcius(boolean celcius) {
        this.celcius = celcius;
    }

    public FaultLogDescription getFaultLogDescription() {
        return faultLogDescription;
    }

    public void setFaultLogDescription(FaultLogDescription faultLogDescription) {
        this.faultLogDescription = faultLogDescription;
    }
}
