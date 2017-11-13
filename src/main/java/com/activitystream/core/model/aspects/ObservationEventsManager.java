package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.interfaces.*;
import com.activitystream.core.model.stream.CustomerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ObservationEventsManager extends AbstractListAspect<CustomerEvent> implements CompactableElement, LinkedElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_OBS_EVENTS, ObservationEventsManager::new) {
        /*
        @Override
        public boolean isActive(SavableElement element, BaseStreamElement root) {
            return ((OrientVertex) element).countEdges(Direction.BOTH, ASConstants.REL_OBS_EVENTS) > 0;
        }
        */
    };

    protected static final Logger logger = LoggerFactory.getLogger(ObservationEventsManager.class);

    public ObservationEventsManager() {
    }

    @Override
    public void loadFromValue(Object subEvents) {
        if (subEvents instanceof List) {
            ((List) subEvents).forEach(subEvent -> {
                if (subEvent instanceof Map) add(new CustomerEvent((Map) subEvent));
            });
        } else {
            logger.warn("Context elements: " + subEvents);
        }
        verify();
    }


    /************
     * Utilities
     ************/

    @Override
    public void compact() {

    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {

    }
}
