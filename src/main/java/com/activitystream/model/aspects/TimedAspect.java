package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.utils.Slugify;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TimedAspect extends AbstractMapAspect {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_TIMED, TimedAspect::new, AspectType.MergeStrategy.MERGE);

    protected static final Logger logger = LoggerFactory.getLogger(TimedAspect.class);

    private TimeComponent legacyComponent;

    public TimedAspect() {
    }

    public TimedAspect(String type, Long duration) {
        this(type, new DateTime(duration), null);
    }

    public TimedAspect(String type, DateTime begins, DateTime ends) {
        setRoot(root);
        if (type != null && !type.isEmpty()) {
            if (begins != null) put(ASConstants.FIELD_BEGINS, begins);
            if (ends != null) put(ASConstants.FIELD_ENDS, ends);
            put(ASConstants.FIELD_TYPE, type);
        }
    }

    public TimedAspect(String type, String beginsTimestamp, String endsTimestamp) {
        setRoot(root);
        if (type != null && !type.isEmpty()) {
            put(ASConstants.FIELD_TYPE, type);
            if (beginsTimestamp != null) put(ASConstants.FIELD_BEGINS, DateTime.parse(beginsTimestamp.replaceAll(" ","T")));
            if (endsTimestamp != null) put(ASConstants.FIELD_ENDS, DateTime.parse(endsTimestamp.replaceAll(" ","T")));
        }
    }

    /************
     * Utility functions
     ************/

    private void combineTimeZone(Map<String, Object> map, String timeKey, String zoneKey) {
        if (map.containsKey(zoneKey)) {
            map.put(timeKey, ((DateTime) map.get(timeKey)).withZone(DateTimeZone.forID((String) map.remove(zoneKey))));
        }
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public DateTime getBegins() {
        if (isEmpty()) return null;
        return ((TimeComponent) values().iterator().next()).getBegins();
    }

    public String getType() {
        return (String) keySet().iterator().next();
    }

    public DateTime getEnds() {
        if (isEmpty()) return null;
        return ((TimeComponent) values().iterator().next()).getEnds();
    }

    public Long getDuration() {
        if (isEmpty()) return null;
        return ((TimeComponent) values().iterator().next()).getDuration();
    }

    public TimeComponent getComponent(String type) {
        return (TimeComponent) get(type);
    }

    /************ Assignment & Validation ************/

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = Slugify.asSlug(theKey);
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        if (value instanceof Map) {
            return super.put(theKey, new TimeComponent((Map) value, getRoot()));
        } else {
            switch (theKey) {
                case ASConstants.FIELD_TYPE:
                    return setType(validator().processString(theKey, value, false));
                default:
                    return getLegacyComponent().put(key, value);
            }
        }
    }

    /**
     * Adds period information to and entity
     * Some period types have defined meaning like "begins" which tells when an events begins (or the duration of an event)
     * @param type the period type/name
     * @param begins the date time when the period begins
     * @return the timed aspect for chaining
     */
    public TimedAspect withPeriod(String type, String begins) {
        return withPeriod(type, begins, null, null);
    }

    /**
     * Adds period information to and entity
     * Some period types have defined meaning like "begins" which tells when an events begins (or the duration of an event)
     * @param type the period type/name
     * @param begins the date time when the period begins in a ISO format
     * @param duration the duration of the event in seconds
     * @return the timed aspect for chaining
     */
    public TimedAspect withPeriod(String type, String begins, Long duration) {
        return withPeriod(type, begins, null, duration);
    }

    /**
     * Adds period information to and entity
     * Some period types have defined meaning like "begins" which tells when an events begins (or the duration of an event)
     * @param type the period type/name
     * @param begins the date time when the period begins in a ISO format
     * @param ends the date time when the period ends in a ISO format
     * @return the timed aspect for chaining
     */
    public TimedAspect withPeriod(String type, String begins, String ends) {
        return withPeriod(type, begins, ends, null);
    }

    /**
     * Adds period information to and entity
     * Some period types have defined meaning like "begins" which tells when an events begins (or the duration of an event)
     * @param type the period type/name
     * @param begins the date time when the period begins in a ISO format
     * @param ends the date time when the period ends in a ISO format
     * @param duration the duration of the event in seconds
     * @return the timed aspect for chaining
     */
    public TimedAspect withPeriod(String type, String begins, String ends, Long duration) {
        LinkedHashMap<String,Object> tComponent = new LinkedHashMap();
        if (begins != null && !begins.isEmpty()) tComponent.put("begins",begins);
        if (ends != null && !ends.isEmpty()) tComponent.put("ends",ends);
        if (duration != null) tComponent.put("ends",duration);
        TimeComponent timeComponent = new TimeComponent(tComponent,this);
        put(type, timeComponent);
        return this;
    }

    private Object setType(String type) {
        values().remove(getLegacyComponent());
        super.put(type, getLegacyComponent());
        return null;
    }

    private TimeComponent getLegacyComponent() {
        if (legacyComponent == null) {
            legacyComponent = new TimeComponent(null, getRoot());
            super.put("", legacyComponent);
        }
        return legacyComponent;
    }

    @Override
    public void verify() {
        if (legacyComponent != null)
            legacyComponent.verify();
    }

    /************  Persistence ************/

    @Override
    protected void collectValuesToSave(Map<String, Object> values) {

        if (!values.isEmpty()) {
            String type = StringUtils.defaultString((String) values.remove(ASConstants.FIELD_TYPE));
            if (!isEmpty()) {
                String newType = (String) keySet().iterator().next();
                if (!StringUtils.isEmpty(newType))
                    type = newType;
            }
            TimeComponent oldComponent = new TimeComponent(values, getRoot());
            values.clear();
            values.put(type, oldComponent);

            if (legacyComponent != null) {
                setType(type);
                legacyComponent.mergeFromSaved(oldComponent);
            }
        }

        super.collectValuesToSave(values);

        if (!values.isEmpty()) {
            String type = values.keySet().iterator().next();
            TimeComponent component = (TimeComponent) values.values().iterator().next();
            values.clear();
            values.putAll(component);
            if (!StringUtils.isEmpty(type))
                values.put(ASConstants.FIELD_TYPE, type);
        }

        extractTimeZone(values, ASConstants.FIELD_BEGINS, "btz");
        extractTimeZone(values, ASConstants.FIELD_ENDS, "etz");
    }

    private void extractTimeZone(Map<String, Object> values, String dateKey, String zoneKey) {
        if (values.get(dateKey) == null)
            values.remove(zoneKey);
        else
            values.put(zoneKey, ((DateTime) values.get(dateKey)).getZone().getID());
    }

    /**
     * Creates a new Timed Aspect instance
     * Utility function for cleaner chaining
     * @return a new Times Aspect
     */
    public static TimedAspect timed() {
        return new TimedAspect();
    }

}
