package com.bwg.iot.model;

/**
 * Created by holow on 3/22/2016.
 */
public class SetupParams {

    private int lowRangeLow;
    private int lowRangeHigh;
    private int highRangeLow;
    private int highRangeHigh;
    private boolean gfciEnabled;
    private boolean drainModeEnabled;
    private long lastUpdateTimestamp;

    public int getLowRangeLow() {
        return lowRangeLow;
    }

    public void setLowRangeLow(int lowRangeLow) {
        this.lowRangeLow = lowRangeLow;
    }

    public int getLowRangeHigh() {
        return lowRangeHigh;
    }

    public void setLowRangeHigh(int lowRangeHigh) {
        this.lowRangeHigh = lowRangeHigh;
    }

    public int getHighRangeLow() {
        return highRangeLow;
    }

    public void setHighRangeLow(int highRangeLow) {
        this.highRangeLow = highRangeLow;
    }

    public int getHighRangeHigh() {
        return highRangeHigh;
    }

    public void setHighRangeHigh(int highRangeHigh) {
        this.highRangeHigh = highRangeHigh;
    }

    public boolean isGfciEnabled() {
        return gfciEnabled;
    }

    public void setGfciEnabled(boolean gfciEnabled) {
        this.gfciEnabled = gfciEnabled;
    }

    public boolean isDrainModeEnabled() {
        return drainModeEnabled;
    }

    public void setDrainModeEnabled(boolean drainModeEnabled) {
        this.drainModeEnabled = drainModeEnabled;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }
}
