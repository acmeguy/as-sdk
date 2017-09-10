package com.activitystream.model.stream;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.relations.RelationsManager;
import com.activitystream.model.relations.RelationsType;
import com.activitystream.model.security.ProcessSettings;
import com.activitystream.model.validation.InvalidPropertyContentError;
import com.activitystream.model.validation.MissingPropertyError;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public abstract class AbstractBaseEvent extends AbstractStreamItem implements BaseStreamItem, EnrichableElement, ManagedRelationsElement {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseEvent.class);

    private static final DateTimeFormatter fmt_datetime = ISODateTimeFormat.dateTime();
    private static final List<String> basicItemProperties =
            Arrays.asList(ASConstants.FIELD_OCCURRED_AT, ASConstants.FIELD_ORIGIN, ASConstants.FIELD_PARTITION, ASConstants.FIELD_IMPORTANCE,
                    ASConstants.FIELD_DESCRIPTION);
    private static final List<String> extraItemProperties =
            Arrays.asList(ASConstants.FIELD_PROPERTIES, ASConstants.FIELD_RECEIVED_AT, ASConstants.FIELD_REGISTERED_AT);

    private ProcessSettings processSettings = null;
    private String messageKey = null;
    TimeSeriesEntry eventTimeSeries = null;

    AbstractBaseEvent() {
        super();
    }

    AbstractBaseEvent(Map map, BaseStreamElement root) {
        super(map, root);

        Date at = new Date();
        if (!containsKey(ASConstants.FIELD_OCCURRED_AT)) {
            DateTime rootOccurredAt = root instanceof AbstractBaseEvent ? ((AbstractBaseEvent) root).getOccurredAt() : null;
            put(ASConstants.FIELD_OCCURRED_AT, rootOccurredAt != null ? rootOccurredAt : new DateTime(at));
        }
        put(ASConstants.FIELD_RECEIVED_AT, new DateTime(at));
    }

    public List<String> getBasicProperties() {
        return basicItemProperties;
    }

    public List<String> getExtraItemProperties() {
        return extraItemProperties;
    }

    @Override
    public String getRelationsRootType() {
        return ASConstants.FIELD_INVOLVES;
    }

    @Override
    public RelationsManager getRelationsManager(boolean create) {
        if (get(getRelationsRootType()) == null && create) {
            super.put(getRelationsRootType(), new RelationsManager(null, this));
        }
        return (RelationsManager) get(getRelationsRootType());
    }

    @Override
    public RelationsManager getRelationsManager() {
        return getRelationsManager(false);
    }

    @Override
    public boolean hasRelations() {
        return containsKey(getRelationsRootType());
    }

    /************ Utility Functions ************/

    /**
     * Simplifies the entity (used when serialized from graph and before using it as a re-usable message
     */
    public void clean() {
        this.remove("_dir");
        this.remove("_stream_id");
        this.remove("_links");
        this.remove("_update_at");

        for (Relation relation : getRelationsManager()) {
            relation.remove("_dir");
            relation.remove("_stream_id");
            relation.remove("_links");
            relation.remove("_update_at");
            if (relation.containsKey("_rel_path")) {
                String relPath = (String) relation.remove("_rel_path");
                if (relPath.contains(":")) {
                    relation.directPut(relPath, relation.remove(relPath.substring(relPath.lastIndexOf(":") + 1)));
                    relation.setRelationsType(new RelationsType(relPath));
                }
            }
            if (relation.getRelatedEntityItem() instanceof BusinessEntity) {
                BusinessEntity relEntity = (BusinessEntity) relation.getRelatedEntityItem();
                relEntity.remove("relations");
                relEntity.remove("_update_at");
                relEntity.remove("_stream_id");
                if (relEntity.getAspectManager() != null) {
                    relEntity.getAspectManager().remove("inventory");
                    relEntity.getAspectManager().remove(ASConstants.ASPECTS_STATUS);
                }
                //relEntity.remove("aspects");
            }
        }
    }

    @Override
    public String getFootprint() {
        return getFootprint(this.getOccurredAt(), null);
    }

    public String getFootprint(DateTime occurredAt, String granularity) {

        //round/ceil the occurred at if there is granularity involved
        if (granularity != null && occurredAt != null) {
            //todo - calculate ceil time for period setting
            Duration periodDuration = Period.parse(granularity).toStandardDuration();
            long t = (occurredAt.getMillis() / periodDuration.getMillis()) * periodDuration.getMillis();
            occurredAt = new DateTime(t).withDurationAdded(periodDuration, 1).withZone(occurredAt.getZone());
        }

        //Takes all relation directly related to the event and adds them to the footprint
        if (occurredAt != null && this.getOrigin() != null && this.getType() != null) {
            StringBuilder footprint = new StringBuilder().append(this.getType()).append("--")
                    .append(fmt_datetime.print(occurredAt.toDateTime(DateTimeZone.UTC).toLocalDateTime())).append("--").append(this.getOrigin());

            TreeSet<String> set = new TreeSet<>(); //makse sure the references are sorted
            traversal().forEachEntityRelation(relation -> {
                if (relation.getRoot() == this || (relation.getRoot().getRoot() != null && relation.getRoot().getRoot() == this)) {
                    set.add(relation.getFootprint());
                }
            });

            for (String reference : set) footprint.append("--").append(reference); //added post sorting
            //logger.warn("footprint: " + footprint.toString().toLowerCase());
            return footprint.toString().toLowerCase();
        }
        return null;
    }

    @Override
    public List<String> getAllowedRelTypes() {
        return ASConstants.EVENT_RELATIONS;
    }

    @Override
    public String getMessageKey() {
        return (this.messageKey != null) ? this.messageKey : (String) get(ASConstants.FIELD_TYPE);
    }

    public abstract EventTypeReference getEventType();

    /************
     * Processing
     ************/

    @Override
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public ProcessSettings getProcessSettings() {
        return processSettings;
    }

    public void setProcessSettings(ProcessSettings processSettings) {
        this.processSettings = processSettings;
    }

    /************ Enrichment & Analytics ************/

    /*
    @Override
    public void expand(Tenant tenant, StreamItemAccessPolicy accessPolicy) {
        for (AbstractBaseEvent subEvent : getSubEvents()) {
            subEvent.expand(tenant, accessPolicy);
        }
        if (getRelationsManager() != null) getRelationsManager().expand(tenant, accessPolicy);
    }
    */

    @Override
    public void simplify() {
        if (getAspectManager() != null) getAspectManager().simplify();
        if (getRelationsManager() != null) getRelationsManager().simplify();
    }

    @Override
    public String getTimeSeriesType() {
        return ASConstants.TIME_SERIES_EVENTS;
    }

    @Override
    public List<TimeSeriesEntry> getAllTimeSeriesEntries() {
        if (containsKey("_timeseries")) {
            return (List<TimeSeriesEntry>) get("_timeseries");
        }
        List<TimeSeriesEntry> allTimeSeriesEntries = super.getAllTimeSeriesEntries();
        Map baseMap = getTimeSeriesMap();
        for (TimeSeriesEntry entry : allTimeSeriesEntries) {
            addTimeSeriesDimensions(entry);
            entry.putAll(baseMap);
            super.addTimeSeriesDimensions(entry); //Old populator (Kept for the time being)
            super.populateTimeSeriesEntry(entry, "root", 0);  //New time-series adder
        }
        //allTimeSeriesEntries.add(new TimeSeriesEntry(TIME_SERIES_BAMM, null, toBamm()));
        put("_timeseries", allTimeSeriesEntries);

        return allTimeSeriesEntries;
    }

    @Override
    public void addAllTimeSeriesEntries(List<TimeSeriesEntry> entries, boolean checkNested) {
        entries.addAll(getAllTimeSeriesEntries());
        if (checkNested) {
            for (AbstractStreamItem subEvent : getSubEvents()) {
                TransactionEvent transEvent = (TransactionEvent) subEvent;
                transEvent.detach();
                entries.addAll(transEvent.getAllTimeSeriesEntries());
            }
        }
    }

    public TimeSeriesEntry getAllTimeSeriesEntriesByType(String type) {
        for (TimeSeriesEntry entry : getAllTimeSeriesEntries()) {
            if (entry.getTimeSeriesType().equals(type)) {
                return entry;
            }
        }
        return null;
    }

    public Map<String, Object> getTimeSeriesMap() {
        Map<String, Object> baseValues = new LinkedHashMap<>();

        Object date = get(ASConstants.FIELD_OCCURRED_AT);
        DateTime occurredAtOrg = (date instanceof DateTime) ? ((DateTime) date) : new DateTime(date);
        baseValues.put(ASConstants.FIELD_OCCURRED_AT, occurredAtOrg.withZone(DateTimeZone.UTC));
        if (!occurredAtOrg.getZone().equals(DateTimeZone.UTC)) {
            baseValues.put(ASConstants.FIELD_OCCURRED_AT_LTOD, occurredAtOrg.toLocalDateTime().toDate().getTime());
        }
        baseValues.put(ASConstants.FIELD_STREAM_ID, getStreamId());

        baseValues.put(ASConstants.FIELD_ORIGIN, get(ASConstants.FIELD_ORIGIN));
        baseValues.put(ASConstants.FIELD_PARTITION, getPartition());
        baseValues.put(ASConstants.FIELD_TYPE, get(ASConstants.FIELD_TYPE));
        if (containsKey(ASConstants.FIELD_IMPORTANCE)) baseValues.put(ASConstants.FIELD_IMPORTANCE, get(ASConstants.FIELD_IMPORTANCE));
        return baseValues;
    }

    public TimeSeriesEntry getEventTimeSeriesEntry() {
        if (this.eventTimeSeries == null) {
            TimeSeriesEntry entry = new TimeSeriesEntry(ASConstants.TIME_SERIES_EVENTS, ASConstants.TIME_SERIES_EVENTS);
            populateTimeSeriesEntry(entry, "root", 0);
            this.eventTimeSeries = entry;
        }
        return this.eventTimeSeries;
    }

    public String getTarget() {
        if (this.eventTimeSeries != null) {
            EntityReference entityRef = this.eventTimeSeries.getPrimaryEntityReference("TARGET");
            if (entityRef != null) return entityRef.getEntityReference();
        }
        return getPrimaryRole("AFFECTS");
    }

    /*
    todo - implement elsewhere
    public GeoLocationAspectEntry getLocation() {
        if (this.eventTimeSeries != null) {
            return (GeoLocationAspectEntry) this.eventTimeSeries.getPrimaryPart("geo_location");
        }
        return null;
    }
    */

    protected String getPrimaryRole(String role) {
        Relation rel = getRelationsManager().getFirstRelationsOfType(role);
        if (rel != null) {
            BusinessEntity customerEnt = (BusinessEntity) rel.getRelatedItem();
            return customerEnt.getEntityReference().getEntityReference();
        }
        return null;
    }

    public Map<String, Object> getEventSummaryMap() {
        throw new NotImplementedException();
    }

    /************
     * Getters
     ************/
    public Integer getImportance() {
        return (Integer) get(ASConstants.FIELD_IMPORTANCE);
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {
        super.verify();
        if (!containsKey(ASConstants.FIELD_OCCURRED_AT)) put(ASConstants.FIELD_OCCURRED_AT, new DateTime(new Date()));
        if (!containsKey(ASConstants.FIELD_ORIGIN)) put(ASConstants.FIELD_ORIGIN, "unspecified");
        if (!containsKey(ASConstants.FIELD_TYPE)) addProblem(new MissingPropertyError("No message type was specified for this stream item"));

        RelationsManager relationsManager = getRelationsManager();
        if (relationsManager != null) {
            if (!relationsManager.hasRelationsOfType(ASConstants.REL_ACTOR))
                addProblem(new MissingPropertyError("Message contains no actor information."));
        } else {
            addProblem(new MissingPropertyError("Message contains no information regarding the entities involved."));
        }

        if (!(this instanceof NoEventType)) {
            if (!getEventType().isValidLeafType())
                addProblem(new InvalidPropertyContentError("'" + getEventType() + "' is not a valid event type for " + getClass().getSimpleName()));
        }
    }

    @Override
    public String getElementType() {
        return getEventType().getVertexTypeName();
    }

}
