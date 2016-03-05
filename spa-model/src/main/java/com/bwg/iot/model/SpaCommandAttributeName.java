package com.bwg.iot.model;

/**
 * Contains constants representing spa command attribute names that are kept in values map.
 */
public final class SpaCommandAttributeName {

    //Keep these in sync with SpaCommandAttribName enum in bwg.proto or remove here and use spa-gateway-idl
    public static final String DESIRED_TEMP = "desiredTemp";
    public static final String DESIRED_STATE = "desiredState";
    public static final String PORT = "port";

    private SpaCommandAttributeName() {
        // cponstants class
    }
}
