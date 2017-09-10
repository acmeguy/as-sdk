package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.AnalyticsElement;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.validation.UnsupportedAspectError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings("unchecked")
public class TagsAspect extends AbstractListAspect<String> implements AnalyticsElement {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_TAGS, TagsAspect::new, null);

    private static final Logger logger = LoggerFactory.getLogger(TagsAspect.class);

    public TagsAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        if (value != null) {
            if (value instanceof List) {
                try {
                    for (String tag : (List<String>) value) ;
                } catch (Exception e) {
                    this.addProblem(new UnsupportedAspectError("Tags should contain a list of strings, not: " + value + ". " + e));
                }
                addAll((List) value);
            } else if (value instanceof String) {
                Collections.addAll(this, ((String) value).split(" "));
            } else {
                this.addProblem(new UnsupportedAspectError("Tags should contain a list of strings, not: " + value));
                return;
            }
        }
        verify();
    }

    /************
     * Utility functions
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************  Analytical functions  ************/

    @Override
    public void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth) {

    }

    @Override
    public void addTimeSeriesDimensions(TimeSeriesEntry entry) {
        entry.put("tags", this);
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {

    }

}
