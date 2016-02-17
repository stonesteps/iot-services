package com.bwg.iot.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * Created by triton on 2/16/16.
 */
//@Projection(name="dealerView", types = { Spa.class })
public interface LightSpa {

    String getSerialNumber();
    String getProductName();
    String getModel();
    String getDealerId();



    String getOwner();

    List<Alert> getAlerts();
}

