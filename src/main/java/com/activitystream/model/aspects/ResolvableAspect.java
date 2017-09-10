package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;

public class ResolvableAspect extends AbstractMapAspect {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_RESOLVABLE, ResolvableAspect::new);

    public ResolvableAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        if (value instanceof String) {
            put("external_id", value);
        }
    }

    /************
     * Utility functions
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public String getExternalId() {
        return (String) get(ASConstants.FIELD_EXTERNAL_ID);
    }

    public String getBatchId() {
        return (String) get(ASConstants.FIELD_BATCH_ID);
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

        switch (theKey) {
            case ASConstants.FIELD_EXTERNAL_ID:
                value = validator().processString(theKey, value, true);
                break;
            case ASConstants.FIELD_BATCH_ID:
                value = validator().processString(theKey, value, false);
                break;
            default:
                this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Resolvable Aspect"));
        }
        return super.put(key, value);
    }

    @Override
    public void verify() {

    }

}
