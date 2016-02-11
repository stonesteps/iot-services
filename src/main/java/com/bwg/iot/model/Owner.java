package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-09T01:15:44.374Z")
public class Owner  {
  
  private String customerName = null;
  private String firstName = null;
  private String lastName = null;
  private String email = null;
  private String address1 = null;
  private String address2 = null;
  private String city = null;
  private String state = null;
  private String country = null;
  private String zip = null;
  private String phone = null;

  
  /**
   * display name for customer
   **/
  @ApiModelProperty(value = "display name for customer")
  @JsonProperty("customerName")
  public String getCustomerName() {
    return customerName;
  }
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("address1")
  public String getAddress1() {
    return address1;
  }
  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("address2")
  public String getAddress2() {
    return address2;
  }
  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("city")
  public String getCity() {
    return city;
  }
  public void setCity(String city) {
    this.city = city;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("state")
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("country")
  public String getCountry() {
    return country;
  }
  public void setCountry(String country) {
    this.country = country;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("zip")
  public String getZip() {
    return zip;
  }
  public void setZip(String zip) {
    this.zip = zip;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Owner owner = (Owner) o;
    return Objects.equals(customerName, owner.customerName) &&
        Objects.equals(firstName, owner.firstName) &&
        Objects.equals(lastName, owner.lastName) &&
        Objects.equals(email, owner.email) &&
        Objects.equals(address1, owner.address1) &&
        Objects.equals(address2, owner.address2) &&
        Objects.equals(city, owner.city) &&
        Objects.equals(state, owner.state) &&
        Objects.equals(country, owner.country) &&
        Objects.equals(zip, owner.zip) &&
        Objects.equals(phone, owner.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerName, firstName, lastName, email, address1, address2, city, state, country, zip, phone);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Owner {\n");
    
    sb.append("  customerName: ").append(customerName).append("\n");
    sb.append("  firstName: ").append(firstName).append("\n");
    sb.append("  lastName: ").append(lastName).append("\n");
    sb.append("  email: ").append(email).append("\n");
    sb.append("  address1: ").append(address1).append("\n");
    sb.append("  address2: ").append(address2).append("\n");
    sb.append("  city: ").append(city).append("\n");
    sb.append("  state: ").append(state).append("\n");
    sb.append("  country: ").append(country).append("\n");
    sb.append("  zip: ").append(zip).append("\n");
    sb.append("  phone: ").append(phone).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
