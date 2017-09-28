package com.activitystream.model.interfaces;

import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.security.SecurityScope;

public interface BaseStreamElement extends VerifiableElement {

    /************  Utility Functions ************/

    //void setRootElement(SavableElement rootElement);

    void setRoot(BaseStreamElement root);

    void setSecurityScope(SecurityScope scope);

    BaseStreamElement getRoot();

    BusinessEntity getRootBusinessEntity();

    /************  Persistence ************/

    //SavableElement save(Tenant tenant, PersistenceCache cache);

    /**
     * Check for all embedded entities references and replaces them with the full entity
     */
    /*
    default void expand(Tenant tenant, StreamItemAccessPolicy accessPolicy) {
    }
    */

    /**
     * Remove nested structure
     */
    default void simplify() {
    }

}
