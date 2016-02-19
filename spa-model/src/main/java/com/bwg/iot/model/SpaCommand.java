package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class SpaCommand {

    @Id
    private String id = null;

    public SpaCommand(){}


    public String getId() {
    return id;
    }
    public void setId(String id) {
    this.id = id;
    }



    @Override
    public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    SpaCommand spaCommand = (SpaCommand) o;
    return Objects.equals(id, spaCommand.id);
    }

    @Override
    public int hashCode() {
    return Objects.hash(id);
    }

    @Override
    public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpaCommand {\n");

    sb.append("  id: ").append(id).append("\n");
    sb.append("}\n");
    return sb.toString();
    }
}
