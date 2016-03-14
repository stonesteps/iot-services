package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;


@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Dealer extends ResourceSupport {

    @Id
    private String _id;
    private String oemId;
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

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Dealer dealer = (Dealer) o;
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

        sb.append("  _id: ").append(_id).append("\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  address: ").append(address.toString()).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
