package com.activitystream.core.model.interfaces;

import com.activitystream.core.model.security.ProcessSettings;
import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface BaseStreamItem extends BaseStreamElement, EnrichableElement {

    /************ Stream Item Services ************/

    void setMessageKey(String messageKey);

    String getMessageKey();

    ProcessSettings getProcessSettings();

    String getPartition();

    UUID getStreamId();

    String getFootprint();

    String getElementType();

    DateTime getOccurredAt();

    DateTime getReceivedAt();

    default BaseStreamItem getParentStreamItem() {
        BaseStreamElement element = getRoot();
        while (element != null && element != this) {
            if (element instanceof BaseStreamItem)
                return (BaseStreamItem) element;
            if (element.getRoot() == element)
                break;
            element = element.getRoot();
        }
        return null;
    }

    boolean isUnSavable();

    void setUnSavable(boolean unSavable);

    boolean isRedundant();

    void setRedundant(boolean redundant);

    void prepareForFederation();

}
