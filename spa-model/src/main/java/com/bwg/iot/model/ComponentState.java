package com.bwg.iot.model;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;


public class ComponentState extends ResourceSupport {

    private String componentType;
    private String port;
    private String serialNumber;
    private String value;
    private String targetValue;
    private String name;

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String value) {
        this.targetValue = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentState that = (ComponentState) o;

        if (componentType != null ? !componentType.equals(that.componentType) : that.componentType != null)
            return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = componentType != null ? componentType.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComponentState{" +
                "componentType='" + componentType + '\'' +
                ", port='" + port + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", value='" + value + '\'' +
                ", targetValue='" + targetValue + '\'' +
                '}';
    }
}
