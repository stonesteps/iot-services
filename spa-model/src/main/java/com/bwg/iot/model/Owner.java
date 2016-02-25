package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;


public class Owner  {

    @Id
    private String id;
    private String customerName = null;
    private String firstName = null;
    private String lastName = null;
    private Address address = null;

    public String getId() {
        return id;
    }

    public String getCustomerName() {
      return customerName;
    }
    public void setCustomerName(String customerName) {
      this.customerName = customerName;
    }


    public String getFirstName() {
      return firstName;
    }
    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }


    public String getLastName() {
      return lastName;
    }
    public void setLastName(String lastName) {
      this.lastName = lastName;
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
      Owner owner = (Owner) o;
      return Objects.equals(customerName, owner.customerName) &&
          Objects.equals(firstName, owner.firstName) &&
          Objects.equals(lastName, owner.lastName) &&
          Objects.equals(address, owner.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName, firstName, lastName, address);
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Owner {\n");

        sb.append("  customerName: ").append(customerName).append("\n");
        sb.append("  firstName: ").append(firstName).append("\n");
        sb.append("  lastName: ").append(lastName).append("\n");
        sb.append("  address: ").append(address).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}