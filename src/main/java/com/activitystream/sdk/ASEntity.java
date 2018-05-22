package com.activitystream.sdk;

import com.activitystream.core.model.aspects.*;
import com.activitystream.core.model.entities.BusinessEntity;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.interfaces.AspectInterface;
import com.activitystream.core.model.interfaces.BaseStreamElement;
import com.activitystream.core.model.interfaces.BaseStreamItem;
import com.activitystream.core.model.relations.Relation;
import com.activitystream.sdk.utilities.JacksonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ASEntity extends BusinessEntity {

    public ASEntity() {
    }

    public ASEntity(Archetype archetype, String id) {
        put(ASConstants.FIELD_ARCHETYPE, archetype.getName());

        if (archetype.getVariant() != null) {
            put(ASConstants.FIELD_ARCHETYPE_VARIANT, archetype.getVariant());
            put(ASConstants.FIELD_ENTITY_REF, new EntityReference(archetype.getVariant(), id));
        } else {
            put(ASConstants.FIELD_ENTITY_REF, new EntityReference(archetype.getName(), id));
        }
    }

    public ASEntity(String type, String id, String label, String description) {
        this(new EntityReference(type, id), label, description);
    }

    public ASEntity(String type, String id, String label) {
        this(new EntityReference(type, id), label, null);
    }

    /**
     * Creates a new business entity like Customer, Order, Phone, Email, Vehicle etc.
     *
     * @param type the type of the business entity (Camel Cased)
     * @param id   the unique ID of the entity (Unique withing the Entity Type)
     */
    public ASEntity(String type, String id) {
        this(new EntityReference(type, id), null, null);
    }

    /**
     * Creates a new business entity like Customer, Order, Phone, Email, Vehicle etc.
     *
     * @param enttiyReference the Entity Reference for the Entity
     * @param label           the human readable label used to represent the entity
     */
    public ASEntity(EntityReference enttiyReference, String label) {
        put(ASConstants.FIELD_ENTITY_REF, enttiyReference);
        if (label != null) withAspect(new PresentationAspect(label));
    }

    /**
     * Creates a new business entity like Customer, Order, Phone, Email, Vehicle etc.
     *
     * @param enttiyReference the Entity Reference for the Entity
     * @param label           the human readable label used to represent the entity
     * @param description     a short description for the entity
     */
    public ASEntity(EntityReference enttiyReference, String label, String description) {
        put(ASConstants.FIELD_ENTITY_REF, enttiyReference);
        if (label != null) withAspect(new PresentationAspect(label, null, null, description, null, null));
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
        withAspect(address);
    }

    public void setClassification(ClassificationAspect classification) {
        withAspect(classification);
    }

    public void setDemography(DemographyAspect demography) {
        withAspect(demography);
    }

    public void setDimensions(DimensionsAspect dimensions) {
        withAspect(dimensions);
    }

    public void setGeoLocation(GeoLocationAspect geoLocation) {
        withAspect(geoLocation);
    }

    public void setMetrics(MetricsAspect metrics) {
        withAspect(metrics);
    }

    public void setPresentation(PresentationAspect presentation) {
        withAspect(presentation);
    }

    public void setTimed(TimedAspect timed) {
        withAspect(timed);
    }

    public void setInventory(InventoryAspect inventory) {
        withAspect(inventory);
    }

    @Override
    public ASEntity withRelation(Relation relation) {
        return withRelation(relation, (BaseStreamItem) root);
    }

    public ASEntity withEntityReference(String entityType, String uniqueId) {
        put(ASConstants.FIELD_ENTITY_REF, new EntityReference(entityType, uniqueId));
        return this;
    }

    public ASEntity withEntityReference(EntityReference entityReference) {
        put(ASConstants.FIELD_ENTITY_REF, entityReference);
        return this;
    }

    @Override
    public ASEntity withRelation(Relation relation, BaseStreamItem root) {
        super.withRelation(relation, root);
        return this;
    }

    @Override
    public ASEntity withRelation(String type, Object value) {
        super.withRelation(type, value);
        return this;
    }

    @Override
    public ASEntity withRelations(String type, Object value) {
        super.withRelations(type, value);
        return this;
    }

    @Override
    public ASEntity withProperties(Object... properties) {
        super.withProperties(properties);
        return this;
    }

    @Override
    public ASEntity withProperties(String property, Object value) {
        super.withProperties(property, value);
        return this;
    }

    @Override
    public ASEntity withRelationIfValid(String type, String entityType, String entityId) {
        super.withRelationIfValid(type, entityType, entityId);
        return this;
    }

    @Override
    public ASEntity withRelationIfValid(String type, String entityType, String entityId, Map<String, Object> relationsProperties) {
        super.withRelationIfValid(type, entityType, entityId, relationsProperties);
        return this;
    }

    public ASEntity withRelationIfValid(String type, String entityType, String entityId, Object... properties) {
        Map<String, Object> propertiesMap = new LinkedHashMap<>();
        for (int i = 0; i < properties.length; i += 2) {
            propertiesMap.put(properties[i].toString(), properties[i + 1]);
        }
        return withRelationIfValid(type, entityType, entityId, propertiesMap);
    }

    @Override
    public ASEntity withRelationIfValid(String type, EntityReference entityRef) {
        super.withRelationIfValid(type, entityRef);
        return this;
    }

    /**
     * Add an aspect to the entity.
     * Aspects are standard schema peaces that can be added to entities
     *
     * @param aspect the Entity Aspect to add
     * @return this ASEntity for chaining purposes
     */
    public ASEntity withAspect(AspectInterface aspect) {
        if (aspect != null && !aspect.isEmpty()) {
            aspect.setRoot(this);
            super.addAspect(aspect, this);
        }
        return this;
    }

    @Override
    public ASEntity setNow() {
        super.setNow();
        return this;
    }

    public ASEntity withOccurredAt(DateTime timestamp) {
        if (timestamp != null) put(ASConstants.FIELD_OCCURRED_AT, timestamp);
        else remove(ASConstants.FIELD_OCCURRED_AT);
        return this;
    }

    public ASEntity withOccurredAt(String timestamp) {
        if (timestamp != null) put(ASConstants.FIELD_OCCURRED_AT, DateTime.parse(timestamp));
        else remove(ASConstants.FIELD_OCCURRED_AT);
        return this;
    }

    @Override
    @Deprecated
    public ASEntity withPresentation(String label, String thumbnail, String icon, String description, String detailsUrl) {
        super.withPresentation(label, thumbnail, icon, description, detailsUrl);
        return this;
    }

    /**
     * Partitions are used to separate data into storage containers/partitions
     * A sub-tenant can only see data belonging in his partition or partitions he has access to
     * The "_common" partition is used for entities belonging to everyone.
     * Email, Phone Number etc. are good examples of entities which should be stored in the "_common" partition
     * Entities inherit the partition from the Event that creates them if no partition is specified.
     * Entities can only be stored in one partition
     *
     * @param partition the partition that the entity should be stored in
     * @return this ASEntity for chaining purposes
     */
    public ASEntity withPartition(String partition) {
        put(ASConstants.FIELD_PARTITION, partition);
        return this;
    }

    public ASEntity withDimension(String dimension, String value) {
        super.addDimension(dimension, value, this);
        return this;
    }

    @Override
    public ASEntity withMetric(String metric, Number value) {
        super.withMetric(metric, value);
        return this;
    }

    @Override
    public ASEntity withMetrics(Object... metrics) {
        super.withMetrics(this, metrics);
        return this;
    }

    public ASEntity withDeletedFlag(boolean deleted) {
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

    public void send() throws Exception {
        ASService.send(this);
    }

}
