package com.activitystream.core.model.aspects;

import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.sdk.ASConstants;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_EMAIL, EmailAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(EmailAspect.class);

    public EmailAspect() {
    }

    public EmailAspect(String email) {
        withEmail(email);
    }

    @Override
    public void loadFromValue(Object value) {
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    public EmailAspect withEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            put(ASConstants.FIELD_ADDRESS, email);
        }

        return this;
    }

    public EmailAspect withEmailAnonymized() {
        put(ASConstants.FIELD_IS_ANONYMIZED, true);
        return this;
    }

    public String getEmail() {
        return (String) get(ASConstants.FIELD_ADDRESS);
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
            case ASConstants.FIELD_ADDRESS:
                value = validator().processLowerCaseString(theKey, value, true);
                break;
            case ASConstants.FIELD_IS_ANONYMIZED:
                value = validator().processBoolean(theKey, value, true);
        }

        return super.put(theKey, value);
    }
}
