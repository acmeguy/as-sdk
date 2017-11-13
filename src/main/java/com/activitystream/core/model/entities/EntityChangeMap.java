package com.activitystream.core.model.entities;

import java.util.LinkedHashMap;
import java.util.Set;

public class EntityChangeMap extends LinkedHashMap<String, Set> {

    public enum ACTION {
        IGNORE,
        PROCESS,
        IMMEDIATE
    }

    private String aspect = null;
    private ACTION triggersReIndexing = ACTION.IGNORE;
    private ACTION triggersTimeSeriesUpdate = ACTION.IGNORE;

    public EntityChangeMap(String aspect, ACTION triggersReIndexing, ACTION triggersTimeSeriesUpdate) {
        super();
        this.setAspect(aspect);
        this.setTriggersReIndexing(triggersReIndexing);
        this.setTriggersTimeSeriesUpdate(triggersTimeSeriesUpdate);
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public ACTION getTriggersReIndexing() {
        return triggersReIndexing;
    }

    public void setTriggersReIndexing(ACTION triggersReIndexing) {
        this.triggersReIndexing = triggersReIndexing;
    }

    public ACTION getTriggersTimeSeriesUpdate() {
        return triggersTimeSeriesUpdate;
    }

    public void setTriggersTimeSeriesUpdate(ACTION triggersTimeSeriesUpdate) {
        this.triggersTimeSeriesUpdate = triggersTimeSeriesUpdate;
    }
}
