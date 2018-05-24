package com.activitystream.core.model.aspects;

import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.sdk.ASConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerPermissionAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_CUSTOMER_PERMISSION, CustomerPermissionAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(CustomerPermissionAspect.class);

    public CustomerPermissionAspect() {
    }

    public CustomerPermissionAspect(boolean marketingPermission, boolean informationPermission, boolean processingPermission) {
        withMarketingPermission(marketingPermission);
        withInformationPermission(informationPermission);
        withProcessingPermission(processingPermission);
    }

    @Override
    public void loadFromValue(Object value) {
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    public CustomerPermissionAspect withMarketingPermission(boolean marketingPermission) {
        put(ASConstants.FIELD_MARKETING_PERMISSION, marketingPermission);
        return this;
    }

    public CustomerPermissionAspect withInformationPermission(boolean informationPermission) {
        put(ASConstants.FIELD_INFORMATION_PERMISSION, informationPermission);
        return this;
    }

    public CustomerPermissionAspect withProcessingPermission(boolean processingPermission) {
        put(ASConstants.FIELD_PROCESSING_PERMISSION, processingPermission);
        return this;
    }

    public boolean getMarketingPermission() {
        return (boolean) get(ASConstants.FIELD_MARKETING_PERMISSION);
    }

    public boolean getInformationPermission() {
        return (boolean) get(ASConstants.FIELD_INFORMATION_PERMISSION);
    }

    public boolean getProcessingPermission() {
        return (boolean) get(ASConstants.FIELD_PROCESSING_PERMISSION);
    }

    @Override
    public Object put(Object key, Object value) {
        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();

        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_MARKETING_PERMISSION:
            case ASConstants.FIELD_INFORMATION_PERMISSION:
            case ASConstants.FIELD_PROCESSING_PERMISSION:
                value = validator().processBoolean(theKey, value, true);
                break;
        }

        return super.put(theKey, value);
    }
}
