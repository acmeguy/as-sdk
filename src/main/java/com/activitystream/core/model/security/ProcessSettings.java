package com.activitystream.core.model.security;

public class ProcessSettings {

    protected boolean enrich = false;
    protected boolean analyse = false;
    protected boolean statistics = false;
    protected boolean store = false;
    protected boolean index = false;
    protected boolean active = false;
    protected boolean broadcast = false;

    protected boolean returnLoop = false;
    protected int importance = 0;
    protected int retention = 0;

    protected ProcessSettings() {
    }

    public boolean doCognitiveAnalysis() {
        return this.analyse;
    }

    public boolean doStatistics() {
        return this.statistics;
    }

    public boolean doStoreInHistorical() {
        return this.store;
    }

    public boolean doIndex() {
        return this.index;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean doEnrichment() {
        return this.enrich;
    }

    public boolean isBroadcast() {
        return this.broadcast;
    }

    public boolean isReturned() {
        return this.returnLoop;
    }

    public int defaultImportance() {
        return this.importance;
    }

    public int dataRetention() {
        return this.retention;
    }

    @Override
    public String toString() {
        return "ProcessSettings{" +
                "enrich=" + enrich +
                ", analyse=" + analyse +
                ", statistics=" + statistics +
                ", store=" + store +
                ", index=" + index +
                ", active=" + active +
                ", broadcast=" + broadcast +
                ", returnLoop=" + returnLoop +
                ", importance=" + importance +
                ", retention=" + retention +
                '}';
    }
}
