package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;

@Document
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class DashboardInfo extends ResourceSupport {

  private Map<String, Long> alertCounts;
  private Map<String, Long> spaCounts;
  private Map<String, Long> messageCounts;

  public Map<String, Long> getAlertCounts() {
    return alertCounts;
  }

  public void setAlertCounts(Map<String, Long> alertCounts) {
    this.alertCounts = alertCounts;
  }

  public Map<String, Long> getSpaCounts() {
    return spaCounts;
  }

  public void setSpaCounts(Map<String, Long> spaCounts) {
    this.spaCounts = spaCounts;
  }

  public Map<String, Long> getMessageCounts() {
    return messageCounts;
  }

  public void setMessageCounts(Map<String, Long> messageCounts) {
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
