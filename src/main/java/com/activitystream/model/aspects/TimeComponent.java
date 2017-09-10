package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.core.AbstractMapElement;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.stream.AbstractBaseEvent;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;






public class TimeComponent extends AbstractMapElement {

    private static final Logger logger = LoggerFactory.getLogger(TimeComponent.class);

    public TimeComponent(Map value, BaseStreamElement root) {
        super(value, root);
    }

    public TimeComponent() {
    }

    public DateTime getBegins() {
        return (DateTime) get(ASConstants.FIELD_BEGINS);
    }

    public DateTime getEnds() {
        return (DateTime) get(ASConstants.FIELD_ENDS);
    }

    public Long getDuration() {
        return (Long) get(ASConstants.FIELD_DURATION);
    }

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_BEGINS:
            case ASConstants.FIELD_ENDS:
                value = validator().processIsoDateTime(theKey, value, false);
                break;
            case "btz":
            case "etz":
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_DURATION:
                value = validator().processLong(theKey, value, false, null, null);
                break;
            default:
                this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Timed Aspect"));
                return null;
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {
        DateTime begins = (DateTime) get(ASConstants.FIELD_BEGINS);
        DateTime ends = (DateTime) get(ASConstants.FIELD_ENDS);
        Long duration = (Long) get(ASConstants.FIELD_DURATION);
        boolean changed = false;

        if (ends == null && (begins != null && duration != null)) {
            ends = begins.plus(duration);
            changed = true;
        }
        if (begins == null && ends != null && duration != null) {
            begins = ends.minus(duration);
            changed = true;
        }
        if (ends == null && begins == null && duration != null) {
            if (this.root instanceof AbstractBaseEvent) {
                AbstractBaseEvent stremItem = (AbstractBaseEvent) this.root;
                if (stremItem.getOccurredAt() != null) {
                    ends = stremItem.getOccurredAt();
                    begins = stremItem.getOccurredAt().minus(duration);
                    changed = true;
                } else {
                    ends = stremItem.getReceivedAt();
                    begins = stremItem.getReceivedAt().minus(duration);
                    changed = true;
                }
            } else {
                logger.warn("No parent message provided for defaults!");
            }
        }
        if (duration == null && begins != null && ends != null) {
            duration = ends.getMillis() - begins.getMillis();
            changed = true;
        }

        if (changed) {
            put(ASConstants.FIELD_BEGINS, begins);
            put(ASConstants.FIELD_ENDS, ends);
            put(ASConstants.FIELD_DURATION, duration);
        }
    }

    public void mergeFromSaved(TimeComponent oldComponent) {
        if (oldComponent == null) return;

        // If the beginning time changes, but we don't get a new duration or end time, the old duration and end time combined with the new beginning time
        // will not be consistent. To avoid this inconsistency, only keep previously saved values if values that are present are unchanged.
        if (hasNotChanged(oldComponent, ASConstants.FIELD_BEGINS) && hasNotChanged(oldComponent, ASConstants.FIELD_ENDS) &&
                hasNotChanged(oldComponent, ASConstants.FIELD_DURATION)) {
            oldComponent.forEach(this::putIfAbsent);
        }
    }

    private boolean hasNotChanged(TimeComponent other, String key) {
        return get(key) == null || other.get(key) == null || get(key).equals(other.get(key));
    }

    public static TimeComponent timeComponent() {
        return new TimeComponent();
    }
}
