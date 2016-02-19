package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;


public class Address {

    @Id
    private String id;
    private String address1 = null;
    private String address2 = null;
    private String city = null;
    private String state = null;
    private String country = null;
    private String zip = null;
    private String phone = null;
    private String email = null;


    public String getEmail() {
      return email;
    }
    public void setEmail(String email) {
      this.email = email;
    }


    public String getAddress1() {
      return address1;
    }
    public void setAddress1(String address1) {
      this.address1 = address1;
    }


    public String getAddress2() {
      return address2;
    }
    public void setAddress2(String address2) {
      this.address2 = address2;
    }


    public String getCity() {
      return city;
    }
    public void setCity(String city) {
      this.city = city;
    }

    public String getState() {
      return state;
    }
    public void setState(String state) {
      this.state = state;
    }

    public String getCountry() {
      return country;
    }
    public void setCountry(String country) {
      this.country = country;
    }

    public String getZip() {
      return zip;
    }
    public void setZip(String zip) {
      this.zip = zip;
    }

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
      Address owner = (Address) o;
      return Objects.equals(email, owner.email) &&
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
        return Objects.hash(email, address1, address2, city, state, country, zip, phone);
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Owner {\n");

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
