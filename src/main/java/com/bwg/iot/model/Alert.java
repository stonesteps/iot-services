package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-09T01:15:44.374Z")
public class Alert  {
  
  private Long alertId = null;
  private String name = null;
  public enum SeverityLevelEnum {
     yellow,  red, 
  };
  private SeverityLevelEnum severityLevel = null;
  private String shortDescription = null;
  private String longDescription = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("alertId")
  public Long getAlertId() {
    return alertId;
  }
  public void setAlertId(Long alertId) {
    this.alertId = alertId;
  }

  
  /**
   * Identifying name of alert
   **/
  @ApiModelProperty(value = "Identifying name of alert")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("severityLevel")
  public SeverityLevelEnum getSeverityLevel() {
    return severityLevel;
  }
  public void setSeverityLevel(SeverityLevelEnum severityLevel) {
    this.severityLevel = severityLevel;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("shortDescription")
  public String getShortDescription() {
    return shortDescription;
  }
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  
  /**
   * verbose alert message
   **/
  @ApiModelProperty(value = "verbose alert message")
  @JsonProperty("longDescription")
  public String getLongDescription() {
    return longDescription;
  }
  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Alert alert = (Alert) o;
    return Objects.equals(alertId, alert.alertId) &&
        Objects.equals(name, alert.name) &&
        Objects.equals(severityLevel, alert.severityLevel) &&
        Objects.equals(shortDescription, alert.shortDescription) &&
        Objects.equals(longDescription, alert.longDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertId, name, severityLevel, shortDescription, longDescription);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Alert {\n");
    
    sb.append("  alertId: ").append(alertId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  severityLevel: ").append(severityLevel).append("\n");
    sb.append("  shortDescription: ").append(shortDescription).append("\n");
    sb.append("  longDescription: ").append(longDescription).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
