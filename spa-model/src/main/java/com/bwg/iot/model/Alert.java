package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;


public class Alert  {

  public enum SeverityLevelEnum {
    yellow,  red,
  };

  @Id
  private String alertId = null;
  private String name = null;
  private String severityLevel = null;
  private String shortDescription = null;
  private String longDescription = null;
  private String component;
  private String spaId;
  private String creationDate;

  
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


  public String getSeverityLevel() {
    return severityLevel;
  }
  public void setSeverityLevel(String severityLevel) {
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


  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }


  public String getSpaId() {
    return spaId;
  }

  public void setSpaId(String spaId) {
    this.spaId = spaId;
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
        Objects.equals(spaId, alert.spaId) &&
        Objects.equals(severityLevel, alert.severityLevel) &&
        Objects.equals(shortDescription, alert.shortDescription) &&
        Objects.equals(longDescription, alert.longDescription) &&
        Objects.equals(component, alert.component) &&
        Objects.equals(creationDate, alert.creationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertId, name, severityLevel, shortDescription, longDescription, spaId, creationDate);
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
      sb.append("  spaId:").append(spaId).append("\n");
      sb.append("  component").append(component).append("\n");
      sb.append("  creationDate").append(creationDate).append("\n");
      sb.append("}\n");
      return sb.toString();
  }
}
