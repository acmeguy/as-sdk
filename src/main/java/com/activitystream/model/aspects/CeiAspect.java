package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CeiAspect extends AbstractMapAspect {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_CEI, CeiAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(CeiAspect.class);

    public CeiAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
    }

    /************
     * Utility functions
     ************/

    /*
    @Override
    protected Map<String, Object> fromElement(SavableElement element, StreamItemAccessPolicy accessPolicy) {
        return Collections.emptyMap();
    }
    */

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public Float getEngagement() {
        return (Float) get(ASConstants.FIELD_ENDS);
    }

    public Float getHappiness() {
        return (Float) get(ASConstants.FIELD_HAPPINESS);
    }

    public Float getCare() {
        return (Float) get(ASConstants.FIELD_CARE);
    }

    public Float getIntent() {
        return (Float) get(ASConstants.FIELD_INTENT);
    }

    public Float getRating() {
        return (Float) get(ASConstants.FIELD_RATING);
    }

    public Float getDuration() {
        return (Float) get(ASConstants.FIELD_DURATION);
    }

    /************ Enrichment & Analytics ************/

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_ENGAGEMENT:
            case ASConstants.FIELD_HAPPINESS:
            case ASConstants.FIELD_CARE:
            case ASConstants.FIELD_RATING:
            case ASConstants.FIELD_INTENT:
                value = validator().processFloat(theKey, value, false, null, null);
                break;
            case ASConstants.FIELD_DURATION:
                value = validator().processLong(theKey, value, false, null, null);
                break;
            default:
                this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Customer Experience Aspect"));
        }
        return super.put(theKey, value);
    }


    @Override
    public void verify() {

    }

}
