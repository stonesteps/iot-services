package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.Map;

@Document
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CompoundIndexes({
        @CompoundIndex(name = "measurementreading_spaid_timestamp_idx", def = "{'spaId': 1, 'timestamp': -1}"),
        @CompoundIndex(name = "measurementreading_spaid_type_idx", def = "{'spaId': 1, 'type': 1, 'timestamp': -1}")
})
public class MeasurementReading extends ResourceSupport {

    @Id
    private String _id;

    private String spaId;
    private String ownerId;
    private String dealerId;
    private String oemId;
    private String moteId;
    private String sensorId;

    @Indexed(expireAfterSeconds = 604800)
    private Date timestamp;
    private String type;
    private String unitOfMeasure;
    private Double value;
    private Map<String, String> metadata;
    private String quality;
    private String sensorIdentifier;

    /**
     * The sensor id is the mongodb generated id for the component that represents a sensor
     *
     * @return
     */
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * The sensor identifier is whatever mac-like address the gateway reports on measurements for the sensor,
     * it should be unique per mote only.
     * @return
     */

    public String getSensorIdentifier() {
        return sensorIdentifier;
    }

    public void setSensorIdentifier(String sensorIdentity) {
        this.sensorIdentifier = sensorIdentity;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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

    public String getMoteId() {
        return moteId;
    }

    public void setMoteId(String moteId) {
        this.moteId = moteId;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
