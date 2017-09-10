package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.relations.RelationsManager;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MessagingAspect extends ContentAspect {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_MESSAGING, MessagingAspect::new, AspectType.MergeStrategy.REPLACE) {

    };

    private static List<String> MESSAGING_TYPES =
            Arrays.asList(ASConstants.REL_SENT_BY, ASConstants.REL_SENT_TO, ASConstants.REL_SENT_TO_CC, ASConstants.REL_SENT_TO_BCC);

    public MessagingAspect() {
    }

    @Override
    public void loadFromValue(Object m) {
        if (m instanceof String) {
            put("subject", m);
        } else if (m instanceof Map) {
            super.loadFromValue(m);
        }
    }

    /************
     * CEP Utility Functions and Getters
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

        switch (theKey) {
            case ASConstants.FIELD_INVOLVES:
                value = new RelationsManager(value, MESSAGING_TYPES, this);
                return directPut(theKey, value);
            case ASConstants.FIELD_SUBJECT:
            case ASConstants.FIELD_TITLE:
            case ASConstants.FIELD_CONTENT:
            case ASConstants.FIELD_DETECTED_LANGUAGE:
            case ASConstants.FIELD_SENTIMENT:
                return super.put(theKey, value);
            default:
                if (!key.toString().startsWith("_"))
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Messaging Aspect"));
        }
        return null;

    }

    @Override
    protected void collectValuesToSave(Map<String, Object> values) {
        super.collectValuesToSave(values);
        values.remove(ASConstants.FIELD_INVOLVES);
    }


}
