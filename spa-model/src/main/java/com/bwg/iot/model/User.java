package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;


public class User {

    @Id
    private String id;
    private String dealerId;
    private String oemId;
    private String lastName;
    private String firstName;
    private Address address;
    private List<String> roles;
    private String createdDate;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    private String modifiedDate;

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (dealerId != null ? !dealerId.equals(user.dealerId) : user.dealerId != null) return false;
        if (oemId != null ? !oemId.equals(user.oemId) : user.oemId != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (roles != null ? !roles.equals(user.roles) : user.roles != null) return false;
        if (createdDate != null ? !createdDate.equals(user.createdDate) : user.createdDate != null) return false;
        return modifiedDate != null ? modifiedDate.equals(user.modifiedDate) : user.modifiedDate == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dealerId != null ? dealerId.hashCode() : 0);
        result = 31 * result + (oemId != null ? oemId.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", dealerId='" + dealerId + '\'' +
                ", oemId='" + oemId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", address=" + address +
                ", roles=" + roles +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                '}';
    }
}
