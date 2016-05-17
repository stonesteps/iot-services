package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class SpaRegistrationRequest extends ResourceSupport {

    private String spaId;
    private String regKey;
    private String serialNumber;
    private User user;
    private String access_token;
    private Double lon;
    private Double lat;

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public String getRegKey() {
        return regKey;
    }

    public void setRegKey(String regKey) {
        this.regKey = regKey;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SpaRegistrationRequest that = (SpaRegistrationRequest) o;

        if (spaId != null ? !spaId.equals(that.spaId) : that.spaId != null) return false;
        if (regKey != null ? !regKey.equals(that.regKey) : that.regKey != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        return user != null ? user.equals(that.user) : that.user == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (spaId != null ? spaId.hashCode() : 0);
        result = 31 * result + (regKey != null ? regKey.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
