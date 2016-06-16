package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;


@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class Recipe extends ResourceSupport {


    @Id
    private String _id;

    @Indexed
    private String spaId;
    private List<SpaCommand> settings;
    private String name;
    private JobSchedule schedule;
    private String notes;
    private Date creationDate;
    private boolean system = false;

    public Recipe() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public List<SpaCommand> getSettings() {
        return settings;
    }

    public void setSettings(List<SpaCommand> settings) {
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

    public JobSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(JobSchedule schedule) {
        this.schedule = schedule;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        if (!super.equals(o)) return false;

        Recipe recipe = (Recipe) o;

        if (_id != null ? !_id.equals(recipe._id) : recipe._id != null) return false;
        if (!spaId.equals(recipe.spaId)) return false;
        if (settings != null ? !settings.equals(recipe.settings) : recipe.settings != null) return false;
        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if (notes != null ? !notes.equals(recipe.notes) : recipe.notes != null) return false;
        if (system != recipe.system) return false;
        return creationDate != null ? creationDate.equals(recipe.creationDate) : recipe.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (_id != null ? _id.hashCode() : 0);
        result = 31 * result + spaId.hashCode();
        result = 31 * result + (settings != null ? settings.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }
}
