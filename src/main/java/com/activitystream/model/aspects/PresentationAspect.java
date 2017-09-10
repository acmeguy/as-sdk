package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PresentationAspect extends AbstractMapAspect {

    public static final AspectType ASPECT_TYPE =
            new AspectType.Embedded(ASConstants.ASPECTS_PRESENTATION, PresentationAspect::new, AspectType.MergeStrategy.MERGE) {
            };

    protected static final Logger logger = LoggerFactory.getLogger(PresentationAspect.class);

    public PresentationAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        if (value instanceof String) {
            put(ASConstants.FIELD_LABEL, value);
        } else {
            super.loadFromValue(value);
        }
    }

    public PresentationAspect(String label) {
        this((label != null) ? label.replaceAll("  "," ").trim() : "" , null, null, null, null, null);
    }

    public PresentationAspect(String label, String thumbnail, String icon, String description, String detailsUrl, BaseStreamElement root) {
        setRoot(root);

        put(ASConstants.FIELD_LABEL, label);
        if (thumbnail != null && !thumbnail.isEmpty()) put(ASConstants.FIELD_THUMBNAIL, thumbnail);
        if (icon != null && !icon.isEmpty()) put(ASConstants.FIELD_ICON, icon);
        if (description != null && !description.isEmpty()) put(ASConstants.FIELD_DESCRIPTION, description);
        if (detailsUrl != null && !detailsUrl.isEmpty()) put(ASConstants.FIELD_DETAILS_URL, detailsUrl);

    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public String getLabel() {
        return (String) get(ASConstants.FIELD_LABEL);
    }

    public void setLabel(String label) {
        if (label != null && !label.isEmpty()) put(ASConstants.FIELD_LABEL, label);
        else remove(ASConstants.FIELD_LABEL);
    }

    public PresentationAspect addLabel(String label) {
        setLabel(label);
        return this;
    }

    public String getDescription() {
        return (String) get(ASConstants.FIELD_DESCRIPTION);
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) put(ASConstants.FIELD_DESCRIPTION, description);
        else remove(ASConstants.FIELD_DESCRIPTION);
    }

    public PresentationAspect addDescription(String description) {
        setDescription(description);
        return this;
    }

    public String getThumbnail() {
        return (String) get(ASConstants.FIELD_THUMBNAIL);
    }

    public void setThumbnail(String thumbnail) {
        if (thumbnail != null && !thumbnail.isEmpty()) put(ASConstants.FIELD_THUMBNAIL, thumbnail);
        else remove(ASConstants.FIELD_THUMBNAIL);
    }

    public PresentationAspect addThumbnail(String thumbnail) {
        setThumbnail(thumbnail);
        return this;
    }

    public String getDetailsUrl() {
        return (String) get(ASConstants.FIELD_DETAILS_URL);
    }

    public void setDetailsUrl(String detailsUrl) {
        if (detailsUrl != null && !detailsUrl.isEmpty()) put(ASConstants.FIELD_DETAILS_URL, detailsUrl);
        else remove(ASConstants.FIELD_DETAILS_URL);
    }

    public PresentationAspect addDetailsUrl(String detailsUrl) {
        setDetailsUrl(detailsUrl);
        return this;
    }

    public String getIcon() {
        return (String) get(ASConstants.FIELD_ICON);
    }

    public void setIcon(String icon) {
        if (icon != null && !icon.isEmpty()) put(ASConstants.FIELD_ICON, icon);
        else remove(ASConstants.FIELD_ICON);
    }

    public PresentationAspect addIcon(String icon) {
        setIcon(icon);
        return this;
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_LABEL:
                //Supports both Strings and Maps
                if (value == null || value.toString().trim().isEmpty()) return null;
                break;
            case ASConstants.FIELD_DETAILS_URL:
            case ASConstants.FIELD_THUMBNAIL:
            case ASConstants.FIELD_DESCRIPTION:
            case ASConstants.FIELD_ICON:
                value = validator().processString(theKey, value, false);
                break;
            default:
                if (!theKey.startsWith("_")) {
                    logger.warn("Wrong property: " + theKey + " for Presentation aspect.");
                    this.addProblem(new IgnoredPropertyError("The '" + theKey + "' property is not supported for the Presentation Aspect"));
                }
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    /**
     * Creates a new Presentation Aspect instance
     * Utility function for cleaner chaining
     * @return a new Presentation Aspect
     */
    public static PresentationAspect presentation() {
        return new PresentationAspect();
    }
}
