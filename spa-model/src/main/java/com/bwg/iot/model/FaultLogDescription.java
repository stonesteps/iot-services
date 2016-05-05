package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class FaultLogDescription {

    @Id
    private String _id;

    private String controllerType;
    private int code;
    private String description;
    private FaultLogSeverity severity;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FaultLogSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(FaultLogSeverity severity) {
        this.severity = severity;
    }
}
