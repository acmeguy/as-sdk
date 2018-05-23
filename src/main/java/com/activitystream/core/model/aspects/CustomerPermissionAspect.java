package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;

public class CustomerPermissionAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_CUSTOMER_PERMISSION, CustomerPermissionAspect::new);

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }
}
