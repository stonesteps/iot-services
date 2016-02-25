package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.*;


public class SpaCommand {

    @Id
    private String id = null;
    private String spaId;
    private String requestTypeId;
    private String originatorId;
    private String sentTimestamp;
    private String processedTimestamp;
    private String ackTimestamp;
    private HashMap<String, String> values;

    public SpaCommand(){}


    public String getId() {
    return id;
    }
    public void setId(String id) {
    this.id = id;
    }

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(String sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public String getProcessedTimestamp() {
        return processedTimestamp;
    }

    public void setProcessedTimestamp(String processedTimestamp) {
        this.processedTimestamp = processedTimestamp;
    }

    public String getAckTimestamp() {
        return ackTimestamp;
    }

    public void setAckTimestamp(String ackTimestamp) {
        this.ackTimestamp = ackTimestamp;
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
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
    return Objects.hash(id, spaId, originatorId, requestTypeId);
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class SpaCommand {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  spaId: " ).append(spaId).append("\n");
        sb.append("  requestTypeId: " ).append(requestTypeId).append("\n");
        sb.append("  originatorId: " ).append(originatorId).append("\n");
        sb.append("  sentTimestamp: " ).append(sentTimestamp).append("\n");
        sb.append("  processedTimestamp: " ).append(processedTimestamp).append("\n");
        sb.append("  ackTimestamp: " ).append(ackTimestamp).append("\n");
        sb.append("  values: " ).append(values).append("\n");
        sb.append("}\n");
    return sb.toString();
    }
}