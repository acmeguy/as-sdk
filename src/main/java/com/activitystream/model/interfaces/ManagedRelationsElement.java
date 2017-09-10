package com.activitystream.model.interfaces;

import com.activitystream.model.relations.RelationsManager;

public interface ManagedRelationsElement {

    String getRelationsRootType();

    RelationsManager getRelationsManager(boolean create);

    RelationsManager getRelationsManager();

    boolean hasRelations();

}
