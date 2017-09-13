package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.interfaces.AspectInterface;
import com.activitystream.model.validation.InvalidPropertyContentError;
import com.activitystream.model.interfaces.LinkedElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TrafficSourceAspect extends AbstractListAspect<TrafficSource> implements LinkedElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_TRAFFIC_SOURCES, TrafficSourceAspect::new,
            AspectType.MergeStrategy.REPLACE) {
    };

    protected static final Logger logger = LoggerFactory.getLogger(TrafficSourceAspect.class);

    public TrafficSourceAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        if (value instanceof List) {
            for (Object element : (List<?>) value) {
                add(new TrafficSource((Map) element, getRoot()));
            }
        } else if (value instanceof Map) {
            add(new TrafficSource((Map) value, getRoot()));
        } else {
            addProblem(new InvalidPropertyContentError("Traffic source can only be constructed from a list or a map."));
        }
    }

    /************
     * Utility functions
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {

    }

    public static TrafficSourceAspect trafficSources() {
        return new TrafficSourceAspect();
    }

    public AspectInterface addTrafficSource(TrafficSource trafficSource) {
        add(trafficSource);
        return this;
    }
}
