package com.activitystream.core.model.exceptions;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.validation.CoreException;
import com.google.common.collect.ImmutableMap;

public class StreamItemNotFoundException extends CoreException {

    public StreamItemNotFoundException(String streamId) {
        super("Stream item not found", ImmutableMap.of(ASConstants.FIELD_STREAM_ID, streamId));
    }

    public StreamItemNotFoundException(String streamId, String entityRef) {
        super("Entity not found", ImmutableMap.of(
                ASConstants.FIELD_STREAM_ID, streamId,
                ASConstants.FIELD_ENTITY_REF, entityRef));
    }

    public StreamItemNotFoundException(EntityReference entityRef) {
        this(entityRef.getEntityStreamId().toString(), entityRef.getEntityReference());
    }
}
