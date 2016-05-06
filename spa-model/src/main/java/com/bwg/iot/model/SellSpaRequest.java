package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class SellSpaRequest extends ResourceSupport {

    private String spaId;
    private String ownerId;
    private String associateId;
    private String technicianId;
    private Date salesDate;

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SellSpaRequest that = (SellSpaRequest) o;

        if (spaId != null ? !spaId.equals(that.spaId) : that.spaId != null) return false;
        if (!ownerId.equals(that.ownerId)) return false;
        if (associateId != null ? !associateId.equals(that.associateId) : that.associateId != null) return false;
        return salesDate != null ? salesDate.equals(that.salesDate) : that.salesDate == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (spaId != null ? spaId.hashCode() : 0);
        result = 31 * result + ownerId.hashCode();
        result = 31 * result + (associateId != null ? associateId.hashCode() : 0);
        result = 31 * result + (salesDate != null ? salesDate.hashCode() : 0);
        return result;
    }
}
