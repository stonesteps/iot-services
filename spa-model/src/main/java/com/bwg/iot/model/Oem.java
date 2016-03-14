package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;


@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Oem extends ResourceSupport {

    @Id
    private String _id;
    private String name;
    private Address address;

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

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Oem dealer = (Oem) o;
      return Objects.equals(_id, dealer._id) &&
          Objects.equals(name, dealer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, name);
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Dealer {\n");

        sb.append("  id: ").append(_id).append("\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  address: ").append(address.toString()).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
