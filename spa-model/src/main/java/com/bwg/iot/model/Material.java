package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;


@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Material extends ResourceSupport {

    @Id
    private String _id;
    private List<String> oemId;
    private String componentType;
    private String brandName;
    private String description;
    private String sku;
    private String alternateSku;
    private int warrantyDays;
    private Date uploadDate;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getOemId() {
        return oemId;
    }

    public void setOemId(List<String> oemId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Material material = (Material) o;

        if (warrantyDays != material.warrantyDays) return false;
        if (!_id.equals(material._id)) return false;
        if (!oemId.equals(material.oemId)) return false;
        if (!brandName.equals(material.brandName)) return false;
        if (!componentType.equals(material.componentType)) return false;
        if (!description.equals(material.description)) return false;
        if (!sku.equals(material.sku)) return false;
        return alternateSku != null ? alternateSku.equals(material.alternateSku) : material.alternateSku == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + _id.hashCode();
        result = 31 * result + oemId.hashCode();
        result = 31 * result + componentType.hashCode();
        result = 31 * result + brandName.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + sku.hashCode();
        result = 31 * result + (alternateSku != null ? alternateSku.hashCode() : 0);
        result = 31 * result + warrantyDays;
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
