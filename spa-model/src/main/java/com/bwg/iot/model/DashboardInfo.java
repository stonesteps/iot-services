package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class DashboardInfo extends ResourceSupport {

  private Map<String, Integer> alertCounts;
  private Map<String, Integer> spaCounts;
  private Map<String, Integer> messageCounts;

  public Map<String, Integer> getAlertCounts() {
    return alertCounts;
  }

  public void setAlertCounts(Map<String, Integer> alertCounts) {
    this.alertCounts = alertCounts;
  }

  public Map<String, Integer> getSpaCounts() {
    return spaCounts;
  }

  public void setSpaCounts(Map<String, Integer> spaCounts) {
    this.spaCounts = spaCounts;
  }

  public Map<String, Integer> getMessageCounts() {
    return messageCounts;
  }

  public void setMessageCounts(Map<String, Integer> messageCounts) {
    this.messageCounts = messageCounts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    DashboardInfo that = (DashboardInfo) o;

    if (alertCounts != null ? !alertCounts.equals(that.alertCounts) : that.alertCounts != null) return false;
    if (spaCounts != null ? !spaCounts.equals(that.spaCounts) : that.spaCounts != null) return false;
    return messageCounts != null ? messageCounts.equals(that.messageCounts) : that.messageCounts == null;

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (alertCounts != null ? alertCounts.hashCode() : 0);
    result = 31 * result + (spaCounts != null ? spaCounts.hashCode() : 0);
    result = 31 * result + (messageCounts != null ? messageCounts.hashCode() : 0);
    return result;
  }
}
