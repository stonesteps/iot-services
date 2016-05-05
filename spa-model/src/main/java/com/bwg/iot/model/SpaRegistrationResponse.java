package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class SpaRegistrationResponse extends ResourceSupport {

    private String spaId;
    private Map<String, Object> token;

    public SpaRegistrationResponse(String spaId, Map<String, Object> token){
        this.spaId = spaId;
        this.token = token;
    }

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public Map<String, Object> getToken() {
        return token;
    }

    public void setToken(Map<String, Object> token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SpaRegistrationResponse response = (SpaRegistrationResponse) o;

        if (spaId != null ? !spaId.equals(response.spaId) : response.spaId != null) return false;
        return token != null ? token.equals(response.token) : response.token == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (spaId != null ? spaId.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }
}
