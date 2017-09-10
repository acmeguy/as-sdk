package com.activitystream.model.exceptions;

import com.activitystream.model.ASConstants;
import com.activitystream.model.validation.CoreException;
import com.google.common.collect.ImmutableMap;

public class WrongStreamItemTypeException extends CoreException {

    public WrongStreamItemTypeException(String streamId, String expectedType, String actualType) {
        super("Found stream item of different type than expected", ImmutableMap.of(
                ASConstants.FIELD_STREAM_ID, streamId,
                "expected_type", expectedType,
                "actual_type", actualType));
    }
}
