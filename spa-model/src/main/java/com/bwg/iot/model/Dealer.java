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
public class Dealer extends ResourceSupport {

    @Id
    private String _id;

    @Indexed
    private String oemId;
    private String name;
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

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId;
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
        String logoFilename = "";
        if (logo != null) {
            logoFilename = logo.getName();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("class Dealer {\n");
        sb.append("  _id: ").append(_id).append("\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  address: ").append(address.toString()).append("\n");
        sb.append("  email: ").append(email).append("\n");
        sb.append("  phone: ").append(phone).append("\n");
        sb.append("  address: ").append(address.toString()).append("\n");
        sb.append("  logo: ").append(logoFilename).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
