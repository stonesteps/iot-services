package com.bwg.iot.model;

/**
 * Contains constants representing spa command attribute names that are kept in values map.
 */
public final class SpaCommandAttributeName {

    //TODO - Marek, remove this class, use the Bwg.SpaCommandAttribName enum from bwg.proto instead
    public static final String DESIRED_TEMP = "desiredTemp";
    public static final String DESIRED_STATE = "desiredState";
    public static final String PORT = "port";

    private SpaCommandAttributeName() {
        // cponstants class
    }
}
