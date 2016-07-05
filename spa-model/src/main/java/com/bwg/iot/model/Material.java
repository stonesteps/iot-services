package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;


@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@CompoundIndexes({
        @CompoundIndex(name = "mat_oem_ctype_idx", def = "{'oemId': 1, 'componentTYpe':1}")
})
public class Material extends ResourceSupport {

    @Id
    private String _id;

    @Indexed
    private String oemId;
    private String componentType;
    private String brandName;
    private String description;
    private String sku;
    private String alternateSku;
    private int warrantyDays;
    private Date uploadDate;

    // embedded in saved SpaTemplate documents.
    // Not to be stored in Material collection
    private String displayName;
    private String materialType;
    private String port;


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

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getAlternateSku() {
        return alternateSku;
    }

    public void setAlternateSku(String alternateSku) {
        this.alternateSku = alternateSku;
    }

    public int getWarrantyDays() {
        return warrantyDays;
    }

    public void setWarrantyDays(int warrantyDays) {
        this.warrantyDays = warrantyDays;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;
        if (!super.equals(o)) return false;

        Material material = (Material) o;

        if (warrantyDays != material.warrantyDays) return false;
        if (oemId != null ? !oemId.equals(material.oemId) : material.oemId != null) return false;
        if (!componentType.equals(material.componentType)) return false;
        if (brandName != null ? !brandName.equals(material.brandName) : material.brandName != null) return false;
        if (description != null ? !description.equals(material.description) : material.description != null)
            return false;
        if (!sku.equals(material.sku)) return false;
        if (alternateSku != null ? !alternateSku.equals(material.alternateSku) : material.alternateSku != null)
            return false;
        if (uploadDate != null ? !uploadDate.equals(material.uploadDate) : material.uploadDate != null) return false;
        return displayName != null ? displayName.equals(material.displayName) : material.displayName == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (oemId != null ? oemId.hashCode() : 0);
        result = 31 * result + componentType.hashCode();
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + sku.hashCode();
        result = 31 * result + (alternateSku != null ? alternateSku.hashCode() : 0);
        result = 31 * result + warrantyDays;
        result = 31 * result + (uploadDate != null ? uploadDate.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Material{" +
                "_id='" + _id + '\'' +
                ", oemId=" + oemId +
                ", brandName" + brandName +
                ", componentType='" + componentType + '\'' +
                ", description='" + description + '\'' +
                ", sku='" + sku + '\'' +
                ", alternateSku='" + alternateSku + '\'' +
                ", warrantyDays=" + warrantyDays +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
