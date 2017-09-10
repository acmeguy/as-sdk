package com.activitystream.model.interfaces;

import com.activitystream.model.aspects.AspectType;
import com.activitystream.model.entities.EntityReference;

import java.util.Collection;

public interface AspectInterface extends EmbeddedStreamElement {

    void loadFromValue(Object value);

    //void loadFromElement(SavableElement element, List<Map<String, EntityReference>> path, StreamItemAccessPolicy accessPolicy);

    default void visited(Collection<EntityReference> visited) {}

    void mergeAspect(AspectInterface aspect);

    AspectType getAspectType();

    void markAsAutoGenerated();

    boolean isAutoGenerated();

    boolean isEmpty();

    default boolean isInherited() {
        return false;
    }

}
