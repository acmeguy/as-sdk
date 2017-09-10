package com.activitystream.model.relations;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.stream.AbstractStreamItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class RelationsType implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RelationsType.class);

    private static Map<String, RelationsType> knownTypes = new LinkedHashMap<>();

    String rtString;

    public RelationsType(String rtString) {
        this.rtString = rtString;
    }

    public static RelationsType resolveTypesString(String rtString) {
        if (!knownTypes.containsKey(rtString)) {
            //logger.warn("Instantiate Relations Type: " + rtString);
            knownTypes.put(rtString, new RelationsType(rtString));
        }
        return knownTypes.get(rtString);
    }

    /**
     * Only the concrete (last) part of the entity-type sub-class structure
     */
    public String getRelationsType() {
        if (this.rtString.contains(":")) return this.rtString.substring(this.rtString.lastIndexOf(":") + 1);
        else return this.rtString;
    }

    /**
     * Only the base entity type for a sub-classes entity types
     */
    public String getRootRelationsType() {
        if (this.rtString.contains(":")) return this.rtString.substring(0, this.rtString.indexOf(":"));
        else return this.rtString;
    }

    /**
     * The complete Entity Type String
     */
    public String getRelationsTypeString() {
        return this.rtString;
    }

    @Override
    public String toString() {
        return getRelationsTypeString();
    }

    public boolean isEntityRelations() {
        return isEntityRelationsKey(getRelationsType());
    }

    public boolean isEventRelations() {
        return isEventRelationsKey(getRelationsType());
    }

    public boolean isCustomRelationshipType() {
        return isCustomRelationshipKey(getRelationsType());
    }

    public boolean isPrimaryRelations(BaseStreamElement root) {
        return isPrimaryRelationsKey(getRootRelationsType(), root);
    }


    /************  Analytical functions  ************/

    public static boolean isPrimaryRelationsKey(String key, BaseStreamElement root) {
        if (root instanceof AbstractStreamItem) return isEventRelationsKey(key);
        else if (root instanceof BusinessEntity) return isEntityRelationsKey(key);
        else return isEntityRelationsKey(key) || isEventRelationsKey(key);
    }

    public static boolean isEntityRelationsKey(String key) {
        if (!key.toUpperCase().equals(key)) return false; //only consider uppercase values
        if (key.contains(":")) key = key.split(":")[0];
        return ASConstants.ENTITY_RELATIONS.contains(key);
    }

    public static boolean isEventRelationsKey(String key) {
        if (!key.toUpperCase().equals(key)) return false; //only consider uppercase values
        if (key.contains(":")) key = key.split(":")[0];
        return ASConstants.EVENT_RELATIONS.contains(key);
    }

    public static boolean isCustomRelationshipKey(String key) {
        if (!key.toUpperCase().equals(key)) return false; //only consider uppercase values
        if (key.contains(":")) {
            key = key.split(":")[0];
        }
        return ASConstants.EVENT_RELATIONS.contains(key);
    }


}