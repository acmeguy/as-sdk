package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.interfaces.BaseStreamElement;
import com.activitystream.core.model.internal.InternalEntity;
import com.activitystream.core.model.utils.StreamIdUtils;
import com.activitystream.core.model.validation.Validator;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class TrafficSource extends InternalEntity {

    private static final List<String> NATIVE_PROPERTIES = Arrays.asList(ASConstants.FIELD_TYPE, ASConstants.FIELD_CAMPAIGN, ASConstants.FIELD_SOURCE,
            ASConstants.FIELD_MEDIUM, ASConstants.FIELD_TERM, ASConstants.FIELD_CONTENT, ASConstants.FIELD_REFERRER);
    private static final Set<String> MANAGED_PROPERTIES =
            ImmutableSet.of(ASConstants.FIELD_OCCURRED_AT, ASConstants.FIELD_ASPECTS, ASConstants.FIELD_INVOLVES,
                    ASConstants.FIELD_RELATIONS, ASConstants.FIELD_ENTITY_REF, ASConstants.FIELD_TYPE_REF, ASConstants.FIELD_ACL, ASConstants.FIELD_PARTITION);

    private static final Logger logger = LoggerFactory.getLogger(TrafficSource.class);

    private String calculatedFootprint = null;

    public TrafficSource() {
    }

    public TrafficSource(Map value, BaseStreamElement root) {
        super(value, root);
    }

    /**
     * Traffic source for events and transaction lines
     * @param type The type of source "campaign", "social", "organic" etc...
     * @param campaign The campaign responsible for the the traffic (if present)
     * @param source The source it self (exact site the traffic came from)
     * @param medium The medium used mail, web etc.
     */
    public TrafficSource(String type, String campaign, String source, String medium) {
        put(ASConstants.FIELD_TYPE, type);
        if (campaign != null && !campaign.isEmpty()) put(ASConstants.FIELD_CAMPAIGN, campaign);
        if (source != null && !source.isEmpty()) put(ASConstants.FIELD_SOURCE, source);
        if (medium != null && !medium.isEmpty()) put(ASConstants.FIELD_MEDIUM, medium);
    }

    @Override
    public List<String> getNativeProperties() {
        return NATIVE_PROPERTIES;
    }

    @Override
    public Set<String> getManagedProperties() {
        return MANAGED_PROPERTIES;
    }

    @Override
    public EntityReference getEntityReference() {
        String trafficSourceFootprint = getTrafficSourceFootprint();
        if (!trafficSourceFootprint.isEmpty()) {
            return new EntityReference(ASConstants.AS_TRAFFIC_SOURCE, trafficSourceFootprint);
        }
        return null;
    }

    private String getTrafficSourceFootprint() {

        if (calculatedFootprint != null) return calculatedFootprint;

        StringBuilder sb = new StringBuilder();
        for (String field : NATIVE_PROPERTIES) {
            sb.append("--").append(get(field));
        }

        calculatedFootprint = StreamIdUtils.calculateStreamId(sb.toString()).toString();
        return calculatedFootprint;
    }

    /************
     * Utility functions
     ************/

    public String getType() {
        return (String) get(ASConstants.FIELD_TYPE);
    }

    /**
     * Sets the traffic source type
     * @param type The type of source "campaign", "social", "organic" etc...
     */
    public void setType(String type) {
        if (type != null && !type.isEmpty()) put(ASConstants.FIELD_TYPE, type);
        else remove(ASConstants.FIELD_TYPE);
    }

    /**
     * Sets the traffic source type
     * @param type The type of source "campaign", "social", "organic" etc...
     */
    public TrafficSource addType(String type) {
        setType(type);
        return this;
    }

    public String getCampaign() {
        return (String) get(ASConstants.FIELD_CAMPAIGN);
    }

    /**
     * Sets the campaign for the traffic source (If campaign)
     * @param campaign The campaign responsible for the the traffic (if present)
     */
    public void setCampaign(String campaign) {
        if (campaign != null && !campaign.isEmpty()) put(ASConstants.FIELD_CAMPAIGN, campaign);
        else remove(ASConstants.FIELD_CAMPAIGN);
    }

    /**
     * Sets the campaign for the traffic source (If campaign)
     * @param campaign The campaign responsible for the the traffic (if present)
     */
    public TrafficSource addCampaign(String campaign) {
        setCampaign(campaign);
        return this;
    }

    public String getCampaignKey() {
        return Validator.normalizeRef(getCampaign());
    }

    public EntityReference getCampaignRef() {
        if (!containsKey(ASConstants.FIELD_CAMPAIGN)) return null;
        return new EntityReference(ASConstants.AS_UTM_CAMPAIGN, getCampaignKey(), (String) get(ASConstants.FIELD_CAMPAIGN));
    }

    public String getSource() {
        return (String) get(ASConstants.FIELD_SOURCE);
    }

    /**
     * Sets the traffic source site
     * @param source The source it self (exact site the traffic came from)
     */
    public void setSource(String source) {
        if (source != null && !source.isEmpty()) put(ASConstants.FIELD_SOURCE, source);
        else remove(ASConstants.FIELD_SOURCE);
    }

    /**
     * Sets the traffic source site
     * @param source The source it self (exact site the traffic came from)
     */
    public TrafficSource addSource(String source) {
        setSource(source);
        return this;
    }

    public String getSourceKey() {
        return Validator.normalizeRef(getSource());
    }

    public EntityReference getSourceRef() {
        if (!containsKey(ASConstants.FIELD_SOURCE)) return null;
        return new EntityReference(ASConstants.AS_UTM_SOURCE, getSourceKey(), (String) get(ASConstants.FIELD_SOURCE));
    }

    public String getMediumKey() {
        return Validator.normalizeRef((String) get(ASConstants.FIELD_MEDIUM));
    }

    public String getMedium() {
        return (String) get(ASConstants.FIELD_MEDIUM);
    }

    /**
     * Sets the medium type of the source
     * @param medium The medium used mail, web etc.
     */
    public void setMedium(String medium) {
        if (medium != null && !medium.isEmpty()) put(ASConstants.FIELD_MEDIUM, medium);
        else remove(ASConstants.FIELD_MEDIUM);
    }

    /**
     * Sets the medium type of the source
     * @param medium The medium used mail, web etc.
     */
    public TrafficSource addMedium(String medium) {
        setMedium(medium);
        return this;
    }

    public String getTypeKey() {
        return Validator.normalizeRef(getType());
    }

    public EntityReference getMediumRef() {
        if (!containsKey(ASConstants.FIELD_MEDIUM)) return null;
        return new EntityReference(ASConstants.AS_UTM_MEDIUM, getMediumKey(), (String) get(ASConstants.FIELD_MEDIUM));
    }

    public String getReferrer() {
        return (String) getOrDefault(ASConstants.FIELD_REFERRER, "unknown");
    }

    /**
     * Sets the traffic referrer site
     * @param referrer The site that referred traffic to the location that is reporting this (2nd degree)
     */
    public void setReferrer(String referrer) {
        if (referrer != null && !referrer.isEmpty()) put(ASConstants.FIELD_REFERRER, referrer);
        else remove(ASConstants.FIELD_REFERRER);
    }

    public TrafficSource addReferrer(String referrer) {
        setReferrer(referrer);
        return this;
    }

    public String getReferrerKey() {
        return Validator.normalizeRef(getReferrer());
    }

    public EntityReference getReferrerRef() {
        if (!containsKey(ASConstants.FIELD_REFERRER)) return null;
        return new EntityReference(ASConstants.AS_WEB_DOMAIN, getReferrerKey(), (String) get(ASConstants.FIELD_REFERRER));
    }

    public String getTerm() {
        return (String) get(ASConstants.FIELD_TERM);
    }

    public void setTerm(String term) {
        if (term != null && !term.isEmpty()) put(ASConstants.FIELD_TERM, term);
        else remove(ASConstants.FIELD_TERM);
    }

    public TrafficSource addTerm(String term) {
        setTerm(term);
        return this;
    }

    public String getTermKey() {
        return Validator.normalizeRef(getTerm());
    }


    @Override
    public void onEachEntityReference(Consumer<EntityReference> action) {
        EntityReference ref = getEntityReference();
        if (ref != null)
            action.accept(ref);
    }

    @Override
    public String getElementType() {
        return null;
    }

    @Override
    public void prepareForFederation() {

    }

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        //todo - implement proper data processing

        switch (theKey) {
            case ASConstants.FIELD_TYPE:
                value = validator().processUtlString(theKey, value, false);
                break;
            case ASConstants.FIELD_REFERRER:
                value = validator().processUtlString(theKey, value, false);
                break;
            case ASConstants.FIELD_CAMPAIGN:
                value = validator().processUtlString(theKey, value, false);
                break;
            case ASConstants.FIELD_SOURCE:
                value = validator().processUtlString(theKey, value, true);
                break;
            case ASConstants.FIELD_MEDIUM:
                value = validator().processUtlString(theKey, value, false);
                break;
            case ASConstants.FIELD_CONTENT:
                if (value instanceof Map) {
                    value = validator().processUrlBasedMap(theKey, value, false);
                } else {
                    return null; //Ignore wrong content values
                }
                break;
            case ASConstants.FIELD_TERM:
                value = validator().processUtlString(theKey, value, false);
                break;
            default:
                logger.warn(theKey + " was not found for Campaign Aspect");
                //this.addException(new UnsupportedAspectError("The " +  theKey + " property is not supported for the Client IP Aspect"));
        }
        return super.put(theKey, value);
    }

    public static TrafficSource trafficSource() {
        return new TrafficSource();
    }

}
