package com.activitystream.core.model.entities;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.internal.TypeReference;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Value object representing a reference to an entity type. The reference can be a single type name, or a list of names representing a class hierarchy.
 */
public class EntityTypeReference implements TypeReference<EntityTypeReference> {

    private static final Map<String, EntityTypeReference> knownTypes = new ConcurrentHashMap<>();

    private String entityTypeString;
    private String[] entityTypeParts;
    private boolean valid;

    public EntityTypeReference(String entityTypeString) {
        this.entityTypeString = entityTypeString;
        this.entityTypeParts = entityTypeString.split(":");

        this.valid = entityTypeString.indexOf('.') < 0 && Arrays.stream(entityTypeParts).allMatch(part -> {
            try {
                return URLEncoder.encode(part, "UTF-8").equals(part);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static EntityTypeReference resolveTypesString(String entityTypeString) {
        return knownTypes.computeIfAbsent(entityTypeString, EntityTypeReference::new);
    }

    /**
     * Only the base entity type for a sub-classes entity types
     */
    public String getRootEntityType() {
        return entityTypeParts[0];
    }

    /**
     * Only the concrete (last) part of the entity-type sub-class structure
     */
    public String getLeafEntityType() {
        return entityTypeParts[entityTypeParts.length - 1];
    }

    public String[] getEntityTypeParts() {
        return entityTypeParts;
    }

    /**
     * The complete Entity Type String
     */
    @JsonValue
    @Override
    public String toString() {
        return entityTypeString;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EntityTypeReference && entityTypeString.equals(((EntityTypeReference) o).entityTypeString);
    }

    @Override
    public int hashCode() {
        return entityTypeString.hashCode();
    }

    @Override
    public int compareTo(EntityTypeReference o) {
        return entityTypeString.compareTo(o.entityTypeString);
    }

    @Override
    public EntityTypeReference getSuperType() {
        int lastColon = entityTypeString.lastIndexOf(':');
        if (lastColon >= 0)
            return resolveTypesString(entityTypeString.substring(0, lastColon));
        return null;
    }

    @Override
    public boolean hasSuperType() {
        return entityTypeString.lastIndexOf(':') >= 0;
    }

    @Override
    public EntityReference asEntityReference() {
        return new EntityReference(ASConstants.AS_ENTITY_TYPE, entityTypeString);
    }

    @Override
    public boolean isValidType() {
        return valid;
    }

    @Override
    public boolean isInternal() {
        return entityTypeString.startsWith("AS");
    }

    @Override
    public String getVertexTypeName() {
        return getLeafEntityType();
    }

    @Override
    public String getRootVertexTypeName() {
        return ASConstants.AS_CUSTOMER_ENTITY;
    }
}
