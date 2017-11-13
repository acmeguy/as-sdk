package com.activitystream.core.model.internal;

import com.activitystream.core.model.entities.EntityReference;

import java.io.Serializable;

/**
 * Reference to type of item that is stored as a vertex type in the OrientDB graph.
 */
public interface TypeReference<ReferenceT extends TypeReference<ReferenceT>> extends Comparable<ReferenceT>, Serializable {

    String getVertexTypeName();

    String getRootVertexTypeName();

    ReferenceT getSuperType();

    boolean hasSuperType();

    boolean isInternal();

    EntityReference asEntityReference();

    boolean isValidType();
}
