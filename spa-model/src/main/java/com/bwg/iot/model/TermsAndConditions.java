package com.bwg.iot.model;

import org.springframework.data.annotation.Id;


public class TermsAndConditions {

    @Id
    private String id;
    private String text;
    private String version;
    private String createdTimestamp;
    private Boolean current;

    public TermsAndConditions(){}

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TermsAndConditions that = (TermsAndConditions) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (createdTimestamp != null ? !createdTimestamp.equals(that.createdTimestamp) : that.createdTimestamp != null) return false;
        return current != null ? current.equals(that.current) : that.current == null;

    }

    @Override
    public String toString() {
        return "TermsAndConditions{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", version='" + version + '\'' +
                ", createdTimestamp='" + createdTimestamp + '\'' +
                ", current=" + current +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (createdTimestamp != null ? createdTimestamp.hashCode() : 0);
        result = 31 * result + (current != null ? current.hashCode() : 0);
        return result;
    }
}