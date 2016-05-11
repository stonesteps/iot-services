package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class BuildSpaRequest extends ResourceSupport {

    private Spa spa;
    private List<Component> components;

    public Spa getSpa() {
        return spa;
    }

    public void setSpa(Spa spa) {
        this.spa = spa;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BuildSpaRequest that = (BuildSpaRequest) o;

        if (!spa.equals(that.spa)) return false;
        return components.equals(that.components);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + spa.hashCode();
        result = 31 * result + components.hashCode();
        return result;
    }
}
