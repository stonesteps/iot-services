package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.Objects;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@CompoundIndexes({
        @CompoundIndex(name = "alert_oem_idx", def = "{'oemId': 1, 'severityLevel' : 1, 'creationDate': -1}"),
        @CompoundIndex(name = "alert_dealer_idx", def = "{'dealerId': 1,  'severityLevel' : 1, 'creationDate': -1}"),
        @CompoundIndex(name = "alert_spa_idx", def = "{'spaId': 1,  'severityLevel' : 1, 'creationDate': -1}")
})
public class Alert extends ResourceSupport {

  public enum SeverityLevelEnum {
    NONE, INFO, WARNING, ERROR, SEVERE
  };

  @Id
  private String _id = null;
  private String name = null;
  private String severityLevel = null;
  private String shortDescription = null;
  private String longDescription = null;
  private String component;
  private Integer portNo;
  private String spaId;
  private String oemId;
  private String dealerId;

  @Indexed(expireAfterSeconds = 604800)
  private Date creationDate;
  private Date clearedDate;
  private String clearedByUserId;
  
  public String get_id() {
    return _id;
  }
  public void set_id(String id) {
    this._id = id;
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

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public Integer getPortNo() {
    return portNo;
  }

  public void setPortNo(Integer portNo) {
    this.portNo = portNo;
  }

  public String getOemId() {
    return oemId;
  }

  public void setOemId(String oemId) {
    this.oemId = oemId;
  }

  public String getDealerId() {
    return dealerId;
  }

  public void setDealerId(String dealerId) {
    this.dealerId = dealerId;
  }

  public String getSpaId() {
    return spaId;
  }

  public void setSpaId(String spaId) {
    this.spaId = spaId;
  }

  public Date getClearedDate() {
    return clearedDate;
  }

  public void setClearedDate(Date clearedDate) {
    this.clearedDate = clearedDate;
  }

  public String getClearedByUserId() {
    return clearedByUserId;
  }

  public void setClearedByUserId(String clearedByUserId) {
    this.clearedByUserId = clearedByUserId;
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
    return Objects.equals(_id, alert._id) &&
        Objects.equals(name, alert.name) &&
        Objects.equals(severityLevel, alert.severityLevel) &&
        Objects.equals(spaId, alert.spaId) &&
        Objects.equals(shortDescription, alert.shortDescription) &&
        Objects.equals(longDescription, alert.longDescription) &&
        Objects.equals(component, alert.component) &&
        Objects.equals(portNo, alert.portNo) &&
        Objects.equals(creationDate, alert.creationDate) &&
        Objects.equals(clearedDate, alert.clearedDate) &&
        Objects.equals(clearedByUserId, alert.clearedByUserId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_id, name, severityLevel, shortDescription, longDescription, spaId, component, portNo, creationDate, clearedDate, clearedByUserId);
  }

  @Override
  public String toString()  {
      StringBuilder sb = new StringBuilder();
      sb.append("class Alert {\n");

      sb.append("  _id: ").append(_id).append("\n");
      sb.append("  name: ").append(name).append("\n");
      sb.append("  severityLevel: ").append(severityLevel).append("\n");
      sb.append("  shortDescription: ").append(shortDescription).append("\n");
      sb.append("  longDescription: ").append(longDescription).append("\n");
      sb.append("  spaId:").append(spaId).append("\n");
      sb.append("  component").append(component).append("\n");
      sb.append("  portNo").append(portNo).append("\n");
      sb.append("  creationDate").append(creationDate).append("\n");
      sb.append("  clearedDate").append(clearedDate).append("\n");
      sb.append("  clearedByUserId").append(clearedByUserId).append("\n");
      sb.append("}\n");
      return sb.toString();
  }
}
