package com.activitystream.model.security;

import com.activitystream.model.ASConstants;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.validation.CoreException;
import com.google.common.collect.ImmutableMap;

public class InvalidReferenceException extends CoreException {

    public InvalidReferenceException(EntityReference entityReference) {
        super("Invalid entity reference", ImmutableMap.of(ASConstants.FIELD_ENTITY_REF, entityReference));
    }
}
