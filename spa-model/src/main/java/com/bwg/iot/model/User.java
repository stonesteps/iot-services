package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.jaxrs.JaxRsLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class User extends ResourceSupport {

    public enum Role {
        OWNER, BWG, OEM, DEALER, ASSOCIATE, TECHNICIAN, ADMIN
    }

    @Id
    private String _id;
    private String dealerId;
    private String oemId;
    private String lastName;
    private String firstName;
    private String phone;
    private String email;
    private Address address;
    private List<String> roles;
    private Date createdDate;
    private Date modifiedDate;
    private String notes;

    private String spaId;
    private String errorMessage;

    @Transient
    private String fullName;
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Transient
    private String username;
    public String getUsername() {
        return email;
    }

    private String password;


    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public boolean hasRoles(List roleList) {
        return roles.containsAll(roleList);
    }


    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
      return this.password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getNotes() {
      return notes;
    }

    public void setNotes(String notes) {
      this.notes = notes;
    }
    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public String get_id() {
        return _id;
    }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


    public void set_id(String _id) { this._id = _id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (_id != null ? !_id.equals(user._id) : user._id != null) return false;
        if (dealerId != null ? !dealerId.equals(user.dealerId) : user.dealerId != null) return false;
        if (oemId != null ? !oemId.equals(user.oemId) : user.oemId != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (roles != null ? !roles.equals(user.roles) : user.roles != null) return false;
        if (createdDate != null ? !createdDate.equals(user.createdDate) : user.createdDate != null) return false;
        if (notes != null ? !notes.equals(user.notes) : user.notes != null) return false;
        return modifiedDate != null ? modifiedDate.equals(user.modifiedDate) : user.modifiedDate == null;

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (dealerId != null ? dealerId.hashCode() : 0);
        result = 31 * result + (oemId != null ? oemId.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String cdate = (createdDate == null) ? "" : createdDate.toString();
        String mdate = (modifiedDate == null) ? "" : modifiedDate.toString();
        return "User{" +
                "id='" + _id + '\'' +
                ", dealerId='" + dealerId + '\'' +
                ", oemId='" + oemId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", roles=" + roles +
                ", createdDate='" + cdate + '\'' +
                ", modifiedDate='" + mdate + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public User toUserLite() {
        User lite = new User();
        lite.set_id(_id);
        lite.setEmail(email);
        lite.setFirstName(firstName);
        lite.setLastName(lastName);
        lite.setDealerId(dealerId);
        lite.setOemId(oemId);
        lite.setRoles(roles);
        return lite;
    }

    public User toMinimal() {
        User lite = new User();
        lite.set_id(_id);
        lite.setFirstName(firstName);
        lite.setLastName(lastName);
        return lite;
    }
}
