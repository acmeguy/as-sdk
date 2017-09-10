package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import com.activitystream.model.validation.MessageValidator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class ClientIpAspect extends AbstractMapAspect implements CompactableElement, LinkedElement, EnrichableElement, AnalyticsElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_CLIENT_IP, ClientIpAspect::new) {
    };

    private static final Logger logger = LoggerFactory.getLogger(ClientIpAspect.class);

    public ClientIpAspect() {

    }

    public ClientIpAspect(Object m, BaseStreamElement root) {
        setRoot(root);
        loadFromValue(m);
    }

    @Override
    public void loadFromValue(Object m) {
        if (m instanceof String) {
            put("ip", m);
        } else {
            super.loadFromValue(m);
        }
    }

    /************
     * Utility functions
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    //todo - create enrichment getters

    @Override
    public void onEachEntityRelation(Consumer<Relation> action) {
        EntityReference ipRef = getIpRef();
        if (ipRef != null)
            action.accept(new Relation(ASConstants.REL_CONNECTS_FROM, ipRef, getRoot()));
    }

    @Override
    public void onEachRelationType(StreamItemRelationTypeConsumer action) {
        if (getRoot() instanceof BusinessEntity)
            action.accept(ASConstants.REL_CONNECTS_FROM, ((BusinessEntity) getRoot()).getElementType(), ASConstants.AS_IP_ADDRESS);
    }

    @Override
    public void onEachEntityReference(Consumer<EntityReference> action) {
        EntityReference ipReference = getIpRef();
        if (ipReference != null) {
            if (getLongitude() != null && getLatitude() != null) {
                ipReference.setDefaults(new HashMap<String, Object>() {{
                    put("latlong", getLatitude() + "," + getLongitude());
                }});
            }
            action.accept(ipReference);
        } else {
            logger.error("ahem!");
        }

        //Create references
        if (get("_ipEntity") != null) {

            if (get("_isp") != null) action.accept(getIspRef());

            if (getCountryCode() != null && getPostalCode() != null) {
                action.accept(getPostalCodeRef());
                action.accept(getCountryCodeRef());
            }

            if (getGeoLocationRef() != null) action.accept(getGeoLocationRef());
        }
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    @Override
    public void compact() {

    }

    public EntityReference getIspRef() {
        return (this.get("_isp") != null) ? new EntityReference(ASConstants.AS_ISP, MessageValidator.normalizeRef((String) get("_isp"))) : null;
    }

    public EntityReference getIpRef() {
        return (this.get("ip") != null) ? new EntityReference(ASConstants.AS_IP_ADDRESS, (String) this.get("ip")) : null;
    }

    public EntityReference getCountryCodeRef() {
        return (getCountryCode() != null) ? new EntityReference(ASConstants.AS_COUNTRY, MessageValidator.normalizeUppercaseRef(getCountryCode())) : null;
    }

    public EntityReference getPostalCodeRef() {
        if (getCountryCode() == null || getPostalCode() == null) {
            return null;
        } else {
            return new EntityReference(ASConstants.AS_POSTAL_CODE,
                    MessageValidator.normalizeUppercaseRef(getCountryCode() + "--" + getPostalCode().replaceAll(" ", "").toUpperCase()));
        }
    }

    public EntityReference getGeoLocationRef() {
        if (getLatitude() == null || getLongitude() == null) {
            return null;
        } else {
            return new EntityReference(ASConstants.AS_GEO_LOCATION, getLatitude() + "," + getLongitude());
        }
    }

    public String getIp() {
        return (String) get("ip");
    }

    public String getEntityReference() {
        return ASConstants.AS_IP_ADDRESS + "/" + getIp();
    }

    public String getNormalizedPostcode() {
        String postcode = getPostalCode();
        if (postcode == null) postcode = "";
        return postcode.toUpperCase().replaceAll(" ", "");
    }

    public String getPostalCode() {
        return (String) get("_postal_code");
    }

    public String getCountryCode() {
        return (String) get("_country_code");
    }

    public Double getLongitude() {
        return (Double) get("_longitude");
    }

    public Double getLatitude() {
        return (Double) get("_latitude");
    }

    /************ Enrichment & Analytics ************/

    @Override
    public void addTimeSeriesDimensions(TimeSeriesEntry entry) {

        final String[] fields =
                new String[]{"country_code", "isp", "continent", "city", "postal_code", "region", "longitude", "latitude", "dma_code", "timezone"};
        Map<String, Object> ip = new LinkedHashMap<>();
        ip.put("ip", get("ip"));
        for (String field : fields) {
            if (containsKey("_" + field) && get("_" + field) != null) ip.put(field, get("_" + field));

            if (field.equals("timezone") && entry.containsKey(ASConstants.FIELD_OCCURRED_AT)) {
                DateTime time = entry.getOccurredAt();
                String timeZoneName = (String) get("_" + field);
                if (timeZoneName != null && !timeZoneName.isEmpty()) {
                    TimeZone timeZone = TimeZone.getTimeZone(timeZoneName);
                    if (timeZone != null && !time.getZone().equals(timeZone)) {
                        entry.put(ASConstants.FIELD_OCCURRED_AT_LTOD, time.toDate().getTime() + timeZone.getRawOffset());
                    }
                }
            }
        }
        entry.put("client_ip", ip);
    }

    @Override
    public void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth) {
    }

    private CharSequence getCharSequence(String property, String fallbackProperty) {
        return (getOrDefault(property, getOrDefault(fallbackProperty, "")) != null) ?
                (CharSequence) getOrDefault(property, getOrDefault(fallbackProperty, "")) : "";
    }

    private Double getDouble(String property) {
        return (getOrDefault(property, 0d) != null) ? (Double) getOrDefault(property, 0d) : 0d;
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
            case "client_ip":
                theKey = "ip";
            case "ip":
                if (value == null || value.toString().isEmpty()) return null;
                //super.put("_ipEntity", new BusinessEntity(ASConstants.AS_IP_ADDRESS + "/" + value, this));
                break;
            case "bind_to":
            case "track_for":
                break;
            default:
                //allow enrichment fields (All prefixed with "_")
                if (!theKey.startsWith("_")) {
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Client IP Aspect"));
                    return null;
                }
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }
}
