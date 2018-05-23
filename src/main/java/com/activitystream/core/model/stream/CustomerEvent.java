package com.activitystream.core.model.stream;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.interfaces.BaseStreamElement;
import com.activitystream.core.model.aspects.AspectManager;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.relations.Relation;
import com.activitystream.core.model.relations.RelationsManager;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.validation.InvalidPropertyContentError;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CustomerEvent extends AbstractBaseEvent {

    private static final Logger logger = LoggerFactory.getLogger(CustomerEvent.class);
    private Map<String, AbstractBaseEvent> footprintDuplicateGuard;

    public CustomerEvent() {

    }

    public CustomerEvent(Map map, BaseStreamElement root) {
        super(map, root);
    }

    public CustomerEvent(Map map) {
        super(map, null);
    }


    public Map<String, AbstractBaseEvent> getFootprintDuplicateGuard() {
        if (footprintDuplicateGuard == null)
            footprintDuplicateGuard = new HashMap<>();
        return footprintDuplicateGuard;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public DateTime getReceivedAt() {
        return (DateTime) getOrDefault(ASConstants.FIELD_RECEIVED_AT, get("_" + ASConstants.FIELD_RECEIVED_AT));
    }

    public DateTime getOccurredAt() {
        Object date = get(ASConstants.FIELD_OCCURRED_AT);
        if (date instanceof DateTime) return (DateTime) get(ASConstants.FIELD_OCCURRED_AT);
        else return new DateTime(get(ASConstants.FIELD_OCCURRED_AT));
    }

    public RelationsManager getInvolves() {
        return (RelationsManager) get(ASConstants.FIELD_RELATIONS);
    }

    public String getDescription() {
        return (String) get(ASConstants.FIELD_DESCRIPTION);
    }

    public String getTypeContext() {
        //todo - calculate for type
        String domain = (String) get(ASConstants.FIELD_TYPE);
        if (domain != null) {
            domain = domain.substring(0, domain.indexOf(".", domain.indexOf(".") + 1)); //2 levels deep
        }
        return domain;
    }

    @Override
    public EventTypeReference getEventType() {
        return EventTypeReference.resolveTypesString(EventTypeReference.Category.CUSTOMER_EVENT, getType());
    }

    public CustomerEvent setType(String type) {
        put(ASConstants.FIELD_TYPE, type);
        return this;
    }

    public Map<String, Object> getProperties(boolean create) {
        Map<String, Object> properties = (Map<String, Object>) get(ASConstants.FIELD_PROPERTIES);
        if (create && properties == null) {
            properties = new LinkedHashMap<String, Object>();
            put(ASConstants.FIELD_PROPERTIES, properties);
        }
        return properties;
    }

    public String getBuyer() {
        return getPrimaryRole("ACTOR:BUYER");
    }

    //Refactor the following to use aspects and/or make aspects extend Avro records

    public String getActor() {
        return getPrimaryRole("ACTOR");
    }

    public String getActorProxy() {
        Relation actorRel = getRelationsManager().getFirstRelationsOfType("ACTOR");
        if (actorRel == null) return null;
        Relation proxyForRel = actorRel.getRelatedBusinessEntity().getRelationsManager().getFirstRelationsOfType("PROXY_FOR");
        if (proxyForRel == null) return null;
        return proxyForRel.getRelatedBusinessEntity().getEntityReference().toString();
    }

    /************ Access ************/

    @Override
    public Object put(Object key, Object value) {
        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();

        if (!theKey.equals(theLCKey)) {
            addProblem(new AdjustedPropertyWarning("Property name '" + theKey + "' converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_RECEIVED_AT:
            case ASConstants.FIELD_REGISTERED_AT:
            case ASConstants.FIELD_OCCURRED_AT:
                //todo - configure better nanosecond support
                value = validator().processIsoDateTime(theKey, value, true);
                break;
            case "event":
            case "action":
                theKey = ASConstants.FIELD_TYPE;
            case ASConstants.FIELD_TYPE:
                value = validator().processTypeString(theKey, value, true);
                break;
            case "source":
                theKey = ASConstants.FIELD_ORIGIN;
            case ASConstants.FIELD_ORIGIN:
                value = validator().processTypeString(theKey, value, true);
                break;
            case ASConstants.FIELD_PARTITION:
                value = validator().processLowerCaseString(theKey, value, false);
                break;
            case ASConstants.FIELD_STREAM_ID_INTERNAL:
                theKey = ASConstants.FIELD_STREAM_ID;
            case ASConstants.FIELD_STREAM_ID:
                if (value instanceof String)
                    value = UUID.fromString((String) value);
                if (!(value instanceof UUID)) {
                    addProblem(new InvalidPropertyContentError("Stream ID must be a UUID."));
                }
                break;
            case ASConstants.FIELD_DESCRIPTION:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_SUBTENANT:
                value = validator().processLowerCaseString(theKey, value, false);
                break;
            case ASConstants.FIELD_RELATIONS:
                theKey = ASConstants.FIELD_INVOLVES;
            case ASConstants.FIELD_INVOLVES:
                if (value == null) {
                    addProblem(new InvalidPropertyContentError("Relations can not be populated with a null value"));
                    return null;
                } else if (!(value instanceof RelationsManager)) {
                    value = new RelationsManager(value, getAllowedRelTypes(), this);
                }
                break;
            case ASConstants.FIELD_ACL:
                if (value == null) {
                    addProblem(new InvalidPropertyContentError("Access Control List can not be populated with a null value"));
                    return null;
                } else if (!(value instanceof List)) {
                    addProblem(new InvalidPropertyContentError("Access Control List must be populated with a list. Not a " + value.getClass()));
                    return null;
                }
                break;
            case "token":
                break;
            case ASConstants.FIELD_IMPORTANCE:
                if (value instanceof ImportanceLevel) value = ((ImportanceLevel) value).ordinal();
                value = validator().processInteger(theKey, value, false, 0, 5);
                break;
            case ASConstants.FIELD_ASPECTS:
                value = validator().processMap(theKey, value, true);
                value = new AspectManager((Map) value, this);
                break;
            case "_exceptions":
                break;
            case ASConstants.FIELD_PROPERTIES:
                if (value == null) {
                    addProblem(new InvalidPropertyContentError("Properties can not be populated with a null value"));
                    return null;
                } else if (!(value instanceof Map)) {
                    addProblem(
                            new InvalidPropertyContentError("Properties must be populated with a map. Not a " + value.getClass() + ", value: " + value));
                    return null;
                }
                break;
            default:
                //allow enrichment fields (All prefixed with "_")
                if (!theKey.startsWith("_")) {
                    addProblem(new InvalidPropertyContentError("Ignored Property: " + theKey + " (contains: " + value + ")"));
                    //todo - check this handling as some messages seem to have incomplete/access information
                    //logger.error("Missing field support for: " + theKey);
                    return null;
                }

        }
        return super.put(theKey, value);
    }

    @Override
    public void prepareForFederation() {
    }

}
