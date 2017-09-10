package com.activitystream.model.analytics;

import com.activitystream.model.ASConstants;
import com.activitystream.model.entities.EntityReference;
import org.apache.avro.specific.SpecificRecordBase;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Helps build time-series entries (Data points)
 */
public class TimeSeriesEntry extends LinkedHashMap<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(TimeSeriesEntry.class);

    private SpecificRecordBase avroRecord;
    private List<RegisteredTSPart> registeredParts = new LinkedList<>();
    private List<RegisteredReference> registeredReferences = new LinkedList<>();

    public TimeSeriesEntry(String timeSeriesType, String timeSeries) {
        super();
        put(ASConstants.FIELD_TIME_SERIES_TYPE, timeSeriesType);
        put(ASConstants.FIELD_TIME_SERIES, timeSeries);
    }

    public TimeSeriesEntry(String timeSeriesType, String timeSeries, SpecificRecordBase avroRecord) {
        super();
        put(ASConstants.FIELD_TIME_SERIES_TYPE, timeSeriesType);
        put(ASConstants.FIELD_TIME_SERIES, timeSeries);
        this.avroRecord = avroRecord;
    }

    public String getTimeSeriesType() {
        return (String) get(ASConstants.FIELD_TIME_SERIES_TYPE);
    }

    public String getTimeSeries() {
        return (String) get(ASConstants.FIELD_TIME_SERIES);
    }

    public DateTime getOccurredAt() {
        return (DateTime) get("occurred_at");
    }

    @Override
    public String toString() {
        if (getTimeSeries() != null) return getTimeSeriesType() + "/" + getTimeSeries();
        else return getTimeSeriesType();
    }

    public void setAvroRecord(SpecificRecordBase avroRecord) {
        this.avroRecord = avroRecord;
    }

    public SpecificRecordBase asAvroRecord() {
        if (this.avroRecord == null) {
            //logger.warn("Create Avro Record for " + this.timeSeriesName);
            switch (this.getTimeSeriesType()) {
                case ASConstants.TIME_SERIES_EVENTS:
                    avroRecord = eventAsAvroRecord();
                    break;
                case "pageviews":
                    avroRecord = pageviewAsAvroRecord();
                    break;
                case "observations":
                    try {
                        avroRecord = observationAsAvroRecord();
                    } catch (Exception e) {
                        logger.warn("Error creating obs avro: " + e, e);
                    }
                    break;
                default:
                    logger.warn("Avro mapping missing for: " + this.getTimeSeriesType());
                    break;

            }
        }
        return avroRecord;
    }

    public void registerPart(String partName, Long weight, SpecificRecordBase registeredPart) {
        //logger.warn("partName: " + partName +  ", weight: " + weight);
        registeredParts.add(new RegisteredTSPart(partName, weight, registeredPart));
    }

    public void setRegisteredReferences(String role, Long weight, EntityReference reference) {
        //logger.warn("role: " + role +  ", weight: " + weight + ", " + reference);
        registeredReferences.add(new RegisteredReference(role, weight, reference));
    }

    public List<CharSequence> getInvolves() {
        List<CharSequence> references = new LinkedList<>();

        this.registeredReferences.forEach(registeredReference -> {
            String ref = registeredReference.getRegisteredReference().getEntityReference();
            if (!references.contains(ref))
                references.add(ref);
        });

        return references;
    }

    public EntityReference getPrimaryEntityReference(String roleName) {
        EntityReference primaryReference = null;
        long wasWeight = -3;

        for (RegisteredReference ref : registeredReferences) {
            if (ref.getRole().equals(roleName)) {
                if (ref.getWeight() > wasWeight) {
                    primaryReference = ref.getRegisteredReference();
                    wasWeight = ref.getWeight();
                }
            }
        }
        return primaryReference;
    }

    public SpecificRecordBase getPrimaryPart(String partName) {
        SpecificRecordBase primaryPart = null;
        long wasWeight = -3;

        for (RegisteredTSPart part : registeredParts) {
            if (part.getPart().equals(partName)) {
                if (part.getWeight() > wasWeight) {
                    primaryPart = part.getRegisteredPart();
                    wasWeight = part.getWeight();
                }
            }
        }
        return primaryPart;
    }

    private SpecificRecordBase observationAsAvroRecord() {
        //return AvroUtilities.observationAsAvroRecord(this);
        throw new NotImplementedException();
        //return null
    }

    private SpecificRecordBase pageviewAsAvroRecord() {
        throw new NotImplementedException();
        //return AvroUtilities.pageviewAsAvroRecord(this);
    }

    private SpecificRecordBase eventAsAvroRecord() {
        throw new NotImplementedException();
        //return AvroUtilities.eventAsAvroRecord(this);
    }
}


