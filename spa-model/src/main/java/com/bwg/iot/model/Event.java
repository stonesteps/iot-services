package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@CompoundIndexes({@CompoundIndex(name = "event_spaid_eventoccuredtimestamp_idx", def = "{'spaId': 1, 'eventOccuredTimestamp': -1}")})
public class Event {

    @Id
    private String _id;

    private String spaId;
    private String ownerId;
    private String dealerId;
    private String oemId;

    private String eventType;

    @Indexed(expireAfterSeconds = 604800)
    private Date eventOccuredTimestamp;
    private Date eventReceivedTimestamp;
    private Map<String, String> metadata;
    private Map<String, String> oidData;
    private String description;

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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getEventOccuredTimestamp() {
        return eventOccuredTimestamp;
    }

    public void setEventOccuredTimestamp(Date eventOccuredTimestamp) {
        this.eventOccuredTimestamp = eventOccuredTimestamp;
    }

    public Date getEventReceivedTimestamp() {
        return eventReceivedTimestamp;
    }

    public void setEventReceivedTimestamp(Date eventReceivedTimestamp) {
        this.eventReceivedTimestamp = eventReceivedTimestamp;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Map<String, String> getOidData() {
        return oidData;
    }

    public void setOidData(Map<String, String> oidData) {
        this.oidData = oidData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
