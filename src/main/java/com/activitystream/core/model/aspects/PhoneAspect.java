package com.activitystream.core.model.aspects;

import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.sdk.ASConstants;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/***
 *
 * Note: This aspect should only be used if the entity archetype is "Phone".
 *
 * ***/

public class PhoneAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_PHONE, PhoneAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(PhoneAspect.class);

    public PhoneAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    public PhoneAspect withCountryCode(String countryCode) {
        if (StringUtils.isNotBlank(countryCode)) {
            put(ASConstants.FIELD_COUNTRY_CODE, countryCode);
        }

        return this;
    }

    public PhoneAspect withAreaCode(String areaCode) {
        if (StringUtils.isNotBlank(areaCode)) {
            put(ASConstants.FIELD_AREA_CODE, areaCode);
        }

        return this;
    }

    public PhoneAspect withPhoneNumber(String phoneNumber) {
        if (StringUtils.isNotBlank(phoneNumber)) {
            put(ASConstants.FIELD_NUMBER, phoneNumber);
        }

        return this;
    }

    public PhoneAspect withPhoneNumberType(String phoneNumberType) {
        if (StringUtils.isNotBlank(phoneNumberType)) {
            put(ASConstants.FIELD_NUMBER_TYPE, phoneNumberType);
        }

        return this;
    }

    public PhoneAspect withPhoneAnonymized() {
        put(ASConstants.FIELD_IS_ANONYMIZED, true);
        return this;
    }

    public String getCountryCode() {
        return (String) get(ASConstants.FIELD_COUNTRY_CODE);
    }

    public String getAreaCode() {
        return (String) get(ASConstants.FIELD_AREA_CODE);
    }

    public String getPhoneNumber() {
        return (String) get(ASConstants.FIELD_NUMBER);
    }

    public String getPhoneNumberType() {
        return (String) get(ASConstants.FIELD_NUMBER_TYPE);
    }

    public boolean isAnonymized() {
        boolean isAnonymized = false;

        try {
            isAnonymized = (boolean) get(ASConstants.FIELD_IS_ANONYMIZED);
        } catch (NullPointerException e) {
            logger.info("Email address isn't anonymized.");
        }

        return isAnonymized;
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
            case ASConstants.FIELD_COUNTRY_CODE:
            case ASConstants.FIELD_AREA_CODE:
            case ASConstants.FIELD_NUMBER:
            case ASConstants.FIELD_NUMBER_TYPE:
                value = validator().processString(theKey, value, false);
            case ASConstants.FIELD_IS_ANONYMIZED:
                value = validator().processBoolean(theKey, value, true);
        }

        return super.put(theKey, value);
    }
}
