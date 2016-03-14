package com.bwg.iot.model;

import org.springframework.data.annotation.Id;

import java.util.*;


public class SpaCommand {

    public enum RequestType {
        PUMPS(1), LIGHTS(2), BLOWER(3), MISTER(4), FILTER(5), DIAG_REPORT(6), PANEL(7), HEATER(8), OZONE(9),
        MICROSILK(10), AUX(11), FILTER_CYCLE(12);

        private int code;
        private RequestType(int c){
            code = c;
        }
        public int getCode() {
            return code;
        }
    }

    public enum ValueKeyName {
        DESIRED_TEMP("desiredTemp"), DESIRED_STATE("desiredState"), PORT("port");

        private final String keyName;
        private ValueKeyName(final String keyName) { this.keyName = keyName; }
        public String getKeyName() { return this.keyName; }
    }

    @Id
    private String _id;
    private String spaId;
    private Integer requestTypeId;
    private String originatorId;
    private String sentTimestamp;
    private String processedTimestamp;
    private ProcessedResult processedResult;
    private String ackTimestamp;
    private String ackResponseCode;
    private HashMap<String, String> values;

    public SpaCommand(){}


    public String get_id() {
    return _id;
    }
    public void set_id(String id) {
    this._id = id;
    }

    public String getSpaId() {
        return spaId;
    }

    public void setSpaId(String spaId) {
        this.spaId = spaId;
    }

    public Integer getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(Integer requestTypeId) {
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

    public ProcessedResult getProcessedResult() { return processedResult; }

    public void setProcessedResult(ProcessedResult processedResult) { this.processedResult = processedResult; }

    public String getAckTimestamp() {
        return ackTimestamp;
    }

    public void setAckTimestamp(String ackTimestamp) {
        this.ackTimestamp = ackTimestamp;
    }

    public String getAckResponseCode() { return ackResponseCode; }

    public void setAckResponseCode(String ackResponseCode) { this.ackResponseCode = ackResponseCode; }

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
        return Objects.equals(_id, spaCommand._id);
    }

    @Override
    public int hashCode() {
    return Objects.hash(_id, spaId, originatorId, requestTypeId);
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class SpaCommand {\n");

        sb.append("  id: ").append(_id).append("\n");
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
