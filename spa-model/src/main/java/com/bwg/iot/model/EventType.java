package com.bwg.iot.model;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    NOTIFICATION(1), ALERT(2), MEASUREMENT(3), REQEUST(4);

    private final int code;
    private static final Map<Integer, EventType> cache = new HashMap<>();

    static {
        for (final EventType eventType : EventType.values()) {
            cache.put(Integer.valueOf(eventType.getCode()), eventType);
        }
    }

    private EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static EventType toEnum(final int code) {
        return cache.get(Integer.valueOf(code));
    }

}
