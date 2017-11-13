package com.activitystream.core.model.security;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.validation.CoreException;
import com.google.common.collect.ImmutableMap;

public class InvalidReferenceException extends CoreException {

    public InvalidReferenceException(EntityReference entityReference) {
        super("Invalid entity reference", ImmutableMap.of(ASConstants.FIELD_ENTITY_REF, entityReference));
    }
}
