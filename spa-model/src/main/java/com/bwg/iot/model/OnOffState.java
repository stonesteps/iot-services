package com.bwg.iot.model;

/**
 * Used in commands and in spa state representation.
 */
public enum OnOffState {
    OFF(0), ON(1);

    private final int code;

    private OnOffState(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
