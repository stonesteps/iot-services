package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class SpaRegistrationResponse extends ResourceSupport {

    private String spaId;
    private GluuToken token;

    public SpaRegistrationResponse(String spaId, GluuToken token){
        this.spaId = spaId;
        this.token = token;
    }

    public SpaRegistrationResponse(String spaId, String tokenString) throws IOException {
        this.spaId = spaId;
        GluuToken myToken = new GluuToken();
        ObjectMapper mapper = new ObjectMapper();
        myToken = mapper.readValue(tokenString, GluuToken.class);
        this.token = myToken;
    }

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public GluuToken getToken() {
        return token;
    }

    public void setToken(GluuToken token) {
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
