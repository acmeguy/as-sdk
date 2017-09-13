package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.interfaces.AspectInterface;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.UnsupportedAspectError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ABTestingAspect extends AbstractMapAspect implements AspectInterface {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_AB_TEST, ABTestingAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(ABTestingAspect.class);

    public ABTestingAspect() {

    }

    @Override
    public void loadFromValue(Object value) {
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

    public String getId() {
        return (String) get("id");
    }

    public String getVariant() {
        return (String) get("variant");
    }

    public String getOutcome() {
        return (String) get("outcome");
    }

    public Double getMetric() {
        return (Double) get("metric");
    }

    public Double getAmount() {
        return (Double) get("amount");
    }

    //todo - create Esper friendly property getters

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
            case "id":
                value = validator().processIdString(theKey, value, true);
                break;
            case "variant":
            case "outcome":
                value = validator().processString(theKey, value, false);
                break;
            case "metric":
            case "amount":
                value = validator().processDouble(theKey, value, false, null, null);
                break;
            case ASConstants.FIELD_PROPERTIES:
                value = validator().processMap(theKey, value, false);
                break;
            default:
                this.addProblem(new UnsupportedAspectError("The " + theKey + " property is not supported for the Client Device Aspect"));
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

}
