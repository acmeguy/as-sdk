package com.activitystream.model.interfaces;


/**
 * A stream element that is not saved by itself, but only as part of a parent element.
 */
public interface EmbeddedStreamElement extends BaseStreamElement {

    /**
     * Called before the parent element is saved. Fields to be saved as part of the parent element should be added to properties.
     */
    //void preSave(StreamItemChangeSet changeSet, Tenant tenant, SavableElement target, PersistenceCache cache);

    /**
     * Called after the parent element is saved. Can be used to add edges to the parent element.
     */
    //void postSave(SavableElement target, Tenant tenant, PersistenceCache cache);
}
