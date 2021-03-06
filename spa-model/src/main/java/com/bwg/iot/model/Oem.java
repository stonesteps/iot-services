package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.Objects;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Oem extends ResourceSupport {

    @Id
    private String _id;
    private String name;

    @Indexed(unique=true, sparse=true)
    private String customerNumber;
    private Address address;
    private String email;
    private String phone;
    private Attachment logo;
    private Date createdDate;
    private Date modifiedDate;

    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Attachment getLogo() {
        return logo;
    }

    public void setLogo(Attachment logo) {
        this.logo = logo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oem)) return false;
        if (!super.equals(o)) return false;

        Oem oem = (Oem) o;

        if (customerNumber != oem.customerNumber) return false;
        if (!name.equals(oem.name)) return false;
        return address != null ? address.equals(oem.address) : oem.address == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + customerNumber.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String logoFilename = "";
        if (logo != null) {
            logoFilename = logo.getName();
        }
        return "Oem{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", customerNumber=" + customerNumber +
                ", address=" + address +
                ", email=" + email +
                ", phone=" + phone +
                ", logo=" + logoFilename +
                '}';
    }
}
