package com.activitystream.model.interfaces;

import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.security.SecurityScope;
import com.activitystream.model.utils.ElementTraversal;

import java.util.function.Consumer;

public interface LinkedElement {

    enum LINK_DETAILS {
        FULL,
        LIST,
        LIST_DETAILED
    }


    /************  Utility Functions ************/

    /**
     * Low-level element traversal. Should probably not be used outside the implementation of {@link ElementTraversal}.
     */
    boolean traverse(ElementVisitor visitor);

    /**
     * Higher level element traversal, providing a somewhat stream-like interface for manipulating nested elements.
     */
    default ElementTraversal traversal() {
        return new ElementTraversal(this);
    }

    default void onEachEntityRelation(Consumer<Relation> action) {
    }

    default void onEachRelationType(StreamItemRelationTypeConsumer action) {
    }

    default void onEachEntityReference(Consumer<EntityReference> action) {
    }

    void setSecurityScope(SecurityScope scope);

    //void createApiLinks(Tenant tenant, LINK_DETAILS details);

    @FunctionalInterface
    interface ElementVisitor {

        boolean visit(LinkedElement linkedElement);
    }

    @FunctionalInterface
    interface StreamItemRelationTypeConsumer {

        void accept(String relationType, String outType, String inType);
    }
}
