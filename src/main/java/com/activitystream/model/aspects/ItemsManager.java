package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.stream.AbstractBaseEvent;
import com.activitystream.model.stream.TransactionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemsManager extends AbstractListAspect<TransactionEvent> implements CanContainSubEvents, AnalyticsElement, EnrichableElement, LinkedElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_ITEMS, ItemsManager::new) {
        /*
        @Override
        public boolean isActive(SavableElement element, BaseStreamElement root) {
            return ((OrientVertex) element).countEdges(Direction.OUT, ASConstants.REL_SUB_EVENT) > 0;
        }
        */
    };

    public ItemsManager() {
    }

    @Override
    public void loadFromValue(Object items) {
        if (items instanceof Map) {
            add(createSubEvent((Map) items));
        } else if (items instanceof List) {
            for (Object item : (List) items) {
                if (item != null) add(createSubEvent((Map) item));
            }
        }
        verify();
    }

    /************
     * Utility functions
     ************/

    private TransactionEvent createSubEvent(Map evenData) {
        return new TransactionEvent(evenData, root);
    }

    public void addSubEvent(Map eventData) {
        add(createSubEvent(eventData));
    }

    @Override
    public void simplify() {
        this.forEach(TransactionEvent::simplify);
    }

    /************ Enrichment & Analytics ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /**
     * Put primary items in the time series map
     *
     * @param entry
     */
    @Override
    public void addTimeSeriesDimensions(TimeSeriesEntry entry) {
        for (TransactionEvent item : this) {
            //if (relations.getRelatedEntityItem() instanceof BusinessEntity) {
            //    series.putAll(((BusinessEntity) relations.getRelatedEntityItem()).getEntityReference().asAnalyticsMap());
            // }
            //items.getAllEntityReferences(); //all references are here
        }
    }

    @Override
    public void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth) {
        //todo - control this so that it's only called for persisted items or after detaching
        /*
        for (TransactionEvent transactionEvent : (List<TransactionEvent>) this) {
            transactionEvent.populateTimeSeries(series, context, depth+1);
        }
        */
    }

    public ItemsManager mergeItemLine(TransactionEvent newLine) {
        for (TransactionEvent existingLine : this) {
            if (existingLine.equals(newLine)) {
                existingLine.addToItemCount(newLine.getItemCount());
                existingLine.setLineIds(newLine.getLineIds());
                return this;
            }
        }
        add(newLine);
        return this;
    }

    @Override
    public List<AbstractBaseEvent> getSubEvents() {
        List<AbstractBaseEvent> items = new LinkedList<>();
        for (AbstractBaseEvent item : this) items.add(item);
        return items;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {
        //for (TransactionEvent item : (List<TransactionEvent>) this) item.verify();
    }

}
