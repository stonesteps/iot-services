package com.bwg.iot.model;

import org.springframework.data.annotation.Id;


public class TacUserAgreement {

    @Id
    private String id;
    private String userId;
    private String version;
    private String dateAgreed;
    private Boolean current;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDateAgreed() {
        return dateAgreed;
    }

    public void setDateAgreed(String dateAgreed) {
        this.dateAgreed = dateAgreed;
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

        TacUserAgreement that = (TacUserAgreement) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return version != null ? version.equals(that.version) : that.version == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Owner {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  userId: ").append(userId).append("\n");
        sb.append("  version: ").append(version).append("\n");
        sb.append("  dateAgreed: ").append(dateAgreed).append("\n");
        sb.append("  current: ").append(current.toString()).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}