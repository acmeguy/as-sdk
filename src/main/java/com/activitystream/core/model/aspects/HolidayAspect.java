package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

/****
 *
 * Internal notes: This aspect is only for internal use and should not be used in integrations
 *
 * ****/

public class HolidayAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_HOLIDAY, HolidayAspect::new);

    public HolidayAspect() {}

    public HolidayAspect withHolidayName(String holidayName) {
        if (StringUtils.isNotBlank(holidayName)) {
            put(ASConstants.FIELD_NAME, holidayName);
        }

        return this;
    }



    @Override
    public void loadFromValue(Object value) {
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }
}
