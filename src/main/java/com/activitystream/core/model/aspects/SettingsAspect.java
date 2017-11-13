package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.validation.InvalidPropertyContentError;

@SuppressWarnings("unchecked")
public class SettingsAspect extends AbstractMapAspect {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_SETTINGS, SettingsAspect::new, AspectType.MergeStrategy.REPLACE);

    public SettingsAspect() {
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
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        if (value == null)
            return null;

        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    public void verifyType(String key, Class<?> valueType) {
        Object value = get(key);
        if (value != null && !valueType.isInstance(value))
            validator().addProblem(new InvalidPropertyContentError(
                    "Value for " + key + " has wrong type " + value.getClass().getName() + ", expected " + valueType.getName()));
    }
}
