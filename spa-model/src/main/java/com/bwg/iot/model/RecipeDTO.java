package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.*;


@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class RecipeDTO extends ResourceSupport {


    @Id
    private String _id;
    private String spaId;
    private HashMap<String, HashMap<String, String>> settings;
    private String name;
//    private Schedule schedule;
    private String notes;
    private Date creationDate;

    public RecipeDTO() {
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

    public HashMap<String, HashMap<String, String>>  getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, HashMap<String, String>>  settings) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeDTO)) return false;
        if (!super.equals(o)) return false;

        RecipeDTO recipe = (RecipeDTO) o;

        if (_id != null ? !_id.equals(recipe._id) : recipe._id != null) return false;
        if (!spaId.equals(recipe.spaId)) return false;
        if (settings != null ? !settings.equals(recipe.settings) : recipe.settings != null) return false;
        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if (notes != null ? !notes.equals(recipe.notes) : recipe.notes != null) return false;
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

    static public Recipe toRecipe(RecipeDTO dto) throws IllegalArgumentException {
        Recipe recipe = new Recipe();
        recipe.set_id(dto.get_id());
        recipe.setName(dto.getName());
        recipe.setSpaId(dto.getSpaId());
        recipe.setNotes(dto.getNotes());
        recipe.setCreationDate(dto.getCreationDate());

        HashMap<String, HashMap<String, String>> dtoSettings = dto.getSettings();
        List<SpaCommand> spaCommands = new ArrayList<>();
        if (dtoSettings != null) {
            for ( Map.Entry<String, HashMap<String,String>> entry : dtoSettings.entrySet() ) {
                String incomingKey = entry.getKey();
                int suffixIndex = incomingKey.indexOf("_");
                if (suffixIndex > 0) {
                    incomingKey = incomingKey.substring(0, suffixIndex);
                }
                if (Component.ComponentType.PUMP.name().equalsIgnoreCase(incomingKey) ||
                        Component.ComponentType.LIGHT.name().equalsIgnoreCase(incomingKey)) {
                    incomingKey = incomingKey + "S";
                }
                SpaCommand.RequestType key = SpaCommand.RequestType.valueOf(incomingKey);
                SpaCommand spaCommand = new SpaCommand();
                spaCommand.setRequestTypeId(key.getCode());
                spaCommand.setValues(entry.getValue());
                spaCommands.add(spaCommand);
            }
            recipe.setSettings(spaCommands);
        }

        return recipe;
    }

    static public RecipeDTO fromRecipe(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.set_id(recipe.get_id());
        dto.setName(recipe.getName());
        dto.setSpaId(recipe.getSpaId());
        dto.setNotes(recipe.getNotes());
        dto.setCreationDate(recipe.getCreationDate());

        List<SpaCommand> spaCommands = recipe.getSettings();
        HashMap<String, HashMap<String, String>> dtoSettings = new HashMap<String, HashMap<String, String>>();

        if (spaCommands != null) {
            for (SpaCommand command : spaCommands) {
               SpaCommand.RequestType requestType =
                       SpaCommand.RequestType.getInstanceFromCodeValue(command.getRequestTypeId());
               String key = requestType.name();
               if (SpaCommand.RequestType.PUMPS.name().equalsIgnoreCase(key) ||
                       SpaCommand.RequestType.LIGHTS.name().equalsIgnoreCase(key)) {
                   key = key.substring(0,key.length()-1);
               }
               key = key + "_" + command.getValues().get("deviceNumber");
               dtoSettings.put(key, command.getValues());
            }
            dto.setSettings(dtoSettings);
        }

        return dto;
    }


}
