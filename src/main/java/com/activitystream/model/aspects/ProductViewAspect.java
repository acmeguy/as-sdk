package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class ProductViewAspect extends AbstractMapAspect implements LinkedElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_PRODUCT_VIEW, ProductViewAspect::new) {
        /*
        @Override
        public boolean isActive(SavableElement element, BaseStreamElement root) {
            return ((OrientVertex) element).countEdges(Direction.OUT, "VIEWED") > 0;
        }
        */
    };

    protected static final Logger logger = LoggerFactory.getLogger(ProductViewAspect.class);

    final static List<String> EXCLUDED_PARAMS = Arrays.asList(ASConstants.FIELD_TOKEN);

    public ProductViewAspect() {

    }

    @Override
    public void loadFromValue(Object value) {
        if (value instanceof String) {
            put("product", value);
        } else {
            super.loadFromValue(value);
        }
    }

    /************
     * Utility functions
     ************/

    @Override
    public void onEachEntityReference(Consumer<EntityReference> action) {
        if (containsKey(ASConstants.FIELD_PRODUCT)) {
            action.accept(new EntityReference((String) get(ASConstants.FIELD_PRODUCT)));
        }
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    private String getQuery(String path) {
        try {
            URL aURL = new URL(path);
            return aURL.getQuery();
        } catch (MalformedURLException e) {
            logger.error("Bad URL", e);
        }
        return null;
    }

    private Map<String, String> getQueryParameters(String query) {
        int pos = query.indexOf("?");
        if (pos > -1) query = query.substring(pos + 1);
        Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query);
        if (!map.isEmpty()) return map;
        return null;
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

        //todo - implement proper data processing

        switch (theKey) {
            case ASConstants.FIELD_PRODUCT:
                value = validator().processUtlString(theKey, value, false);
                break;
            case ASConstants.FIELD_VERSION:
                theKey = ASConstants.FIELD_VARIANT;
            case ASConstants.FIELD_VARIANT:
                value = validator().processUtlString(theKey, value, false);
                break;
            case ASConstants.FIELD_CONTEXT:
                value = validator().processUtlString(theKey, value, false);
                break;
            case ASConstants.FIELD_REFERRER:
                value = validator().processUtlString(theKey, value, false);
                break;
            default:
                logger.warn(theKey + " was not found for Product Viewed Aspect");
                //this.addException(new UnsupportedAspectError("The " +  theKey + " property is not supported for the Client IP Aspect"));
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

}
