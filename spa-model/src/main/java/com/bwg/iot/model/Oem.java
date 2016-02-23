package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;


public class Oem {

    @Id
    private String id;
    private String name;
    private Address address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
      return Objects.equals(id, dealer.id) &&
          Objects.equals(name, dealer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Dealer {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  address: ").append(address.toString()).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
