package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.interfaces.*;
import com.activitystream.core.model.entities.EntityChangeMap;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.validation.IgnoredPropertyError;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * The Demography aspect is used to store demogrphic information for "people entities"
 */
public class DemographyAspect extends AbstractMapAspect implements EnrichableElement {

    public static final AspectType ASPECT_TYPE =
            new AspectType(ASConstants.ASPECTS_DEMOGRAPHY, DemographyAspect::new, AspectType.MergeStrategy.MERGE) {
        @Override
        public boolean shouldCreateForEnrichment(BaseStreamElement root) {
            if (root instanceof HasAspects) {
                AspectManager aspectManager = ((HasAspects) root).getAspectManager();
                return (aspectManager != null && aspectManager.getPresentation() != null && aspectManager.getPresentation().getLabel() != null);
            }
            return false;
        }

    };

    protected static final Logger logger = LoggerFactory.getLogger(DemographyAspect.class);

    /**
     * Creates a new Demography Aspect
     * Demography Aspect is used to categorize entities that represent people (Users, Customer etc.).
     */
    public DemographyAspect() {
    }

    /**
     * Creates a new Demography Aspect
     * Demography Aspect is used to categorize entities that represent people (Users, Customer etc.).
     * @param gender the gender of the person
     * @param birthDate the birthday of the person (full or partial)
     */
    public DemographyAspect(String gender, String birthDate) {
        this(gender, birthDate, null);
    }

    /**
     * Creates a new Demography Aspect
     * Demography Aspect is used to categorize entities that represent people (Users, Customer etc.).
     * @param gender the gender of the person
     * @param birthDate the birthday of the person (full or partial)
     * @param root parent entity
     */
    public DemographyAspect(String gender, String birthDate, BaseStreamElement root) {
        setRoot(root);
        setGender(gender, false);
        setBirthDate(birthDate);
    }


    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public void setBirthDate(String birthDate) {
        if (birthDate != null && !birthDate.isEmpty()) {
            try {
                DateTime bDate = DateTime.parse(birthDate);
                put(ASConstants.FIELD_BIRTH_DAY, bDate.getDayOfMonth());
                put(ASConstants.FIELD_BIRTH_YEAR, bDate.getYear());
                put(ASConstants.FIELD_BIRTH_MONTH, bDate.getMonthOfYear());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public DemographyAspect withBirthDate(String birthDate) {
        setBirthDate(birthDate);
        return this;
    }

    public DemographyAspect withBirthMonth(String birthYear, String birthMonth) {
        setBirthYear(Integer.parseInt(birthYear));
        setBirthYear(Integer.parseInt(birthMonth));
        return this;
    }

    public DemographyAspect withBirthMonth(Integer birthYear, Integer birthMonth) {
        setBirthYear(birthYear);
        setBirthMonth(birthMonth);
        return this;
    }

    public String getGender() {
        String gender = (String) get(ASConstants.FIELD_GENDER);
        if (gender != null) gender = gender.toLowerCase();
        return gender;
    }

    public DemographyAspect assumeGender(String gender) {
        setGender(gender, true);
        return this;
    }

    public DemographyAspect withGender(String gender) {
        setGender(gender, false);
        return this;
    }

    public void setGender(String gender, boolean guessed) {
        if (gender != null) {
            if (gender.isEmpty()) {
                remove(ASConstants.FIELD_GENDER);
                remove(ASConstants.FIELD_GENDER_GUESSED);
            } else if (gender.equalsIgnoreCase("Unknown")) {
                return;
            } else {
                put(ASConstants.FIELD_GENDER, gender);
                put(ASConstants.FIELD_GENDER_GUESSED, guessed);
            }
        }
    }

    public Boolean getGenderGuessed() {
        return containsKey(ASConstants.FIELD_GENDER_GUESSED) ? (Boolean) get(ASConstants.FIELD_GENDER_GUESSED) : true;
    }

    public void setGenderGuessed(Boolean genderGuessed) {
        put(ASConstants.FIELD_GENDER_GUESSED, genderGuessed);
    }

    public int getAgeOnDay() {
        return getAgeOnDay(new DateTime());
    }

    public int getAgeOnDay(DateTime when) {
        if (when == null || (getBirthYear() == null && getBirthDate() == null)) return 0;

        DateTime birthday = new DateTime();

        if (getBirthDate() != null) {
            birthday = getBirthDate();
        } else {
            try {
                birthday = birthday.withYear(getBirthYear());
                birthday = birthday.withMonthOfYear(getBirthMonth());
                birthday = birthday.withDayOfMonth(getBirthDay());
            } catch (Exception e) {
                logger.warn("Error setting date for " + this + " -> " + e);
            }
        }
        return Years.yearsBetween(birthday, when).getYears();

    }

    public String getEthnicity() {
        return (String) get(ASConstants.FIELD_ETHNICITY);
    }

    public void setEthnicity(String ethnicity) {
        if (ethnicity != null && !ethnicity.isEmpty()) put(ASConstants.FIELD_ETHNICITY, ethnicity);
        else remove(ASConstants.FIELD_ETHNICITY, ethnicity);
    }

    public DemographyAspect withEthnicity(String ethnicity) {
        setEthnicity(ethnicity);
        return this;
    }

    public String getMaritalStatus() {
        return (String) get(ASConstants.FIELD_MARITAL_STATUS);
    }

    public void setMaritalStatus(String maritalStatus) {
        if (maritalStatus != null && !maritalStatus.isEmpty()) put(ASConstants.FIELD_MARITAL_STATUS, maritalStatus);
        else remove(ASConstants.FIELD_MARITAL_STATUS, maritalStatus);
    }

    public DemographyAspect withMaritalStatus(String maritalStatus) {
        setMaritalStatus(maritalStatus);
        return this;
    }

    public String getEmployment() {
        return (String) get(ASConstants.FIELD_EMPLOYMENT);
    }

    public void setEmployment(String employment) {
        if (employment != null && !employment.isEmpty()) put(ASConstants.FIELD_EMPLOYMENT, employment);
        else remove(ASConstants.FIELD_EMPLOYMENT);
    }

    public DemographyAspect withEmployment(String employment) {
        setEmployment(employment);
        return this;
    }

    public String getIncome() {
        return (String) get(ASConstants.FIELD_INCOME);
    }

    public void setIncome(String incomeGroup) {
        if (incomeGroup != null && !incomeGroup.isEmpty()) put(ASConstants.FIELD_INCOME, incomeGroup);
        else remove(ASConstants.FIELD_INCOME);
    }

    public DemographyAspect withIncome(String income) {
        setIncome(income);
        return this;
    }

    public String getHousing() {
        return (String) get(ASConstants.FIELD_HOUSING);
    }

    public void setHousing(String housing) {
        if (housing != null && !housing.isEmpty()) put(ASConstants.FIELD_INCOME, housing);
        else remove(ASConstants.FIELD_INCOME);
    }

    public DemographyAspect withHousing(String housing) {
        setHousing(housing);
        return this;
    }


    public String getEducation() {
        return (String) get(ASConstants.FIELD_EDUCATION);
    }

    public void setEducation(String education) {
        if (education != null && !education.isEmpty()) put(ASConstants.FIELD_EDUCATION, education);
        else remove(ASConstants.FIELD_EDUCATION);
    }

    public DemographyAspect withEducation(String education) {
        setEducation(education);
        return this;
    }

    public String getMosaicGroup() {
        return (String) get(ASConstants.FIELD_MOSAIC_GROUP);
    }

    public void setMosaicGroup(String mosaicGroup) {
        if (mosaicGroup != null && ! mosaicGroup.isEmpty()) put(ASConstants.FIELD_MOSAIC_GROUP, mosaicGroup);
        else remove(ASConstants.FIELD_MOSAIC_GROUP);
    }

    public DemographyAspect withMosaicGroup(String mosaicGroup) {
        setMosaicGroup(mosaicGroup);
        return this;
    }


    public DateTime getBirthDate() {
        Object birthDate = get(ASConstants.FIELD_BIRTH_DATE);
        if (birthDate != null) {
            if (birthDate instanceof Date) return new DateTime(birthDate);
            else if (birthDate instanceof DateTime) return (DateTime) birthDate;
            else if (birthDate instanceof String) return DateTime.parse((String) birthDate);
        } else {
            if (getBirthDay() != null && getBirthMonth() != null && getBirthYear() != null) {
                return new DateTime(0).withDate(getBirthYear(), getBirthMonth(), getBirthDay());
            }
        }
        return null;
    }

    public Integer getBirthYear() {
        return (Integer) get(ASConstants.FIELD_BIRTH_YEAR);
    }

    public void setBirthYear(Integer birthYear) {
        if (birthYear != null) put(ASConstants.FIELD_BIRTH_YEAR, birthYear);
        else remove(ASConstants.FIELD_BIRTH_YEAR);
    }

    public DemographyAspect withBirthYear(Integer birthYear) {
        setBirthYear(birthYear);
        return this;
    }

    public Integer getBirthMonth() {
        return (Integer) get(ASConstants.FIELD_BIRTH_MONTH);
    }

    public void setBirthMonth(Integer birthMonth) {
        if (birthMonth != null && birthMonth > 0 && birthMonth < 13) put(ASConstants.FIELD_BIRTH_MONTH, birthMonth);
        else remove(ASConstants.FIELD_BIRTH_MONTH);
    }

    public DemographyAspect withBirthMonth(Integer birthMonth) {
        setBirthMonth(birthMonth);
        return this;
    }

    public Integer getBirthDay() {
        return (Integer) get(ASConstants.FIELD_BIRTH_DAY);
    }

    public void setBirthDay(Integer birthday) {
        if (birthday != null) put(ASConstants.FIELD_BIRTH_DAY, birthday);
        else remove(ASConstants.FIELD_BIRTH_DAY);
    }

    public DemographyAspect withBirthDay(Integer birthday) {
        setBirthDay(birthday);
        return this;
    }


    public Integer getFamilySize() {
        return (Integer) get(ASConstants.FIELD_FAMILY_SIZE);
    }

    public void setFamilySize(String familySize) {
        if (familySize != null) put(ASConstants.FIELD_FAMILY_SIZE, familySize);
        else remove(ASConstants.FIELD_FAMILY_SIZE);
    }

    public DemographyAspect withFamilySize(String familySize) {
        setFamilySize(familySize);
        return this;
    }

    //todo - create Esper friendly property getters

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
            case ASConstants.FIELD_ETHNICITY:
            case ASConstants.FIELD_MARITAL_STATUS:
            case ASConstants.FIELD_EMPLOYMENT:
            case ASConstants.FIELD_INCOME:
            case ASConstants.FIELD_MOSAIC_GROUP:
            case ASConstants.FIELD_HOUSING:
            case ASConstants.FIELD_EDUCATION:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_GENDER:
                value = validator().processLowerCaseString(theKey, value, false);
                break;
            case ASConstants.FIELD_GENDER_GUESSED:
                value = validator().processBoolean(theKey, value, false);
                break;
            case ASConstants.FIELD_BIRTH_DATE:
                value = validator().processIsoDate(theKey, value, false);
                break;
            case ASConstants.FIELD_BIRTH_YEAR:
            case ASConstants.FIELD_BIRTH_MONTH:
            case ASConstants.FIELD_BIRTH_DAY:
            case ASConstants.FIELD_FAMILY_SIZE:
                value = validator().processInteger(theKey, value, false, null, null);
                break;
            case ASConstants.FIELD_PROPERTIES:
                value = validator().processMap(theKey, value, false);
                break;
            default:
                if (!theKey.startsWith("_")) {
                    //todo - assign extra values to properties for convenience?
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Demography Aspect"));
                }
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    /************  Persistence ************/

    @Override
    protected void collectValuesToSave(Map<String, Object> values) {
        super.collectValuesToSave(values);
        if (getBirthDate() != null) {
            values.put(ASConstants.FIELD_BIRTH_DATE, getBirthDate().toDate());
            values.remove(ASConstants.FIELD_BIRTH_YEAR);
            values.remove(ASConstants.FIELD_BIRTH_MONTH);
            values.remove(ASConstants.FIELD_BIRTH_DAY);
        }
    }

    @Override
    protected void handleChanges(Map<String, Object> oldValues, Map<String, Object> newValues) {
        registerChanges(oldValues, newValues, EntityChangeMap.ACTION.PROCESS, EntityChangeMap.ACTION.IGNORE);
    }

    /************  Static Builders ************/

    public static DemographyAspect newFor(String gender, String birthday) {
        return new DemographyAspect(gender, birthday, null);
    }

    /**
     * Creates a new Demography Aspect instance
     * Utility function for cleaner chaining
     * @return a new Demography Aspect
     */
    public static DemographyAspect demography() {
        return new DemographyAspect();
    }

}
