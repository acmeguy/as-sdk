package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.interfaces.*;
import com.activitystream.core.model.stream.AbstractBaseEvent;
import com.activitystream.core.model.stream.TransactionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemsManager extends AbstractListAspect<TransactionEvent> implements CanContainSubEvents, EnrichableElement, LinkedElement {

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

    public ItemsManager addLine(TransactionEvent event) {
        event.setRoot(getRoot());
        add(event);
        return this;
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    public ItemsManager mergeItemLine(TransactionEvent newLine) {
        for (TransactionEvent existingLine : this) {
            if (existingLine.equals(newLine)) {
                existingLine.setItemCount(existingLine.getItemCount() + newLine.getItemCount());
                if (newLine.hasLineIds()) existingLine.addToLineIds(newLine.getLineIds());
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

    /*
    public static ItemsManager items() {
        return new ItemsManager();
    }
    */

    /**
     * A reference to the parent event is temporarily required
     * @param root
     * @return
     */
    public static ItemsManager items(BaseStreamItem root) {
        ItemsManager itemsManager = new ItemsManager();
        itemsManager.setRoot(root);
        return itemsManager;
    }
}
