package com.bwg.iot.model;

import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * Created by triton on 2/16/16.
 */
//@Projection(name="dealerView", types = { Owner.class })
public interface LightOwner {

    String getCustomerName();
}
