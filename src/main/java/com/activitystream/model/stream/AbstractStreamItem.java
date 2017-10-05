package com.activitystream.model.stream;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.core.AbstractMapElement;
import com.activitystream.model.interfaces.CanContainSubEvents;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.relations.RelationsManager;
import com.activitystream.model.utils.StreamIdUtils;
import com.google.common.collect.ImmutableSet;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractStreamItem extends AbstractMapElement
        implements HasAspects, BaseStreamItem, CanContainSubEvents, InterestElement, ManagedRelationsElement {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStreamItem.class);
    private static final Set<String> IGNORED_PROPS =
            ImmutableSet.of(ASConstants.FIELD_ASPECTS, ASConstants.FIELD_INVOLVES, ASConstants.FIELD_ACL, ASConstants.FIELD_SERIAL_NUMBERS,
                    ASConstants.FIELD_STREAM_ID, ASConstants.FIELD_OCCURRED_AT, ASConstants.FIELD_RECEIVED_AT, ASConstants.FIELD_REGISTERED_AT,
                    ASConstants.FIELD_TYPE, ASConstants.FIELD_ORIGIN,
                    ASConstants.FIELD_PARTITION);
    List<String> allowedRelTypes = ASConstants.ENTITY_RELATIONS;
    List<String> allowedAspects = ASConstants.ALL_ASPECT_FIELDS;

    boolean redundant = false;
    boolean unSavable = false;

    public AbstractStreamItem() {
        super();
    }

    public AbstractStreamItem(Map map, BaseStreamElement root) {
        super(map, root);
    }

    public AbstractStreamItem(Map map) {
        super(map, null);
    }

    /************  Utilities ************/

    public boolean hasRelations() {
        return containsKey(getRelationsRootType());
    }

    public List<String> getAllowedRelTypes() {
        return allowedRelTypes;
    }

    public List<String> getAllowedAspects() {
        return allowedAspects;
    }

    @Override
    public List<AbstractBaseEvent> getSubEvents() {
        List<AbstractBaseEvent> items = new LinkedList<>();
        for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) this.entrySet()) {
            if (entry.getValue() instanceof CanContainSubEvents) {
                List<AbstractBaseEvent> subItems = ((CanContainSubEvents) entry.getValue()).getSubEvents();
                if (subItems != null && !subItems.isEmpty()) items.addAll(subItems);
            }
        }
        return items;
    }

    public Object withRelation(Relation relation) {
        this.getRelationsManager(true).addRelation(relation);
        return this;
    }

    public RelationsManager getRelationsManager(boolean create) {
        if (get(getRelationsRootType()) == null && create) {
            super.put(getRelationsRootType(), new RelationsManager(null, this));
        }
        return (RelationsManager) get(getRelationsRootType());
    }

    public RelationsManager getRelationsManager() {
        return getRelationsManager(false);
    }

    /************ Enrichment & Analytics ************/

    public String getTimeSeriesType() {
        return null;
    }

    @Override
    public List<TimeSeriesEntry> getAllTimeSeriesEntries() {
        List<TimeSeriesEntry> timeSeriesEntries = new LinkedList<>();
        if (getTimeSeriesType() != null) timeSeriesEntries.add(new TimeSeriesEntry(getTimeSeriesType(), null));
        return timeSeriesEntries;
    }

    /************ Access ************/

    @Override
    public DateTime getOccurredAt() {
        Object dt = get(ASConstants.FIELD_OCCURRED_AT);
        if (dt instanceof DateTime) return (DateTime) dt;
        else return new DateTime(dt);
    }

    public String getOrigin() {
        return (String) get(ASConstants.FIELD_ORIGIN);
    }

    public String getType() {
        String typeHandle = (String) get(ASConstants.FIELD_TYPE);
        if (typeHandle == null && containsKey(ASConstants.FIELD_TYPE + "_ref")) {
            typeHandle = ((EventTypeReference) get(ASConstants.FIELD_TYPE + "_ref")).toString();
        }
        return typeHandle;
    }

    @Override
    public String getPartition() {
        if (get(ASConstants.FIELD_PARTITION) != null)
            return (String) get(ASConstants.FIELD_PARTITION);
        BaseStreamItem parent = getParentStreamItem();
        return parent != null ? parent.getPartition() : null;
    }

    @Override
    public DateTime getReceivedAt() {
        return (DateTime) get(ASConstants.FIELD_RECEIVED_AT);
    }

    public DateTime getRegisteredAt() {
        return (DateTime) get(ASConstants.FIELD_REGISTERED_AT);
    }

    public UUID getStreamId() {
        //todo - optimize (this is being called often)

        if (this.get(ASConstants.FIELD_STREAM_ID) == null) {
            String footprint = getFootprint();
            if (footprint != null)
                super.put(ASConstants.FIELD_STREAM_ID, StreamIdUtils.calculateStreamId(footprint));
        }
        return (UUID) this.get(ASConstants.FIELD_STREAM_ID); //todo - could be cached and invalidated on any change
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {
        super.verify();
        if (getAspectManager() != null) getAspectManager().verify();
        if (getRelationsManager() != null) getRelationsManager().verify();
    }

    public boolean isRedundant() {
        return redundant;
    }

    public void setRedundant(boolean redundant) {
        this.redundant = redundant;
    }

    public boolean isUnSavable() {
        return unSavable;
    }

    public void setUnSavable(boolean unsavable) {
        this.unSavable = unsavable;
    }
}
