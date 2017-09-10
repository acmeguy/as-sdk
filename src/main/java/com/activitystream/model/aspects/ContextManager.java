package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.LinkedElement;
import com.activitystream.model.relations.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ContextManager extends AbstractListAspect<Relation> implements LinkedElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_CONTEXT, ContextManager::new) {
        /*
        @Override
        public boolean isActive(SavableElement element, BaseStreamElement root) {
            return ((OrientVertex) element).countEdges(Direction.BOTH, ASConstants.REL_CONTEXT) > 0;
        }
        */
    };

    protected static final Logger logger = LoggerFactory.getLogger(ContextManager.class);

    public ContextManager() {
    }

    @Override
    public void loadFromValue(Object eventRelations) {
        if (eventRelations instanceof Map) {
            //logger.warn("eventRelation MAP: " + eventRelations);
            add(new Relation(eventRelations, getRoot()));
        } else if (eventRelations instanceof List) {
            ((List) eventRelations).forEach(eventRelation -> {
                //logger.warn("eventRelation: " + eventRelation);
                add(new Relation(eventRelation, getRoot()));
            });
        } else {
            logger.warn("Context elements: " + eventRelations);
        }
        verify();
    }


    /************
     * Utilities
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {

    }

    /************  Persistence ************/

}
