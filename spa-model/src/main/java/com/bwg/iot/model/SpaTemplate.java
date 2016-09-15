package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@CompoundIndexes({
        @CompoundIndex(name = "oem_sku", def = "{ 'oemId' : 1 , 'sku' : 1 }", unique = true)
})

public class SpaTemplate extends ResourceSupport {


    @Id
    private String _id;

    @Indexed
    private String oemId;
    private String productName;
    private String model;
    private String sku;
    private Integer warrantyDays;
    private Boolean locked;
    private Boolean active;
    private List<Material> materialList;
    private List<Attachment> attachments;

    private String notes;
    private Date creationDate;

    public SpaTemplate() {
        this.locked = Boolean.FALSE;
        this.active = Boolean.TRUE;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getWarrantyDays() {
        return warrantyDays;
    }

    public void setWarrantyDays(Integer warrantyDays) {
        this.warrantyDays = warrantyDays;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SpaTemplate that = (SpaTemplate) o;

        if (!oemId.equals(that.oemId)) return false;
        if (!productName.equals(that.productName)) return false;
        if (!model.equals(that.model)) return false;
        if (!sku.equals(that.sku)) return false;
        if (materialList != null ? !materialList.equals(that.materialList) : that.materialList != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        return creationDate != null ? creationDate.equals(that.creationDate) : that.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + oemId.hashCode();
        result = 31 * result + productName.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + sku.hashCode();
        result = 31 * result + (materialList != null ? materialList.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpaTemplate{" +
                "materialList=" + materialList +
                ", _id='" + _id + '\'' +
                ", oemId='" + oemId + '\'' +
                ", productName='" + productName + '\'' +
                ", model='" + model + '\'' +
                ", sku='" + sku + '\'' +
                ", notes='" + notes + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
