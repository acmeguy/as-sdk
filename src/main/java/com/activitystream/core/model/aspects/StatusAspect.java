package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.validation.UnsupportedAspectError;
import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.net.SocketFlow;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class StatusAspect extends AbstractMapAspect {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_STATUS, StatusAspect::new, AspectType.MergeStrategy.MERGE) {
        /*
        @Override
        protected boolean isActive(SavableElement element, BaseStreamElement root) {
            OrientVertexType vertexType = ((OrientVertex) element).getType();
            return vertexType.isSubClassOf(ASConstants.AS_ENTITY) || vertexType.isSubClassOf(ASConstants.AS_INTERNAL_ENTITY);
        }
        */
    };

    protected static final Logger logger = LoggerFactory.getLogger(StatusAspect.class);

    public StatusAspect() {
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    /*** RECHECK IS NEEDED ***/
    public StatusAspect withRegisteredAt(DateTime dt) {
        put(ASConstants.FIELD_REGISTERED_AT, dt);
        return this;
    }

    public StatusAspect withVersion(Integer version) {
        put(ASConstants.FIELD_VERSION, version);
        return this;
    }


    public StatusAspect withUpdateOccurredAt(DateTime dt) {
        if (dt != null) {
            put(ASConstants.FIELD_UPDATE_OCCURRED_AT, dt);
        }

        return this;
    }

    public StatusAspect withDeleted() {
        put(ASConstants.FIELD_IS_DELETED, true);
        return this;
    }

    public StatusAspect withLooksInvalid(Boolean looksInvalid) {
        if (looksInvalid != null) {
            put(ASConstants.FIELD_LOOKS_INVALID, looksInvalid);
        }

        return this;
    }


    public StatusAspect withCancelled(Boolean cancelled) {
        if (cancelled != null) {
            put(ASConstants.FIELD_CANCELLED, cancelled);
        }

        return this;
    }

    public String getStatus() {
        return null;
    }

    public boolean isVisible() {
        return !getOrDefault(ASConstants.FIELD_LOOKS_INVALID, false);
    }

    public boolean isObsolete() {
        return getOrDefault("_obsolete", false);
    }

    private boolean getOrDefault(String key, boolean defaultValue) {
        Boolean value = (Boolean) get(key);
        return value != null ? value : defaultValue;
    }

    public boolean isObsolete(DateTime occurredAt) {
        DateTime updateOccurredAt = getUpdateOccurredAt();
        return updateOccurredAt != null && occurredAt != null && occurredAt.isBefore(updateOccurredAt);
    }

    public boolean needsUpdate(long minUpdateInterval) {
        Long currentUpdateInterval = (Long) get("_update_interval");
        return currentUpdateInterval == null || currentUpdateInterval > minUpdateInterval;
    }

    public boolean isMeasurement() {
        return getUpdateOccurredAt() != null;
    }

    public DateTime getUpdateOccurredAt() {
        return (DateTime) get(ASConstants.FIELD_UPDATE_OCCURRED_AT);
    }

    public DateTime getRegisteredAt() {
        return (DateTime) get(ASConstants.FIELD_REGISTERED_AT);
    }

    public Integer getVersion() {
        return (Integer) get(ASConstants.FIELD_VERSION);
    }

    /*
    todo implement elsewhere
    public boolean versionMatches(OIdentifiable element) {
        return getVersion().equals(element.getRecord().getVersion());
    }
    */

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
            case ASConstants.FIELD_VERSION:
                value = validator().processInteger(theKey, value, false, null, null);
                break;
            case ASConstants.FIELD_REGISTERED_AT:
                value = validator().processIsoDateTime(theKey, value, false);
                break;
            // The time when the last change to the entity happened at the source. Can be very different from when the entity was updated in the graph if
            // historical data has been imported.
            case ASConstants.FIELD_UPDATE_OCCURRED_AT:
                value = validator().processIsoDateTime(theKey, value, false);
                break;
            case ASConstants.FIELD_IS_DELETED:
            case ASConstants.FIELD_LOOKS_INVALID:
            case ASConstants.FIELD_INDEX_EDGES:
            case ASConstants.FIELD_CANCELLED:
                value = validator().processBoolean(theKey, value, false);
                break;
            default:
                if (!theKey.startsWith("_")) {
                    logger.error("The '" + theKey + "' Aspect is not valid");
                    this.addProblem(new UnsupportedAspectError("The " + theKey + " is not a valid property for the Status Aspect"));
                }
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    /************  Persistence ************/

    @Override
    protected void collectValuesToSave(Map<String, Object> values) {
        super.collectValuesToSave(values);

        values.remove(ASConstants.FIELD_VERSION);
        values.remove(ASConstants.FIELD_REGISTERED_AT);
    }

    @Override
    public void simplify() {
        remove(ASConstants.FIELD_VERSION);
        remove(ASConstants.FIELD_REGISTERED_AT);
    }

}