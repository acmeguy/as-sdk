package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.entities.EntityChangeMap;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.validation.UnsupportedAspectError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LocaleAspect extends AbstractMapAspect {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_LOCALE, LocaleAspect::new, AspectType.MergeStrategy.REPLACE);

    protected static final Logger logger = LoggerFactory.getLogger(LocaleAspect.class);

    public LocaleAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        if (value != null) {
            if (value instanceof String) {
                put(ASConstants.FIELD_LABEL, value);
            } else {
                super.loadFromValue(value);
            }
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


    public Locale getLocale() {
        return (Locale) get(ASConstants.FIELD_LOCALE);
    }

    public Currency getCurrency() {
        return (Currency) get(ASConstants.FIELD_CURRENCY);
    }

    public String getCountryCode() {
        return (String) get(ASConstants.FIELD_COUNTRY_CODE);
    }

    public TimeZone getTimezone() {
        return (TimeZone) get(ASConstants.FIELD_TIMEZONE);
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
            case ASConstants.FIELD_COUNTRY_CODE:
                value = validator().processString(theKey, value, false, 2, 2, false);
                break;
            case ASConstants.FIELD_LOCALE:
                value = validator().processLocale(theKey, value, false);
                break;
            case ASConstants.FIELD_CURRENCY:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_TIMEZONE:
                value = validator().processTimezone(theKey, value, false);
                break;
            default:
                this.addProblem(new UnsupportedAspectError("The " + theKey + " property is not supported for the Locale Aspect"));
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    @Override
    protected void handleChanges(Map<String, Object> oldValues, Map<String, Object> newValues) {
        registerChanges(oldValues, newValues, EntityChangeMap.ACTION.PROCESS, EntityChangeMap.ACTION.IGNORE);
    }

    public static LocaleAspect locale() {
        return new LocaleAspect();
    }
}
