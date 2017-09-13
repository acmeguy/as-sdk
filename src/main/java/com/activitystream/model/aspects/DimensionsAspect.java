package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.entities.EntityChangeMap;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.DynamicAspect;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DimensionsAspect extends AbstractMapAspect implements DynamicAspect {

    public static final AspectType ASPECT_TYPE =
            new AspectType.Embedded(ASConstants.ASPECTS_DIMENSIONS, DimensionsAspect::new, AspectType.MergeStrategy.MERGE) {
            };

    protected static final Logger logger = LoggerFactory.getLogger(DimensionsAspect.class);

    public DimensionsAspect() {

    }

    @Override
    public void loadFromValue(Object value) {
        if (value instanceof String) {
            put("user_agent", value);
        } else if (value instanceof Map) {
            super.loadFromValue(value);
        }
    }

    /************
     * Utility functions
     ************/

    @Override
    public void visited(Collection<EntityReference> visited) {
        super.put("_visited", visited);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        theKey = theKey.replaceAll("\\.", "_");
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        if (theKey.startsWith("_")) {
            if (value == null)
                return null;
            return super.put(theKey, value);
        }

        if (value instanceof List) {
            List categoryList = (List) value;
            if (key.equals(ASConstants.FIELD_CATEGORY) && categoryList.size() == 3) {
                put(ASConstants.FIELD_TYPE, categoryList.get(0));
                put(ASConstants.FIELD_CATEGORY, categoryList.get(1));
                put(ASConstants.FIELD_SUB_CATEGORY, categoryList.get(2));
                return null;
            } else if (key.equals(ASConstants.FIELD_CATEGORY) && categoryList.size() == 2) {
                put(ASConstants.FIELD_TYPE, "To Do");
                put(ASConstants.FIELD_CATEGORY, categoryList.get(0));
                put(ASConstants.FIELD_SUB_CATEGORY, categoryList.get(1));
                return null;
            } else if (key.equals(ASConstants.FIELD_CATEGORIES)) {
                //Allow for magic key
            } else {
                addProblem(new AdjustedPropertyWarning("List values are not preferred as Dimension values"));
                logger.warn("List value in dimensions: " + key + " = " + value);
                return null;
            }
        } else if (value instanceof Map) {
            //addException(new InvalidPropertyContentError("Map values are not supported as Dimension values"));
            //logger.warn("Map value in dimensions: " + key + " = " + value);
            this.putAll((Map) value);
            return null;
        }

        if (value == null || value.toString().isEmpty()) return null; //prune away empty values

        return super.put(theKey, value.toString());
    }

    @Override
    public void verify() {

    }

    /************  Persistence ************/

    @Override
    protected void handleChanges(Map<String, Object> oldValues, Map<String, Object> newValues) {
        registerChanges(oldValues, newValues, EntityChangeMap.ACTION.IGNORE, EntityChangeMap.ACTION.PROCESS);
    }

    @Override
    public String getDocumentClassName() {
        return ASConstants.EM_DIMENSIONS;
    }

    @Override
    public Collection<String> getFieldNames() {
        return keySet();
    }

    @Override
    public Object getValueType() {
        return "STRING";
        //return OType.STRING;
    }
}
