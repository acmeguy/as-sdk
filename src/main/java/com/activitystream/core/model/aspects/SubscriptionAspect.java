package com.activitystream.core.model.aspects;

import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.sdk.ASConstants;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_SUBSCRIPTION, SubscriptionAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(SubscriptionAspect.class);

    public SubscriptionAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    public SubscriptionAspect withLabel(String label) {
        if (StringUtils.isNotBlank(label)) {
            put(ASConstants.FIELD_LABEL, label);
        }

        return this;
    }

    public SubscriptionAspect withType(String type) {
        if (StringUtils.isNotBlank(type)) {
            put(ASConstants.FIELD_TYPE, type);
        }

        return this;
    }

    public SubscriptionAspect withValidFrom(DateTime validFrom) {
        if (validFrom != null) {
            put(ASConstants.FIELD_VALID_FROM, validFrom);
        }

        return this;
    }

    public SubscriptionAspect withValidUntil(DateTime validUntil) {
        if (validUntil != null) {
            put(ASConstants.FIELD_VALID_UNTIL, validUntil);
        }

        return this;
    }

    public SubscriptionAspect withServingCount(Integer servingCount) {
        if (servingCount != null) {
            put(ASConstants.FIELD_SERVING_COUNT, servingCount.toString()); // toString if confluence example is valid, check needed
        }

        return this;
    }

    public SubscriptionAspect withRemainingServings(Integer remainingServings) {
        if (remainingServings != null) {
            put(ASConstants.FIELD_REMAINING_SERVINGS, remainingServings);
        }

        return this;
    }

    public String getLabel() {
        return (String) get(ASConstants.FIELD_LABEL);
    }

    public String getType() {
        return (String) get(ASConstants.FIELD_TYPE);
    }

    public DateTime getValidFrom() {
        if (this.containsKey(ASConstants.FIELD_VALID_FROM)) {
            return (DateTime) get(ASConstants.FIELD_VALID_FROM);
        } else {
            logger.info("Field 'valid_from' value is NULL");
            return null;
        }
    }

    public DateTime getValidUntil() {
        if (this.containsKey(ASConstants.FIELD_VALID_UNTIL)) {
            return (DateTime) get(ASConstants.FIELD_VALID_UNTIL);
        } else {
            logger.info("Field 'valid_until' value is NULL");
            return null;
        }
    }

    public String getServingCount() {
        if (this.containsKey(ASConstants.FIELD_SERVING_COUNT)) {
            return (String) get(ASConstants.FIELD_SERVING_COUNT);
        } else {
            logger.info("The subscription doesn't have a maximum number of servings.");
            return null;
        }
    }

    public String getRemainingServings() {
        if (this.containsKey(ASConstants.FIELD_REMAINING_SERVINGS)) {
            return (String) get(ASConstants.FIELD_REMAINING_SERVINGS);
        } else {
            logger.info("The subscription doesn't have a maximum number of servings.");
            return null;
        }
    }

    public Integer getServingCountInt() {
        if (this.containsKey(ASConstants.FIELD_SERVING_COUNT)) {
            return Integer.parseInt((String) get(ASConstants.FIELD_SERVING_COUNT));
        } else {
            logger.info("The subscription doesn't have a maximum number of servings.");
            return null;
        }
    }

    public Integer getRemainingServingsInt() {
        if (this.containsKey(ASConstants.FIELD_REMAINING_SERVINGS)) {
            return Integer.parseInt((String) get(ASConstants.FIELD_REMAINING_SERVINGS));
        } else {
            logger.info("The subscription doesn't have a maximum number of servings.");
            return null;
        }
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
            case ASConstants.FIELD_LABEL:
            case ASConstants.FIELD_TYPE:
            case ASConstants.FIELD_SERVING_COUNT:
            case ASConstants.FIELD_REMAINING_SERVINGS:
                value = validator().processString(theKey, value, true);
                break;
            case ASConstants.FIELD_VALID_FROM:
            case ASConstants.FIELD_VALID_UNTIL:
                value = validator().processIsoDateTime(theKey, value, true);
                break;
        }

        return super.put(theKey, value);
    }
}
