package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.ASEntity;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.entities.EntityChangeMap;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.function.Consumer;

public class AddressAspect extends AbstractMapAspect implements LinkedElement, EnrichableElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_ADDRESS, AddressAspect::new, AspectType.MergeStrategy.REPLACE) {

    };

    protected static final Logger logger = LoggerFactory.getLogger(AddressAspect.class);

    //todo - Remove this silly/custom cleanup stuff
    final static Map<String, String> KNOWN_WRONG_COUNTRY_CODES = new HashMap<String, String>() {{
        put("DEN", "DK");
        put("SWE", "SE");
        put("GER", "DE");
    }}; //todo - replace with real lookup

    public AddressAspect() {
    }

    //todo - Remove this silly/custom cleanup stuff
    final static Map<String, Locale> KNOWN_COUNTRY_NAMES = new HashMap<String, Locale>() {{
        put("UNITED STATES [MILITARY]", new Locale("US"));
        put("USA", new Locale("US"));
        put("RUSSIAN FEDERATION", new Locale("RU"));
        put("VIET NAM", new Locale("VN"));
        put("KOREA, REPUBLIC OF", new Locale("KR"));
        put("COCOS (KEELING) ISLANDS", new Locale("CC"));
        put("SERBIA AND MONTENEGRO", new Locale("CS"));
        put("FALKLAND ISLANDS (MALVINAS)", new Locale("FK"));
        put("SCOTLAND", new Locale("GB"));
        put("RIUNION", new Locale("RE"));
    }}; //todo - replace with real lookup


    static final List<Locale> localesList = new LinkedList<>();

    static {
        for (String countryCode : Locale.getISOCountries()) {
            localesList.add(new Locale("", countryCode));
        }
    }

    public AddressAspect(String address, String address2, String city, String postcode, String country, String countryCode) {
        if (address != null && !address.isEmpty()) put(ASConstants.FIELD_ADDRESS,address);
        if (address2 != null && !address2.isEmpty()) put(ASConstants.FIELD_ADDRESS_2, address2);
        if (city != null && !city.isEmpty()) put(ASConstants.FIELD_CITY,city);
        if (postcode != null && !postcode.isEmpty()) put(ASConstants.FIELD_ZIP_CODE,postcode);
        if (country != null && !country.isEmpty()) put(ASConstants.FIELD_COUNTRY,country);
        if (countryCode != null && !countryCode.isEmpty()) put(ASConstants.FIELD_COUNTRY_CODE,countryCode);
    }

    @Override
    public void onEachEntityReference(Consumer<EntityReference> action) {
        throw new NotImplementedException();
    }

    /************
     * Utility functions
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public String getAddress() {
        return (String) get(ASConstants.FIELD_ADDRESS);
    }

    public AddressAspect addAddress(String address) {
        if (address != null && !address.isEmpty()) put(ASConstants.FIELD_ADDRESS, address);
        else remove(ASConstants.FIELD_ADDRESS);
        return this;
    }

    public String getAddress2() {
        return (String) get(ASConstants.FIELD_ADDRESS_2);
    }

    public AddressAspect addAddress2(String address) {
        if (address != null && !address.isEmpty()) put(ASConstants.FIELD_ADDRESS_2, address);
        else remove(ASConstants.FIELD_ADDRESS_2);
        return this;
    }

    public String getZipCode() {
        return (String) get(ASConstants.FIELD_ZIP_CODE);
    } //todo - rename these to match postcode data

    public String getPostcode() {
        return getZipCode();
    } //todo - rename these to match postcode data

    public AddressAspect addPostCode(String postalCode) {
        if (postalCode != null && !postalCode.isEmpty()) put(ASConstants.FIELD_ZIP_CODE, postalCode);
        else remove(ASConstants.FIELD_ZIP_CODE);
        return this;
    }

    public String getNormalizedPostcode() {
        if (getZipCode() != null) {
            return getZipCode().toUpperCase().replaceAll("[ |\\-|/]", "");
        }
        return null;
    }

    public String getCity() {
        return (String) get(ASConstants.FIELD_CITY);
    }

    public AddressAspect addCity(String city) {
        if (city != null && !city.isEmpty()) put(ASConstants.FIELD_CITY, city);
        else remove(ASConstants.FIELD_CITY);
        return this;
    }

    public String getCountry() {
        if (containsKey("country")) return (String) get("country");
        else if (this.root instanceof BusinessEntity && ((BusinessEntity) this.root).getEntityReference() != null) {
            EntityReference entityRefrence = ((BusinessEntity) this.root).getEntityReference();
            if (entityRefrence.getEntityId().startsWith("4-")) return "Sweden";
            else if (entityRefrence.getEntityId().startsWith("1-")) return "Denmark";
            else if (entityRefrence.getEntityId().startsWith("3-")) return "Norway";
            else return null;
        } else {
            //logger.error("Country is missing: " + this);
        }
        return null;
    }

    public AddressAspect addCountry(String country) {
        if (country != null && !country.isEmpty()) put(ASConstants.FIELD_COUNTRY, country);
        else remove(ASConstants.FIELD_COUNTRY);
        return this;
    }

    public AddressAspect addFuzzyCountry(String countryOrCountryCode) {

        if (countryOrCountryCode == null || countryOrCountryCode.isEmpty()) return this;

        Locale isLocale = KNOWN_COUNTRY_NAMES.get(countryOrCountryCode);

        if (isLocale == null) {
            for (Locale aLocale : localesList) {
                if (aLocale.getCountry().equals(countryOrCountryCode) || aLocale.getDisplayName().equalsIgnoreCase(countryOrCountryCode)) {
                    isLocale = aLocale;
                    KNOWN_COUNTRY_NAMES.put(countryOrCountryCode,aLocale);
                    break;
                }
            }
        }

        if (isLocale != null) {
            put(ASConstants.FIELD_COUNTRY, isLocale.getDisplayName());
            put(ASConstants.FIELD_COUNTRY_CODE, isLocale.getCountry());
        } else {
            logger.warn("Not Resolved: " + countryOrCountryCode);
        }
        return this;
    }

    public String getState() {
        return (String) get(ASConstants.FIELD_STATE);
    }

    public void setState(String state) {
        if (state != null && !state.isEmpty()) put(ASConstants.FIELD_STATE, state);
        else remove(ASConstants.FIELD_STATE);
    }

    public AddressAspect addState(String state) {
        setState(state);
        return this;
    }

    /**
     * Adds country code and state code as HASC code
     * This only happens if the country code exists and the hasc code is incomplete
     * @param stateCode
     * @return
     */
    public AddressAspect addStateCode(String stateCode) {
        if (getCountryCode() !=null && stateCode != null && stateCode.trim().length() == 2 && getCountryCode().length()==2) {
            if (getHascCode() == null || getHascCode().length() < 5) put(ASConstants.FIELD_HASC_CODE, (getCountryCode() + "." + stateCode).toUpperCase() );
        }
        return this;
    }

    public String getRegion() {
        return (String) get(ASConstants.FIELD_REGION);
    }

    public void setRegion(String region) {
        if (region != null && !region.isEmpty()) put(ASConstants.FIELD_REGION, region);
        else remove(ASConstants.FIELD_REGION);
    }

    public AddressAspect addRegion(String region) {
        setRegion (region);
        return this;
    }

    @Deprecated
    public String getLevel1() {
        return (String) get(ASConstants.FIELD_STATE);
    } //todo - rename these

    public String getHascCode() {
        return (String) get(ASConstants.FIELD_HASC_CODE);
    }

    public String getFipsCode() {
        return (String) get(ASConstants.FIELD_FIPS_CODE);
    }

    public String getNgaCode() {
        return (String) get(ASConstants.FIELD_NGA_CODE);
    }

    @Deprecated
    public String getLevel2() {
        return (String) get(ASConstants.FIELD_REGION);
    } //todo - rename these

    private GeoLocationAspect getLatLong() {
        if (get(ASConstants.FIELD_LATLONG) instanceof GeoLocationAspect) {
            return (GeoLocationAspect) get(ASConstants.FIELD_LATLONG);
        } else if (get(ASConstants.FIELD_LATLONG) instanceof String) {
            return new GeoLocationAspect(get(ASConstants.FIELD_LATLONG), null);

        }
        return null;
    }

    public GeoLocationAspect getGeoLocation() {

        GeoLocationAspect geoLocationAspect = null;

        if (get(ASConstants.FIELD_LATLONG) instanceof GeoLocationAspect) {
            geoLocationAspect = (GeoLocationAspect) get(ASConstants.FIELD_LATLONG);
        } else if (get(ASConstants.FIELD_LATLONG) instanceof String) {
            geoLocationAspect = new GeoLocationAspect((String) get(ASConstants.FIELD_LATLONG), ASConstants.FIELD_ADDRESS, getHascCode(), this.root);
        } else if (get(ASConstants.FIELD_ZIP_LATLONG) instanceof String) {
            geoLocationAspect = new GeoLocationAspect((String) get(ASConstants.FIELD_ZIP_LATLONG), ASConstants.FIELD_ADDRESS, getHascCode(), this.root);
        } else if (get(ASConstants.FIELD_LATLONG) == null) {
            geoLocationAspect = new GeoLocationAspect(null, this.root);
            logger.trace("No location found for address: " + this);
        } else {
            logger.error("WTF");
        }

        if (geoLocationAspect != null) {
            if (getCountryCode() != null) {
                geoLocationAspect.setCountryCode(getCountryCode());
                geoLocationAspect.setHascCode(getCountryCode());
            }
            if (getHascCode() != null) geoLocationAspect.setHascCode(getHascCode());
            if (geoLocationAspect.getFipsCode().isEmpty() && getFipsCode() != null) geoLocationAspect.setFipsCode(getFipsCode());
            if (geoLocationAspect.getNgaCode().isEmpty() && getNgaCode() != null) geoLocationAspect.setNgaCode(getNgaCode());
        }

        return geoLocationAspect;
    }

    public String getCountryCode() {
        String countryCode = (String) get(ASConstants.FIELD_COUNTRY_CODE);
        if (countryCode != null) countryCode = KNOWN_WRONG_COUNTRY_CODES.getOrDefault(countryCode.toUpperCase(), countryCode);
        return countryCode;
    }

    public AddressAspect addCountryCode(String countryCode) {
        //todo - validate country code (against ISO 2 and 3)
        if (countryCode != null && !countryCode.isEmpty()) put(ASConstants.FIELD_COUNTRY_CODE, countryCode.toUpperCase());
        else remove(ASConstants.FIELD_COUNTRY_CODE);
        return this;
    }


    public String getZipcodeIfZipCodeOnly() {
        if (this.getZipCode() != null && this.getCountryCode() != null && this.getCountryCode().length() == 2) {
            return this.getCountryCode() + this.getZipCode();
        }
        return null;
    }

    public String getZipCodeId() {
        String countryCode = this.getCountryCode();
        if (countryCode == null && this.getCountry() != null && this.getCountry().length() == 2) countryCode = this.getCountry(); //EinarN workaround
        String zipCode = this.getZipCode();
        //if (zipCode != null && zipCode.startsWith("0")) zipCode = zipCode.substring(1);
        return (countryCode != null && zipCode != null) ? (countryCode + zipCode).replaceAll(" ", "") : null;
    }

    public boolean isVerified() {
        return containsKey(ASConstants.FIELD_VERIFIED) && (Boolean) get(ASConstants.FIELD_VERIFIED);
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

        if (value == null || value.toString().trim().isEmpty() || value.toString().equalsIgnoreCase("null")) return null;
        switch (theKey) {
            case ASConstants.FIELD_POSTAL_CODE:
                theKey = ASConstants.FIELD_ZIP_CODE;
            case ASConstants.FIELD_ZIP_CODE:
            case ASConstants.FIELD_CITY:
            case ASConstants.FIELD_COUNTRY_CODE:
            case ASConstants.FIELD_COUNTRY:
                if (value.toString().equalsIgnoreCase("unknown")) return null;
                value = validator().processString(theKey, value, true); //value is required if property is provided
                break;
            case ASConstants.FIELD_ADDRESS:
                if (value.toString().equalsIgnoreCase("unknown")) return null;
                value = validator().processString(theKey, value, true); //value is required if property is provided
                if (value.toString().contains(",")) {
                    String[] parts = value.toString().split(",");
                    if (parts.length == 0) return null;
                    value = parts[0];
                    if (parts.length > 1) put(ASConstants.FIELD_ADDRESS_2, parts[1]);
                }
                break;
            case ASConstants.FIELD_ADDRESS_2:
            case ASConstants.FIELD_STATE:
            case ASConstants.FIELD_STATE_CODE:
            case ASConstants.FIELD_REGION:
            case ASConstants.FIELD_ZIP_LATLONG:
            case ASConstants.FIELD_HASC:
            case ASConstants.FIELD_NGA_CODE:
            case ASConstants.FIELD_FIPS_CODE:
            case ASConstants.FIELD_LATLONG:
            case ASConstants.FIELD_SUB_REGION:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_VERIFIED:
                value = validator().processBoolean(theKey, value, false);
                break;
            case ASConstants.FIELD_ALTERED:
                value = validator().processBoolean(theKey, value, false);
                break;
            default:
                if (!key.toString().startsWith("_"))
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Address Aspect"));
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    @Override
    protected void handleChanges(Map<String, Object> oldValues, Map<String, Object> newValues) {
        registerChanges(oldValues, newValues, EntityChangeMap.ACTION.PROCESS, EntityChangeMap.ACTION.IGNORE);
    }

    /**
     * Creates a new Address Aspect instance
     * Utility function for cleaner chaining
     * @return a new Address Aspect
     */
    public static AddressAspect address() {
        return new AddressAspect();
    }

}
