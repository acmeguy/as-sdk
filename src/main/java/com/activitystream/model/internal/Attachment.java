package com.activitystream.model.internal;

import com.activitystream.model.ASConstants;
import com.activitystream.model.aspects.AspectManager;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.utils.StreamIdUtils;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.InvalidPropertyContentError;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class Attachment extends InternalEntity {

    private static final Logger logger = LoggerFactory.getLogger(Attachment.class);
    /************  Constructors ************/

    static final List<String> NATIVE_PROPERTIES =
            Arrays.asList(ASConstants.FIELD_URL, ASConstants.FIELD_FILENAME, ASConstants.FIELD_SIZE, ASConstants.FIELD_HEIGHT, ASConstants.FIELD_WIDTH,
                    ASConstants.FIELD_BITRATE, ASConstants.FIELD_CONTENT_TYPE, ASConstants.FIELD_PROPERTIES, ASConstants.FIELD_CREATED,
                    ASConstants.FIELD_UPDATED);
    private static final List<String> ACTIVE_ASPECTS =
            Arrays.asList(ASConstants.ASPECTS_PRESENTATION, ASConstants.ASPECTS_TAGS, ASConstants.ASPECTS_CLASSIFICATION);

    public Attachment(Map map, BaseStreamElement root) {
        super(map, root);
    }

    /************  Utility functions  ************/

    @Override
    public String getElementType() {
        return ASConstants.AS_ATTACHMENT;
    }

    @Override
    public List<String> getAllowedRelTypes() {
        return Arrays.asList(ASConstants.REL_ATTACHED);
    }

    @Override
    public List<String> getAllowedAspects() {
        return Arrays.asList(ASConstants.ASPECTS_PRESENTATION);
    }

    @Override
    public String getFootprint() {
        if (containsKey(ASConstants.FIELD_FINGERPRINT)) return (String) get(ASConstants.FIELD_FINGERPRINT);
        else if (containsKey(ASConstants.FIELD_URL)) return (String) get(ASConstants.FIELD_URL);
        else {
            logger.error("No footprint available for attachment " + this);
            return null;
        }
    }

    @Override
    public EntityReference getEntityReference() {
        EntityReference ref = null;
        if (containsKey(ASConstants.FIELD_FINGERPRINT)) {
            ref = new EntityReference(ASConstants.AS_ATTACHMENT, (String) get(ASConstants.FIELD_FINGERPRINT));
        } else if (containsKey(ASConstants.FIELD_URL) && get(ASConstants.FIELD_URL) != null && !((String) get(ASConstants.FIELD_URL)).isEmpty()) {
            ref = new EntityReference(ASConstants.AS_ATTACHMENT,
                    StreamIdUtils.calculateStreamId(((String) get(ASConstants.FIELD_URL)).trim().toLowerCase()).toString());
        }

        if (ref != null) {
            //ref.setDefaults(this); //todo - implement defaults
        } else {
            logger.error("No entity reference is available for attachment " + this);
        }

        return ref;
    }

    @Override
    public List<String> getNativeProperties() {
        return NATIVE_PROPERTIES;
    }

    private long getMillis(String key) {
        DateTime value = (DateTime) get(key);
        return value != null ? value.getMillis() : 0L;
    }

    /************  Assignment and validation
     * @param action************/

    @Override
    public void onEachEntityReference(Consumer<EntityReference> action) {
        action.accept(getEntityReference());
    }

    /************  Assignment and validation ************/

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();

        if (!theKey.equals(theLCKey)) {
            addProblem(new AdjustedPropertyWarning("Property name '" + theKey + "' converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_URL:
                value = validator().processUrl(theKey, value, false);
                break;
            case ASConstants.FIELD_FILENAME:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_FINGERPRINT:
            case ASConstants.FIELD_CONTENT_TYPE:
            case ASConstants.FIELD_BITRATE:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_PARTITION:
                value = validator().processLowerCaseString(theKey, value, false);
                break;
            case ASConstants.FIELD_SIZE:
            case ASConstants.FIELD_HEIGHT:
            case ASConstants.FIELD_WIDTH:
                value = validator().processInteger(theKey, value, false, 0, null);
                break;
            case ASConstants.FIELD_CREATED:
            case ASConstants.FIELD_UPDATED:
                value = validator().processIsoDateTime(theKey, value, false);
                break;
            case ASConstants.FIELD_PROPERTIES:
                value = validator().processMap(theKey, value, false);
                break;
            case ASConstants.FIELD_ASPECTS:
                if (value instanceof Map) value = new AspectManager((Map) value, this, getAllowedAspects());
                break;
            case ASConstants.FIELD_ACL:
                if (value == null) {
                    addProblem(new InvalidPropertyContentError(key + " can not be populated with a null value"));
                    return null;
                } else if (!(value instanceof List)) {
                    addProblem(new InvalidPropertyContentError(key + " must be populated with a list. Not a " + value.getClass()));
                    return null;
                }
                break;
            default:
                if (!theKey.startsWith("_")) {
                    addProblem(new InvalidPropertyContentError("Ignored Property: " + theKey + " (contains: " + value + ")"));
                    return null;
                }
        }
        if (value == null || (value instanceof Collection && ((Collection) value).isEmpty())) return null;

        return super.put(theKey, value);
    }

    @Override
    public void prepareForFederation() {

    }
}
