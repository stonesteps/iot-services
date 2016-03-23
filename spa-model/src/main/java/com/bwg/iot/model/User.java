package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;


@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class User extends ResourceSupport {

    @Id
    private String _id;
    private String username;
    private String dealerId;
    private String oemId;
    private String lastName;
    private String firstName;
    private Address address;
    private List<String> roles;
    private Date createdDate;
    private Date modifiedDate;

    @JsonIgnore
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String username) {
        this.username = username;
    }

    public boolean doesPasswordMatch(String pwd){
        if (StringUtils.isEmpty(this.password)){
            return true;
        } else if (this.password.equals(pwd)) {
            return true;
        }
        return false;
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


    public String get_id() {
        return _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (_id != null ? !_id.equals(user._id) : user._id != null) return false;
        if (dealerId != null ? !dealerId.equals(user.dealerId) : user.dealerId != null) return false;
        if (oemId != null ? !oemId.equals(user.oemId) : user.oemId != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (roles != null ? !roles.equals(user.roles) : user.roles != null) return false;
        if (createdDate != null ? !createdDate.equals(user.createdDate) : user.createdDate != null) return false;
        return modifiedDate != null ? modifiedDate.equals(user.modifiedDate) : user.modifiedDate == null;

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (dealerId != null ? dealerId.hashCode() : 0);
        result = 31 * result + (oemId != null ? oemId.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + _id + '\'' +
                ", dealerId='" + dealerId + '\'' +
                ", oemId='" + oemId + '\'' +
                ", username='" + username + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", address=" + address +
                ", roles=" + roles +
                ", createdDate='" + createdDate.toString() + '\'' +
                ", modifiedDate='" + modifiedDate.toString() + '\'' +
                '}';
    }
}
