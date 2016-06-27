package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;


@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class ComponentState extends ResourceSupport {

    private String componentType;
    private String port;
    private String serialNumber;
    private String value;
    private String targetValue;
    private List<String> availableValues;
    private String name;
    private Date registeredTimestamp;
    private String componentId;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

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

    public List<String> getAvailableValues() { return availableValues; }

    public void setAvailableValues(List<String> availableValues) {
        this.availableValues = availableValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegisteredTimestamp() { return registeredTimestamp; }

    public void setRegisteredTimestamp(Date registeredTimestamp) { this.registeredTimestamp = registeredTimestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentState that = (ComponentState) o;

        if (componentType != null ? !componentType.equals(that.componentType) : that.componentType != null)
            return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (componentId != null ? !componentId.equals(that.componentId) : that.componentId != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = componentType != null ? componentType.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (componentId != null ? componentId.hashCode() : 0);
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

    public boolean requiresPort() {
        return (componentType != null && Component.PORT_BASED_COMPONENT_TYPES.contains(componentType));
    }
}
