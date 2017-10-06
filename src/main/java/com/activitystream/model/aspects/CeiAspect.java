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
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public Float getEngagement() {
        return (Float) get(ASConstants.FIELD_ENGAGEMENT);
    }

    public void setEngagement(Float engagement) {
        if (engagement != null) put(ASConstants.FIELD_ENGAGEMENT, engagement);
        else remove(ASConstants.FIELD_ENGAGEMENT);
    }

    public CeiAspect withEngagement(Number engagement) {
        setEngagement((engagement != null) ? engagement.floatValue() : null);
        return this;
    }

    public Float getHappiness() {
        return (Float) get(ASConstants.FIELD_HAPPINESS);
    }

    public void setHappiness(Float happiness) {
        if (happiness != null) put(ASConstants.FIELD_HAPPINESS, happiness);
        else remove(ASConstants.FIELD_HAPPINESS);
    }

    public CeiAspect withHappiness(Number happiness) {
        setHappiness((happiness != null) ? happiness.floatValue() : null);
        return this;
    }

    public Float getCare() {
        return (Float) get(ASConstants.FIELD_CARE);
    }

    public void setCare(Float care) {
        if (care != null) put(ASConstants.FIELD_CARE, care);
        else remove(ASConstants.FIELD_CARE);
    }

    public CeiAspect withCare(Number care) {
        setCare((care != null) ? care.floatValue() : null);
        return this;
    }

    public Float getIntent() {
        return (Float) get(ASConstants.FIELD_INTENT);
    }

    public void setIntent(Float intent) {
        if (intent != null) put(ASConstants.FIELD_INTENT, intent);
        else remove(ASConstants.FIELD_INTENT);
    }

    public CeiAspect withIntent(Number intent) {
        setIntent((intent != null) ? intent.floatValue() : null);
        return this;
    }

    public Float getRating() {
        return (Float) get(ASConstants.FIELD_RATING);
    }

    public void setRating(Float rating) {
        if (rating != null) put(ASConstants.FIELD_RATING, rating);
        else remove(ASConstants.FIELD_RATING);
    }

    public CeiAspect withRating(Number rating) {
        setRating((rating != null) ? rating.floatValue() : null);
        return this;
    }

    public Float getDuration() {
        return (Float) get(ASConstants.FIELD_DURATION);
    }

    public void setDuration(Float duration) {
        if (duration != null) put(ASConstants.FIELD_DURATION, duration);
        else remove(ASConstants.FIELD_DURATION);
    }

    public CeiAspect withDuration(Float duration) {
        setDuration(duration);
        return this;
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

    public static CeiAspect cei() {
        return new CeiAspect();
    }
}
