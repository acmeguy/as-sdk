package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.interfaces.DynamicAspect;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.InvalidPropertyContentError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;

public class MetricsAspect extends AbstractMapAspect implements DynamicAspect {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_METRICS, MetricsAspect::new, AspectType.MergeStrategy.REPLACE);

    protected static final Logger logger = LoggerFactory.getLogger(MetricsAspect.class);

    public MetricsAspect() {
    }

    /************
     * Utility functions
     ************/

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        if (value instanceof List || value instanceof Map) {
            addProblem(new InvalidPropertyContentError("Map values are not supported as Metric values"));
            logger.warn("Map value in dimensions: " + key + " = " + value);
            return null;
        } else if (value instanceof String && isNumeric((String) value)) {
            try {
                value = Double.valueOf((String) value);
            } catch (NumberFormatException e) {
                try {
                    value = Long.valueOf((String) value).doubleValue();
                } catch (NumberFormatException r) {
                }
            }
        } else if (value instanceof Integer) {
            value = ((Integer) value).doubleValue();
        }

        if (value == null || value.toString().isEmpty()) return null; //prune away empty values

        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    @Override
    public String getDocumentClassName() {
        return ASConstants.EM_METRICS;
    }

    @Override
    public Collection<String> getFieldNames() {
        return keySet();
    }

    @Override
    public Object getValueType() {

        return "DOUBLE";
        //return OType.DOUBLE;

    }
}
