package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Spa  {

    @Id
    private String id = null;
    private String serialNumber = null;
    private String productName = null;
    private String model = null;
    private String dealerId = null;
    private String oemId = null;
    private SpaState currentState = null;
    private String registrationDate = null;
    private String manufacturedDate = null;
    private String p2pAPSSID = null;
    private String p2pAPPassword = null;

    private Owner owner = null;

    private List<Alert> alerts = new ArrayList<Alert>();

    public Spa(){}

    public Spa(String serialNumber, String productName, String model){
        this.serialNumber = serialNumber;
        this.productName = productName;
        this.model = model;
    }

    public String getId() {
    return id;
    }
    public void setId(String id) {
    this.id = id;
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
    this.owner = owner;
    }

  
    public List<Alert> getAlerts() {
        return alerts;
    }
    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }


    public SpaState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SpaState currentState) {
        this.currentState = currentState;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getManufacturedDate() {
        return manufacturedDate;
    }

    public void setManufacturedDate(String manufacuredDate) {
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

    @Override
    public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    Spa spa = (Spa) o;
    return Objects.equals(id, spa.id) &&
        Objects.equals(serialNumber, spa.serialNumber) &&
        Objects.equals(productName, spa.productName) &&
        Objects.equals(model, spa.model);
    }

    @Override
    public int hashCode() {
    return Objects.hash(id, serialNumber, productName, model);
    }

    @Override
    public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Spa {\n");

    sb.append("  id: ").append(id).append("\n");
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
