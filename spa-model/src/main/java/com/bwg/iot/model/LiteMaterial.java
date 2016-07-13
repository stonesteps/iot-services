package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

/**
 * Created by triton on 5/9/16.
 */
@Projection(name="liteMaterial", types = { Material.class })
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public interface LiteMaterial {

    String get_id();
    String getMaterialType();
    String getBrandName();
    String getSku();
    String getAlternateSku();
    String getDescription();
    int getWarrantyDays();
    Date getUploadDate();

}

