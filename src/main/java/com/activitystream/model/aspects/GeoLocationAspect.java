package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

public class GeoLocationAspect extends AbstractMapAspect implements EnrichableElement, AnalyticsElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_GEO_LOCATION, GeoLocationAspect::new, AspectType.MergeStrategy.REPLACE) {
        /*
        @Override
        public boolean isActive(SavableElement element, BaseStreamElement root) {
            Object geoObj = element.getProperty(ASConstants.ASPECTS_GEO_LOCATION);
            return geoObj instanceof ODocument && !((ODocument) geoObj).isEmpty();
        }
        */
    };

    final Double missingDouble = new Double(0.0);

    public GeoLocationAspect() {
    }

    public GeoLocationAspect(Map m, BaseStreamElement root) {
        super(m, root);
    }

    public GeoLocationAspect(Object m, BaseStreamElement root) {
        setRoot(root);
        loadFromValue(m);
    }

    @Override
    public void loadFromValue(Object m) {
        if (m instanceof String) {
            put("latlong", m);
        } else {
            super.loadFromValue(m);
        }
    }

    public GeoLocationAspect(String latlong, String type, String hasc, BaseStreamElement root) {
        setRoot(root);
        if (latlong != null) put(ASConstants.FIELD_LATLONG, latlong);
        if (type != null) put(ASConstants.FIELD_TYPE, type);
        if (hasc != null) put(ASConstants.FIELD_HASC_CODE, hasc);
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public Double getLatitude() {
        Object value = getOrDefault(ASConstants.FIELD_LATITUDE, missingDouble);
        if (value instanceof Float) return ((Float) value).doubleValue();
        return (Double) value;
    }

    public GeoLocationAspect addLatitude(Float latitude) {
        if (latitude != null) put(ASConstants.FIELD_LATITUDE, latitude);
        else remove(ASConstants.FIELD_LATITUDE);
        return this;
    }

    public Double getLongitude() {
        Object value = getOrDefault(ASConstants.FIELD_LONGITUDE, missingDouble);
        if (value instanceof Float) return ((Float) value).doubleValue();
        return (Double) value;
    }

    public GeoLocationAspect addLongitude(Float longitude) {
        if (longitude != null) put(ASConstants.FIELD_LONGITUDE, longitude);
        else remove(ASConstants.FIELD_LONGITUDE);
        return this;
    }

    public GeoLocationAspect addLatLong(String latLong) {
        if (latLong != null && !latLong.isEmpty() && latLong.contains(",")) {
            String laLo[] = latLong.split(",");
            put(ASConstants.FIELD_LATITUDE, validator().processDouble("latlong", laLo[0], false, null, null));
            put(ASConstants.FIELD_LONGITUDE, validator().processDouble("latlong", laLo[1], false, null, null));
        } else {
            remove(ASConstants.FIELD_LATITUDE);
            remove(ASConstants.FIELD_LONGITUDE);
        }
        return this;
    }

    public String getHascCode() {
        return (String) getOrDefault(ASConstants.FIELD_HASC_CODE, "");
    }

    public String getFipsCode() {
        return (String) getOrDefault(ASConstants.FIELD_FIPS_CODE, "");
    }

    public String getNgaCode() {
        return (String) getOrDefault(ASConstants.FIELD_NGA_CODE, "");
    }

    public void setHascCode(String hasc) {
        put(ASConstants.FIELD_HASC_CODE, hasc);
    }

    public String getPostalCode() {
        return (String) getOrDefault(ASConstants.FIELD_POSTAL_CODE, "");
    }

    public String getCountryCode() {
        return (String) getOrDefault(ASConstants.FIELD_COUNTRY_CODE, "");
    }

    public void setCountryCode(String countryCode) {
        if (countryCode != null && !countryCode.isEmpty()) put(ASConstants.FIELD_COUNTRY_CODE, countryCode);
        else remove(ASConstants.FIELD_COUNTRY_CODE);
    }

    public void setFipsCode(String fipsCode) {
        put(ASConstants.FIELD_FIPS_CODE, fipsCode);
    }

    public void setNgaCode(String ngaCode) {
        put(ASConstants.FIELD_NGA_CODE, ngaCode);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    @Override
    public void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth) {
        throw new NotImplementedException();
    }

    @Override
    public void addTimeSeriesDimensions(TimeSeriesEntry entry) {

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
            case ASConstants.FIELD_LATLONG:
                value = validator().processString(theKey, value, false);
                String latlong[] = ((String) value).split(",");
                put(ASConstants.FIELD_LATITUDE, validator().processDouble(theKey, latlong[0], false, null, null));
                put(ASConstants.FIELD_LONGITUDE, validator().processDouble(theKey, latlong[1], false, null, null));
                return null;
            case ASConstants.FIELD_LATITUDE:
            case ASConstants.FIELD_LONGITUDE:
                value = validator().processDouble(theKey, value, false, null, null);
                break;
            case ASConstants.FIELD_HASC_CODE:
            case ASConstants.FIELD_FIPS_CODE:
            case ASConstants.FIELD_NGA_CODE:
            case ASConstants.FIELD_TRACK_FOR:
            case ASConstants.FIELD_COUNTRY_CODE:
            case ASConstants.FIELD_POSTAL_CODE:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_TYPE:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_VERIFIED:
                value = validator().processBoolean(theKey, value, false);
                break;
            default:
                if (!theKey.startsWith("_")) {
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the GeoLocation Aspect"));
                }
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    /**
     * Creates a new GeoLocation Aspect instance
     * Utility function for cleaner chaining
     * @return a new GeoLocation Aspect
     */
    public static GeoLocationAspect geoLocation() {
        return new GeoLocationAspect();
    }

}
