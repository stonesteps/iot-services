package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Spa extends ResourceSupport {

    @Id
    private String _id;
    private String serialNumber;
    private String productName;
    private String model;
    private String dealerId;
    private User associate;
    private User technician;
    private String oemId;
    private String templateId;

    private SpaState currentState;

    private Date salesDate;
    private String transactionCode;
    private Date registrationDate;
    private Date manufacturedDate;
    private String p2pAPSSID;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private double[] location;

    @JsonIgnore
    private String p2pAPPassword;
    private String sold = Boolean.FALSE.toString();

    @JsonIgnore
    private String regKey;

    @RestResource
    private User owner = null;

    @RestResource
    private List<Alert> alerts = new ArrayList<Alert>();



    public Spa(){}

    public Spa(String serialNumber, String productName, String model){
        this.serialNumber = serialNumber;
        this.productName = productName;
        this.model = model;
    }

    public boolean isOnline() {
        boolean online = false;
        if (this.currentState != null) {
            Date timestamp = this.currentState.getUplinkTimestamp();
            long updateInterval = currentState.getUpdateIntervalSeconds() < 1 ? 0 : currentState.getUpdateIntervalSeconds();
            updateInterval+=45; // give it time padding for transit&processing

            Date oneIntervalAgo = new Date(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(updateInterval));
            if (timestamp != null) {
                online = timestamp.after(oneIntervalAgo);
            }
        }
        return online;
    }

    public String get_id() {
        return _id;
    }
    public void set_id(String id) {
        this._id = id;
    }


    public String getSerialNumber() {
    return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
    }


    public String getProductName() {
    return productName;
    }
    public void setProductName(String productName) {
    this.productName = productName;
    }


    public String getModel() {
    return model;
    }
    public void setModel(String model) {
    this.model = model;
    }


    public String getDealerId() { return dealerId; }
    public void setDealerId(String dealerId) { this.dealerId = dealerId; }

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
        this.sold = Boolean.toString(owner != null);
    }

  
    public List<Alert> getAlerts() {
        return alerts;
    }
    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public String getSold() {
        return Boolean.toString(!(owner==null));
    }

    public SpaState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SpaState currentState) {
        this.currentState = currentState;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getManufacturedDate() {
        return manufacturedDate;
    }

    public void setManufacturedDate(Date manufacuredDate) {
        this.manufacturedDate = manufacuredDate;
    }

    public String getP2pAPSSID() {
        return p2pAPSSID;
    }

    public void setP2pAPSSID(String p2pAPSSID) {
        this.p2pAPSSID = p2pAPSSID;
    }

    public String getP2pAPPassword() {
        return p2pAPPassword;
    }

    public void setP2pAPPassword(String p2pAPPassword) {
        this.p2pAPPassword = p2pAPPassword;
    }

    public String getRegKey() {
        return regKey;
    }

    public void setRegKey(String regKey) {
        this.regKey = regKey;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public User getAssociate() {
        return associate;
    }

    public void setAssociate(User associate) {
        this.associate = associate;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    Spa spa = (Spa) o;
    return Objects.equals(_id, spa._id) &&
        Objects.equals(serialNumber, spa.serialNumber) &&
        Objects.equals(productName, spa.productName) &&
        Objects.equals(model, spa.model);
    }

    @Override
    public int hashCode() {
    return Objects.hash(_id, serialNumber, productName, model);
    }

    @Override
    public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Spa {\n");

    sb.append("  id: ").append(_id).append("\n");
    sb.append("  serialNumber: ").append(serialNumber).append("\n");
    sb.append("  productName: ").append(productName).append("\n");
    sb.append("  model: ").append(model).append("\n");
    sb.append("  dealerId: ").append(dealerId).append("\n");
    sb.append("  owner: ").append(owner).append("\n");
    sb.append("  alerts: ").append(alerts).append("\n");
    sb.append("}\n");
    return sb.toString();
    }
}
