package com.bwg.iot.model;

import java.util.HashMap;
import java.util.Map;

public enum WifiConnectionHealth {
    DISCONNECTED(0), WEAK(1), AVG(2), STRONG(3), UNKONWN(4);

    private final int code;
    private static final Map<Integer, WifiConnectionHealth> cache = new HashMap<>();

    static {
        for (final WifiConnectionHealth health : WifiConnectionHealth.values()) {
            cache.put(Integer.valueOf(health.getCode()), health);
        }
    }

    private WifiConnectionHealth(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WifiConnectionHealth toEnum(final int code) {
        return cache.get(Integer.valueOf(code));
    }
}
