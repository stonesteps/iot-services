package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;

/**
 * Created by triton on 2/16/16.
 */
@Projection(name="liteSpa", types = { Spa.class })
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public interface LiteSpa {

    String get_id();
    String getSerialNumber();
    String getProductName();
    String getModel();
    String getDealerId();
    String getOemId();
    Date getManufacturedDate();

    @Value("#{target.owner?.toUserLite()}")
    User getOwner();

    boolean isOnline();

    List<Alert> getAlerts();
}

