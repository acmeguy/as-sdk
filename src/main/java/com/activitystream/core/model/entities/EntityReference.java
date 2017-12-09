package com.activitystream.core.model.entities;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.interfaces.BaseStreamItem;
import com.activitystream.core.model.interfaces.LinkedElement;
import com.activitystream.core.model.security.SecurityScope;
import com.activitystream.core.model.utils.Slugify;
import com.activitystream.core.model.utils.StreamIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class EntityReference implements Serializable, Comparable, LinkedElement {

    private static final Logger logger = LoggerFactory.getLogger(EntityReference.class);

    private String entityId;
    private UUID streamId;
    private EntityTypeReference entityTypeReference;
    private String label = null;
    private boolean valid = true;
    private Map<String, Object> defaults = null;
    private SecurityScope securityScope = null;
    private BaseStreamItem root;

    /**
     * Parsing constructor.
     */
    public EntityReference(String itemReference) {
        this(itemReference, (BaseStreamItem) null);
    }

    /**
     * Parsing constructor.
     */
    public EntityReference(String itemReference, BaseStreamItem root) {
        this.root = root;

        if (itemReference == null) {
            logger.warn("Entity Reference is missing!");
            return;
        }
        //todo - throw and exception here?

        int pos = itemReference.indexOf("/");

        if (itemReference.contains("-") && pos < 0) {
            this.entityId = itemReference;
            this.entityTypeReference = EntityTypeReference.resolveTypesString(ASConstants.AS_STREAM_ITEM);
        } else {
            if (pos < 0) {
                logger.warn("itemReference: " + itemReference + " at " + pos);
            } else {
                this.entityId = itemReference.substring(pos + 1);
                this.entityTypeReference = EntityTypeReference.resolveTypesString(itemReference.substring(0, pos));
            }
        }
        validate();
    }

    public EntityReference(String entityType, String entityId) {
        this(entityType, entityId, null, null);
    }

    public EntityReference(String entityType, String entityId, BaseStreamItem root) {
        this(entityType, entityId, null, root);
    }

    public EntityReference(String entityType, String entityId, String label) {
        this(entityType, entityId, label, null, null);
    }

    public EntityReference(String entityType, String entityId, String label, BaseStreamItem root) {
        this(entityType, entityId, label, null, root);
    }

    public EntityReference(String entityType, String entityId, String label, Map<String, Object> defaults, BaseStreamItem root) {
        this.entityTypeReference = EntityTypeReference.resolveTypesString(Slugify.asEntityType(entityType));
        this.entityId = entityId;
        this.label = label;
        this.defaults = defaults;
        this.root = root;
        validate();
    }

    public EntityReference(UUID streamId) {
        this.entityTypeReference = EntityTypeReference.resolveTypesString(ASConstants.AS_STREAM_ITEM);
        this.entityId = streamId.toString();
        this.streamId = streamId;
        validate();
    }

    private void validate() {
        if (entityTypeReference == null || entityId == null)
            this.valid = false;
        else {
            //todo - create pluggable validation that listens to settings
            if (getEntityTypeString().equals("Email")) {
                this.valid = StringUtils.isNotBlank(entityId);
            }
            this.valid = StringUtils.isNotBlank(entityId);
        }
    }

    // Utility functions

    @Override
    public boolean traverse(ElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public void onEachEntityReference(Consumer<EntityReference> action) {
        action.accept(this);
    }

    @Override
    public void setSecurityScope(SecurityScope scope) {
        this.securityScope = scope;
    }

    public Map<String, Object> getDefaults() {
        return defaults;
    }

    public void setDefaults(Map<String, Object> defaults) {
        this.defaults = defaults;
    }

    /************  Access  ************/


    @JsonIgnore
    public String getEntityReference() {
        return getEntityTypeString() + "/" + this.getEntityId();
    }

    @JsonIgnore
    public String getNormalizedEntityReference() {
        return getNormalized(getEntityReference());
    }

    @JsonIgnore
    public EntityTypeReference getEntityTypeReference() {
        return entityTypeReference;
    }

    @JsonIgnore
    public String getEntityTypeString() {
        return (this.entityTypeReference != null) ? entityTypeReference.getLeafEntityType() : null;
    }

    @JsonIgnore
    public String getEntityId() {
        return entityId;
    }

    @JsonIgnore
    public String getRootEntityReference() {
        return getEntityTypeReference().getRootEntityType() + "/" + this.getEntityId();
    }

    @JsonIgnore
    public static String getNormalized(String id) {
        return id.toLowerCase();
    }

    @JsonIgnore
    public String getLeafEntityReference() {
        return getEntityTypeReference().getLeafEntityType() + "/" + this.getEntityId();
    }

    public String getLabel() {
        return label;
    }

    public UUID getEntityStreamId() {
        if (streamId == null) {
            if (getEntityTypeString().equals(ASConstants.AS_STREAM_ITEM))
                streamId = UUID.fromString(this.getEntityId());
            else
                streamId = StreamIdUtils.calculateStreamId(getEntityTypeReference().getRootEntityType(), getEntityId());
        }
        return streamId;
    }

    @JsonIgnore
    public boolean isComplete() {
        return hasEntityId() && hasEntityType();
    }

    public boolean hasEntityType() {
        return entityTypeReference != null && entityTypeReference.isValidType();
    }

    public boolean hasEntityId() {
        return entityId != null;
    }

    public Map asAnalyticsMap() {
        return new HashMap<String, String>() {{
            put(getEntityTypeString(), getEntityId());
        }};
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * Only the concrete (last) part of the entity-type sub-class structure
     */

    @Override
    @JsonValue
    public String toString() {
        return (this.securityScope != null) ? this.securityScope.getAdjustedEntityReference(getEntityTypeString(), this.getEntityId()) :
                this.getEntityReference();
    }

    @Override
    public int compareTo(Object o) {
        //logger.warn(toString() + " is compared to:" + o.toString() + " is: " + toString().compareTo(o.toString()));
        return toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object d) {
        if (d == null)
            return false;
        //logger.warn(toString() +  " is equal to:" + d.toString());
        return this.toString().equals(d.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /*
    @Override
    public void createApiLinks(Tenant tenant, LINK_DETAILS details) {

    }
    */

}



