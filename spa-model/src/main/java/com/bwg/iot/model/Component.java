package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;


@Document
@CompoundIndexes({
        @CompoundIndex(name = "cmp_spa_ctype_idx", def = "{'spaId': 1, 'componentTYpe':1}")
})
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Component extends ResourceSupport {

    public enum ComponentType { GATEWAY, MOTE, PUMP, LIGHT, BLOWER, MISTER, FILTER, AUX, PANEL, OZONE, MICROSILK, CONTROLLER, UV, AV, CIRCULATION_PUMP };
    public final static List<String> PORT_BASED_COMPONENT_TYPES =
            newArrayList(ComponentType.AUX.name(),
                    ComponentType.BLOWER.name(),
                    ComponentType.LIGHT.name(),
                    ComponentType.MOTE.name(),
                    ComponentType.PANEL.name(),
                    ComponentType.PUMP.name(),
                    ComponentType.FILTER.name(),
                    ComponentType.MISTER.name()
            );


    @Id
    private String _id;

    @Indexed
    private String spaId;
    private String ownerId;
    private String dealerId;
    private String oemId;
    private String templateId;
    private String componentType;
    private String name;
    private String serialNumber;
    private String port;
    private String sku;
    private Date registrationDate;
    private Boolean factoryInit = new Boolean(true);
    private Map<String, String> metaValues = newHashMap();
    private List<AssociatedSensor> associatedSensors = newArrayList();

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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
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

    public Map<String, String> getMetaValues() {
        return metaValues;
    }

    public void setMetaValues(Map<String, String> values) {
        this.metaValues = values;
    }

    public Boolean isFactoryInit() {
        return factoryInit;
    }

    public void setFactoryInit(Boolean factoryInit) {
        this.factoryInit = factoryInit;
    }

    /**
     * get the sensors that are associated to the component.
     *
     * These associations are only updated as a result of person asserting them such as spa planner activity. They should only
     * be created/updated as a result of a person saying 'hey, this sensor/mote instance was connected and measures this spa component'.
     *
     * The payoff then is that in System Info tab, the UI can know what sensors are associated to spa components
     * and query for those specific sensor measurements and display right next to the real component they are
     * related to. Primary example being ac current meassurments shown next to PUMP components.
     *
     * @return
     */
    public List<AssociatedSensor> getAssociatedSensors() {
        return associatedSensors;
    }

    public void setAssociatedSensors(List<AssociatedSensor> associatedComponentSensors) {
        this.associatedSensors = associatedComponentSensors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Component component = (Component) o;

        if (!componentType.equals(component.componentType)) return false;
        if (!serialNumber.equals(component.serialNumber)) return false;
        return registrationDate != null ? registrationDate.equals(component.registrationDate) : component.registrationDate == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + componentType.hashCode();
        result = 31 * result + serialNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Component{" +
                "_id='" + _id + '\'' +
                ", spaId='" + spaId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", dealerId='" + dealerId + '\'' +
                ", oemId='" + oemId + '\'' +
                ", spaId='" + spaId + '\'' +
                ", templateId='" + templateId + '\'' +
                ", componentType='" + componentType + '\'' +
                ", name='" + name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", port='" + port + '\'' +
                ", sku='" + sku + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                '}';
    }


}
