package com.activitystream.model.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public interface EnrichableElement extends LinkedElement {

    /**
     * Goes through all aspects and entities and checks if they require enrichment service
     * It passes back a list of completable futures for any async lookups that may need to be run
     */
    //List<Future<Object>> enrich(Tenant tenant, ServiceFrontend enrichmentFrontend, Collection<String> limitTo);

    /**
     * Once the async enrichment is done this is called with the results
     */
    //void enrichWith(Object result, Tenant tenant);

}
