package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.ASEntity;
import com.activitystream.model.interfaces.AnalyticsElement;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;

import java.util.*;

public class ClassificationAspect extends AbstractMapAspect implements AnalyticsElement {

    public static final AspectType ASPECT_TYPE =
            new AspectType.Embedded(ASConstants.ASPECTS_CLASSIFICATION, ClassificationAspect::new, AspectType.MergeStrategy.MERGE) {
            };

    /**
     * The Classification Aspects is used to categorise/classify entities
     * It provides a 3 level structure beneath the Entity Type (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * The classification properties should be filled out in this order: type, variant, categories
     * Behavioral tags can be added using the tags property
     */
    public ClassificationAspect() {
    }


     /**
     * The Classification Aspects is used to categorise/classify entities
     * It provides a 3 level structure beneath the Entity Type (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * The classification properties should be filled out in this order: type, variant, categories
     * Behavioral tags can be added using the tags property
     * @param type The classification Type (1st level)
     * @param variant  The classification Variant (2nd level)
     * @param root Parent entity
     */
    public ClassificationAspect(String type, String variant, BaseStreamElement root) {
        setRoot(root);
        put(ASConstants.FIELD_TYPE, type);
        put(ASConstants.FIELD_VARIANT, variant);
    }

    /**
     * The Classification Aspects is used to categorise/classify entities
     * It provides a 3 level structure beneath the Entity Type (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * The classification properties should be filled out in this order: type, variant, categories
     * Behavioral tags can be added using the tags property
     * @param type The classification Type (1st level)
     * @param variant  The classification Variant (2nd level)
     */
    public ClassificationAspect(String type, String variant) {
        put(ASConstants.FIELD_TYPE, type);
        if (variant != null) put(ASConstants.FIELD_VARIANT, variant);
    }

    /**
     * The Classification Aspects is used to categorise/classify entities
     * It provides a 3 level structure beneath the Entity Type (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * The classification properties should be filled out in this order: type, variant, categories
     * Behavioral tags can be added using the tags property
     * @param type The classification Type (1st level)
     */
    public ClassificationAspect(String type) {
        this(type, null);
    }

    /**
     * The Classification Aspects is used to categorise/classify entities
     * It provides a 3 level structure beneath the Entity Type (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * The classification properties should be filled out in this order: type, variant, categories
     * Behavioral tags can be added using the tags property
     * @param type The classification Type (1st level)
     * @param variant  The classification Variant (2nd level)
     * @param categories The classification Categories (2nd level)
     */
    public ClassificationAspect(String type, String variant, String ... categories) {
        put(ASConstants.FIELD_TYPE, type);
        if (variant != null) put(ASConstants.FIELD_VARIANT, variant);
        if (categories != null) put(ASConstants.FIELD_CATEGORIES, categories);
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

    /**
     * Get the classification Variant for the entity
     * Type is the 1st level classification setting (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * @return
     */
    public String getType() {
        return (String) get(ASConstants.FIELD_TYPE);
    }

    /**
     * Set the classification Type for the entity
     * Type is the 1st level classification setting (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * Classification type should not be the same as the Entity Type as that would be completely redundant
     * @param type
     */
    public void setType(String type) {
        if (type != null && !type.isEmpty()) put(ASConstants.FIELD_TYPE, type);
        else remove(ASConstants.FIELD_TYPE, type);
    }

    /**
     * Set the classification Type for the entity
     * Type is the 1st level classification setting (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * Classification type should not be the same as the Entity Type as that would be completely redundant
     * @param type
     */
    public ClassificationAspect addType(String type) {
        setType(type);
        return this;
    }

    /**
     * Get the classification Variant for the entity
     * Variant is the 2nd level classification setting (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * @return
     */
    public String getVariant() {
        return (String) get(ASConstants.FIELD_VARIANT);
    }

    /**
     * Set the classification variant for the entity
     * Variant is the 2nd level classification setting and is, as such, optional for classification when no other classification exists a part from the type
     * Use type to specify the main classification category for the entity
     * @param variant
     * @return
     */
    public void setVariant(String variant) {
        if (variant != null && !variant.isEmpty()) put(ASConstants.FIELD_VARIANT, variant);
        else remove(ASConstants.FIELD_VARIANT);
    }

    /**
     * Set the classification variant for the entity
     * Variant is the 2nd level classification setting and is, as such, optional for classification when no other classification exists a part from the type
     * Use type to specify the main classification category for the entity
     * @param variant
     * @return
     */
    public ClassificationAspect addVariant(String variant) {
        setVariant(variant);
        return this;
    }

    /**
     * Get the list of classification categories set for the entity
     * @return
     */
    public List<String> getCategories() {
        return (List<String>) get(ASConstants.FIELD_CATEGORIES);
    }


    /**
     * Set classification categories of the entity
     * Classification categories are the 3rd level of classification setting (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * Use Type and Variant to specify the main classification groups and put additional categorization into categories
     * @param categories
     */
    public void setCategories(List<String> categories) {
        if (categories != null && !categories.isEmpty()) put(ASConstants.FIELD_CATEGORIES, categories);
        else remove(ASConstants.FIELD_CATEGORIES);
    }

    /**
     * Set classification categories of the entity
     * Classification categories are the 3rd level of classification setting (EntityType -> Classification.type -> Classification.variant -> Classification.categories)
     * Use Type and Variant to specify the main classification groups and put additional categorization into categories
     * @param categories
     */
    public ClassificationAspect addCategories(List<String> categories) {
        setCategories(categories);
        return this;
    }

    public ClassificationAspect addCategories(String ... categories) {
        return addCategories(Arrays.asList(categories));
    }

    /**
     * Get the behavioral tags set for an entity
     * @return
     */
    public Set<String> getTags() {
        Collection<String> tags = (Collection<String>) get(ASConstants.FIELD_TAGS);
        if (tags == null) return null;
        return resolveDelta(tags, null);
    }

    /**
     * Set the behavioral tags set for an entity
     * @param tags a list of behavioral tags
     */
    public void setTags(List<String> tags) {
        if (tags != null && !tags.isEmpty()) put(ASConstants.FIELD_TAGS, tags);
        else remove(ASConstants.FIELD_TAGS);
    }

    /**
     * Set the behavioral tags set for an entity
     * @param tags a list of behavioral tags
     */
    public ClassificationAspect addTags(List<String> tags) {
        setTags(tags);
        return this;
    }

    /**
     * Set the behavioral tags set for an entity
     * @param tags a list of behavioral tags
     */
    public ClassificationAspect addTags(String ... tags) {
        return addTags(Arrays.asList(tags));
    }


    /************ Enrichment & Analytics ************/

    @Override
    public void addTimeSeriesDimensions(TimeSeriesEntry entry) {

    }

    @Override
    public void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth) {

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

        if (value == null) {
            remove(key);
            return null;
        }

        switch (theKey) {
            case ASConstants.FIELD_TYPE:
            case ASConstants.FIELD_VARIANT:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_TAGS:
            case ASConstants.FIELD_CATEGORIES:
                value = validator().processSimpleValueList(theKey, value, false, "/");
                break;
            case ASConstants.FIELD_LAST_UPDATED:
                value = validator().processIsoDateTime(theKey, value, false);
                break;
            case ASConstants.FIELD_OUTLOOK:
                value = validator().processDouble(theKey, value, false, -5.0, 5.0);
                break;
            case ASConstants.FIELD_RANKING:
                value = validator().processDouble(theKey, value, false, 0.0, 100.0);
                break;
            case ASConstants.FIELD_WEIGHT:
                value = validator().processDouble(theKey, value, false, -Double.MAX_VALUE, Double.MAX_VALUE);
                break;
            case ASConstants.FIELD_ACTIVE_SINCE:
                value = validator().processIsoDateTime(theKey, value, false);
                break;
            default:
                if (!theKey.startsWith("_"))
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Classification Aspect"));
        }
        if (value instanceof String) value = ((String) value).trim();

        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }

    /************  Persistence ************/

    @Override
    protected void collectValuesToSave(Map<String, Object> values) {
        resolveDeltas(values, ASConstants.FIELD_TAGS);
        resolveDeltas(values, ASConstants.FIELD_CATEGORIES);

        super.collectValuesToSave(values);
    }

    private void resolveDeltas(Map<String, Object> values, String key) {
        Collection<String> newList = (Collection<String>) get(key);
        if (newList != null)
            super.put(key, new ArrayList<>(resolveDelta(newList, (Collection<String>) values.get(key))));
    }

    private Set<String> resolveDelta(Collection<String> newList, Collection<String> oldList) {
        Set<String> result = new LinkedHashSet<>();
        if (oldList != null)
            result.addAll(oldList);

        for (String newItem : newList) {
            if (newItem.startsWith("+")) result.add(newItem.substring(1));
            else if (newItem.startsWith("-")) result.remove(newItem.substring(1));
            else return new LinkedHashSet<>(newList);
        }
        return result;
    }

    /**
     * Creates a new Classification Aspect instance
     * Utility function for cleaner chaining
     * @return a new Classification Aspect
     */
    public static ClassificationAspect classification() {
        return new ClassificationAspect();
    }

}
