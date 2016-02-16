package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;


public class Alert  {
  
  @Id
  private String alertId = null;
  private String name = null;
  public enum SeverityLevelEnum {
     yellow,  red, 
  };
  private SeverityLevelEnum severityLevel = null;
  private String shortDescription = null;
  private String longDescription = null;

  
  public String getAlertId() {
    return alertId;
  }
  public void setAlertId(String alertId) {
    this.alertId = alertId;
  }


  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }


  public SeverityLevelEnum getSeverityLevel() {
    return severityLevel;
  }
  public void setSeverityLevel(SeverityLevelEnum severityLevel) {
    this.severityLevel = severityLevel;
  }


  public String getShortDescription() {
    return shortDescription;
  }
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }


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
