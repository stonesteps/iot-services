package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Address extends ResourceSupport {

    @Id
    private String _id;
    private String address1 = null;
    private String address2 = null;
    private String city = null;
    private String state = null;
    private String country = null;
    private String zip = null;

    public String get_id() {
        return _id;
    }
    public void set_id(String id) {
        this._id = id;
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


    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Address owner = (Address) o;
      return Objects.equals(address1, owner.address1) &&
          Objects.equals(address2, owner.address2) &&
          Objects.equals(city, owner.city) &&
          Objects.equals(state, owner.state) &&
          Objects.equals(country, owner.country) &&
          Objects.equals(zip, owner.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address1, address2, city, state, country, zip);
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Address {\n");

        sb.append("  address1: ").append(address1).append("\n");
        sb.append("  address2: ").append(address2).append("\n");
        sb.append("  city: ").append(city).append("\n");
        sb.append("  state: ").append(state).append("\n");
        sb.append("  country: ").append(country).append("\n");
        sb.append("  zip: ").append(zip).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
