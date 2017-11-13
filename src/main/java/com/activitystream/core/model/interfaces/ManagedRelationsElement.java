package com.activitystream.core.model.interfaces;

import com.activitystream.core.model.relations.RelationsManager;

public interface ManagedRelationsElement {

    String getRelationsRootType();

    RelationsManager getRelationsManager(boolean create);

    RelationsManager getRelationsManager();

    boolean hasRelations();

}
