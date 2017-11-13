package com.activitystream.core.model.interfaces;

import java.util.Collection;

public interface DynamicAspect {

    String getDocumentClassName();

    Collection<String> getFieldNames();

    //OType getValueType();
    Object getValueType();
}
