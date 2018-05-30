package com.activitystream.core.model.aspects;

import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.sdk.ASConstants;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/****
 *
 * Internal notes: This aspect is only for internal use and should not be used in integrations
 *
 * ****/

public class HolidayAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_HOLIDAY, HolidayAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(HolidayAspect.class);

    public HolidayAspect() {
    }

    public HolidayAspect withHolidayName(String holidayName) {
        if (StringUtils.isNotBlank(holidayName)) {
            put(ASConstants.FIELD_NAME, holidayName);
        }

        return this;
    }

    public HolidayAspect withHolidayDate(String holidayDate) {
        if (StringUtils.isNotBlank(holidayDate)) {
            put(ASConstants.FIELD_DATE, holidayDate);
        }

        return this;
    }

    public HolidayAspect withPublicHoliday() {
        put(ASConstants.FIELD_IS_PUBLIC, true);

        return this;
    }

    public HolidayAspect withOffsetDays(Integer offsetDays) {
        put(ASConstants.FIELD_OFFSET_DAYS, offsetDays);

        return this;
    }

    public String getHolidayName() {
        if (this.containsKey(ASConstants.FIELD_NAME)) {
            return (String) get(ASConstants.FIELD_NAME);
        }

        return null;
    }

    public String getHolidayDate() {
        if (this.containsKey(ASConstants.FIELD_DATE)) {
            return (String) get(ASConstants.FIELD_DATE);
        }

        return null;
    }


    public Boolean isPublicHoliday() {
        if (this.containsKey(ASConstants.FIELD_IS_PUBLIC)) {
            return (Boolean) get(ASConstants.FIELD_IS_PUBLIC);
        }

        return false;
    }

    public Integer getOffsetDays() {
        if (this.containsKey(ASConstants.FIELD_OFFSET_DAYS)) {
            return (Integer) get(ASConstants.FIELD_OFFSET_DAYS);
        }

        return null;
    }

    @Override
    public void loadFromValue(Object value) {
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    @Override
    public Object put(Object key, Object value) {
        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();

        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_NAME:
            case ASConstants.FIELD_DATE:
                value = validator().processString(theKey, value, true);
                break;
            case ASConstants.FIELD_IS_PUBLIC:
                value = validator().processBoolean(theKey, value, true);
                break;
            case ASConstants.FIELD_OFFSET_DAYS:
                value = validator().processInteger(theKey, value, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                break;
        }

        return super.put(key, value);
    }
}
