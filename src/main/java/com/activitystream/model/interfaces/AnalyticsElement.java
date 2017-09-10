package com.activitystream.model.interfaces;

import com.activitystream.model.analytics.TimeSeriesEntry;

public interface AnalyticsElement extends LinkedElement {

    /************  Analytics  ************/

    /**
     * Provides aspect and element specific dimensions for the given time series entry
     */
    void addTimeSeriesDimensions(TimeSeriesEntry entry);

    /**
     * Provides aspect and element specific dimensions for the given time series entry
     */
    void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth);

}
