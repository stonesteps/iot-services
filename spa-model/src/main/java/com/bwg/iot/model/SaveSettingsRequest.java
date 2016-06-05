package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.HashMap;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class SaveSettingsRequest extends ResourceSupport {

    private String _id;
    private String spaId;
    private HashMap<String, HashMap<String, String>> settings;
    private String name;
    //    private Schedule schedule;
    private String notes;
    private Date creationDate;

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public HashMap<String, HashMap<String, String>> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, HashMap<String, String>> settings) {
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaveSettingsRequest)) return false;
        if (!super.equals(o)) return false;

        SaveSettingsRequest that = (SaveSettingsRequest) o;

        if (!spaId.equals(that.spaId)) return false;
        if (settings != null ? !settings.equals(that.settings) : that.settings != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        return creationDate != null ? creationDate.equals(that.creationDate) : that.creationDate == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + spaId.hashCode();
        result = 31 * result + (settings != null ? settings.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SaveSettingsRequest{" +
                "spaId='" + spaId + '\'' +
                ", settings=" + settings +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
