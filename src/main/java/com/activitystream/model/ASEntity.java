package com.activitystream.model;

import com.activitystream.model.aspects.*;
import com.activitystream.model.config.JacksonMapper;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.AspectInterface;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.interfaces.BaseStreamItem;
import com.activitystream.model.relations.Relation;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ASEntity extends BusinessEntity {

    public ASEntity() {
    }

    public ASEntity(String type, String id, String label, String description) {
        this(new EntityReference(type, id), label, description);
    }

    public ASEntity(String type, String id, String label) {
        this(new EntityReference(type, id), label, null);
    }

    /**
     * Creates a new business entity like Customer, Order, Phone, Email, Vehicle etc.
     * @param type the type of the business entity (Camel Cased)
     * @param id the unique ID of the entity (Unique withing the Entity Type)
     */
    public ASEntity(String type, String id) {
        this(new EntityReference(type, id), null, null);
    }

    /**
     * Creates a new business entity like Customer, Order, Phone, Email, Vehicle etc.
     * @param enttiyReference the Entity Reference for the Entity
     * @param label the human readable label used to represent the entity
     */
    public ASEntity(EntityReference enttiyReference, String label) {
        put(ASConstants.FIELD_ENTITY_REF, enttiyReference);
        if (label != null) addAspect(new PresentationAspect(label));
    }

    /**
     * Creates a new business entity like Customer, Order, Phone, Email, Vehicle etc.
     * @param enttiyReference the Entity Reference for the Entity
     * @param label the human readable label used to represent the entity
     * @param description a short description for the entity
     */
    public ASEntity(EntityReference enttiyReference, String label, String description) {
        put(ASConstants.FIELD_ENTITY_REF, enttiyReference);
        if (label != null) addAspect(new PresentationAspect(label, null, null, description, null, null));
    }

    public ASEntity(Map map) {
        super(map);
    }

    public ASEntity(Map map, BaseStreamElement root) {
        super(map, root);
    }

    public ASEntity(String value) {
        super(value);
    }

    public ASEntity(String value, BaseStreamElement root) {
        super(value, root);
    }

    public ASEntity(Object value, BaseStreamElement root) {
        super(value, root);
    }

    /************  Access ************/

    public void setAddress(AddressAspect address) {
        addAspect(address);
    }

    public void setClassification(ClassificationAspect classification) {
        addAspect(classification);
    }

    public void setDemography(DemographyAspect demography) {
        addAspect(demography);
    }

    public void setDimensions(DimensionsAspect dimensions) {
        addAspect(dimensions);
    }

    public void setGeoLocation(GeoLocationAspect geoLocation) {
       addAspect(geoLocation);
    }

    public void setMetrics(MetricsAspect metrics) {
        addAspect(metrics);
    }

    public void setPresentation(PresentationAspect presentation) {
        addAspect(presentation);
    }

    public void setTimed(TimedAspect timed) {
       addAspect(timed);
    }

    public void setInventory(InventoryAspect inventory) {
       addAspect(inventory);
    }


    public ASEntity addRelation(Relation relation) {
        return addRelation(relation, (BaseStreamItem) root);
    }

    @Override
    public ASEntity addRelation(Relation relation, BaseStreamItem root) {
        super.addRelation(relation, root);
        return this;
    }

    @Override
    public ASEntity addRelation(String type, Object value) {
        super.addRelation(type, value);
        return this;
    }

    @Override
    public ASEntity addRelations(String type, Object value) {
        super.addRelations(type, value);
        return this;
    }

    @Override
    public ASEntity addProperties(Object... properties) {
        super.addProperties(properties);
        return this;
    }

    @Override
    public ASEntity addProperties(String property, Object value) {
        super.addProperties(property, value);
        return this;
    }

    @Override
    public ASEntity addRelationIfValid(String type, String entityType, String entityId) {
        super.addRelationIfValid(type, entityType, entityId);
        return this;
    }

    @Override
    public ASEntity addRelationIfValid(String type, String entityType, String entityId, Map<String, Object> relationsProperties) {
        super.addRelationIfValid(type, entityType, entityId, relationsProperties);
        return this;
    }

    public ASEntity addRelationIfValid(String type, String entityType, String entityId, Object ... properties) {
        Map<String,Object> propertiesMap = new LinkedHashMap<>();
        for (int i = 0; i < properties.length; i += 2) {
            propertiesMap.put(properties[i].toString(), properties[i + 1]);
        }
        return addRelationIfValid(type, entityType, entityId, propertiesMap);
    }

    @Override
    public ASEntity addRelationIfValid(String type, EntityReference entityRef) {
        super.addRelationIfValid(type, entityRef);
        return this;
    }

    /**
     * Add an aspect to the entity.
     * Aspects are standard schema peaces that can be added to entities
     * @param aspect the Entity Aspect to add
     * @return this ASEntity for chaining purposes
     */
    public ASEntity addAspect(AspectInterface aspect) {
        if (aspect != null && !aspect.isEmpty()) {
            aspect.setRoot(this);
            super.addAspect(aspect, this);
        }
        return this;
    }

    @Override
    @Deprecated
    public ASEntity addPresentation(String label, String thumbnail, String icon, String description, String detailsUrl) {
        super.addPresentation(label, thumbnail, icon, description, detailsUrl);
        return this;
    }

    /**
     * Partitions are used to separate data into storage containers/partitions
     * A sub-tenant can only see data belonging in his partition or partitions he has access to
     * The "_common" partition is used for entities belonging to everyone.
     * Email, Phone Number etc. are good examples of entities which should be stored in the "_common" partition
     * Entities inherit the partition from the Event that creates them if no partition is specified.
     * Entities can only be stored in one partition
     * @param partition the partition that the entity should be stored in
     * @return this ASEntity for chaining purposes
     */
    public ASEntity addPartition(String partition) {
        put(ASConstants.FIELD_PARTITION, partition);
        return this;
    }

    public ASEntity addDimension(String dimension, String value) {
        super.addDimension(dimension, value, this);
        return this;
    }

    @Override
    public ASEntity addMetric(String metric, double value) {
        super.addMetric(metric, value);
        return this;
    }

    public ASEntity addDeletedFlag(boolean deleted) {
        directPut(ASConstants.FIELD_DELETE, deleted);
        return this;
    }

    /************  UtilityFunctions ************/

    public String toJSON() throws JsonProcessingException {
        return JacksonMapper.getMapper().writeValueAsString(this);
    }

    public static ASEntity fromJSON(String json) throws IOException {
        return JacksonMapper.getMapper().readValue(json, ASEntity.class);
    }

}
