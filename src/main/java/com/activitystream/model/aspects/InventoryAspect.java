package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.AnalyticsElement;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryAspect extends AbstractMapAspect implements AnalyticsElement {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_INVENTORY, InventoryAspect::new, AspectType.MergeStrategy.REPLACE);

    protected static final Logger logger = LoggerFactory.getLogger(InventoryAspect.class);

    public InventoryAspect() {
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
     * Total number of items for sale (regardless of their immediate availability)
     */
    public Double getItemsForSale() {
        return (Double) get(ASConstants.FIELD_ITEMS_FOR_SALE);
    }

    /**
     * Total number of items that could be sold but are helt back for some purposes
     */
    public Double getItemsOnHold() {
        return (Double) get(ASConstants.FIELD_ITEMS_ON_HOLD);
    }

    /**
     * Total number of items that can not be sold due to a defect or otherwize
     */
    public Double getItemsUnSellable() {
        return (Double) get(ASConstants.FIELD_ITEMS_UNSELLABLE);
    }

    /**
     * Available items that can be sold
     */
    public Double getItemsInStock() {
        return (Double) get(ASConstants.FIELD_ITEMS_IN_STOCK);
    }

    /**
     * Number of items returned
     */

    public Double getItemsReturned() {
        return (Double) get(ASConstants.FIELD_ITEMS_RETURNED);
    }

    /**
     * Total number of items sold
     */
    public Double getItemsSold() {
        return (Double) get(ASConstants.FIELD_ITEMS_SOLD);
    }

    /**
     * Total price of the items sold
     */
    public Double getGrossSold() {
        return (Double) get(ASConstants.FIELD_GROSS_SOLD);
    }

    public void resetSales() {
        put(ASConstants.FIELD_ITEMS_SOLD, 0d);
        put(ASConstants.FIELD_ITEMS_RETURNED, 0d);
        put(ASConstants.FIELD_ITEMS_RESERVED, 0d);
        put(ASConstants.FIELD_ITEMS_ON_HOLD, 0d);
        put(ASConstants.FIELD_ITEMS_UNSELLABLE, 0d);
        put(ASConstants.FIELD_ITEMS_IN_STOCK, getOrDefault(ASConstants.FIELD_ITEMS_FOR_SALE, 0d));
        for (Map category : (List<Map>) getOrDefault(ASConstants.FIELD_PRICE_CATEGORIES, new LinkedList())) {
            category.put(ASConstants.FIELD_ITEMS_SOLD, 0d);
            category.put(ASConstants.FIELD_ITEMS_ON_HOLD, 0d);
            category.put(ASConstants.FIELD_ITEMS_UNSELLABLE, 0d);
            category.put(ASConstants.FIELD_ITEMS_RETURNED, 0d);
            category.put(ASConstants.FIELD_ITEMS_RESERVED, 0d);
            category.put(ASConstants.FIELD_ITEMS_IN_STOCK, category.getOrDefault(ASConstants.FIELD_ITEMS_FOR_SALE, 0d));
        }
        for (Map variant : (List<Map>) getOrDefault(ASConstants.FIELD_VARIANTS, new LinkedList())) {
            variant.put(ASConstants.FIELD_ITEMS_SOLD, 0d);
            variant.put(ASConstants.FIELD_ITEMS_ON_HOLD, 0d);
            variant.put(ASConstants.FIELD_ITEMS_UNSELLABLE, 0d);
            variant.put(ASConstants.FIELD_ITEMS_RETURNED, 0d);
            variant.put(ASConstants.FIELD_ITEMS_RESERVED, 0d);
            variant.put(ASConstants.FIELD_ITEMS_IN_STOCK, variant.getOrDefault(ASConstants.FIELD_ITEMS_FOR_SALE, 0d));
        }
    }

    /************ Enrichment & Analytics ************/

    @Override
    public void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth) {

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

        // (items_for_sale - items_on_hold - items_unsellable) - (items_sold + items_returned) - items_reserved = items_in_stock ?

        switch (theKey) {
            case ASConstants.FIELD_ITEMS_FOR_SALE:
            case ASConstants.FIELD_ITEMS_ON_HOLD:
            case ASConstants.FIELD_ITEMS_UNSELLABLE:
            case ASConstants.FIELD_ITEMS_IN_STOCK:
            case ASConstants.FIELD_ITEMS_RESERVED:
            case ASConstants.FIELD_ITEMS_SOLD:
            case ASConstants.FIELD_ITEMS_RETURNED:
            case ASConstants.FIELD_GROSS_SOLD:
                value = validator().processDouble(theKey, value, false, null, null);
                break;
            case ASConstants.FIELD_PRICE_CATEGORIES:
            case ASConstants.FIELD_VARIANTS:
                if (!(value instanceof List)) {
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property needs to contain a List of variant based inventory."));
                }
                break;
            default:
                //allow enrichment fields (All prefixed with "_")
                if (!theKey.startsWith("_")) {
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Inventory Aspect"));
                    return null;
                }
        }
        return super.put(theKey, value);
    }

    public void doUpdateAvailability(Map availabilityMap) {
        synchronized (this) {

            //Adjust master record
            adjustInventoryMap(availabilityMap, this);

            //Adjust price category records

            List<Map> newPriceCategories = (List<Map>) availabilityMap.get(ASConstants.FIELD_PRICE_CATEGORIES);
            List<Map> localPriceCategories = (List<Map>) this.getOrDefault(ASConstants.FIELD_PRICE_CATEGORIES, new LinkedList<Map>());

            newPriceCategories.forEach(newCategoryAvailabilityMap -> {
                String priceCategoryName = (String) newCategoryAvailabilityMap.get(ASConstants.FIELD_PRICE_CATEGORY);

                boolean found = false;
                for (Map localMap : localPriceCategories) {
                    if (priceCategoryName.equals(localMap.get(ASConstants.FIELD_PRICE_CATEGORY))) {
                        found = true;
                        adjustInventoryMap(newCategoryAvailabilityMap, localMap);
                        break;
                    }
                }
                if (!found) {
                    localPriceCategories.add(newCategoryAvailabilityMap);
                }
            });

            super.put(ASConstants.FIELD_PRICE_CATEGORIES, localPriceCategories);
        }
    }

    private void adjustInventoryMap(Map availabilityMap, Map localMap) {
        Number itemOnHold = (Number) availabilityMap.getOrDefault(ASConstants.FIELD_ITEMS_ON_HOLD, 0d);
        Number itemUnsellable = (Number) availabilityMap.getOrDefault(ASConstants.FIELD_ITEMS_UNSELLABLE, 0d);
        Number itemForSale = (Number) availabilityMap.getOrDefault(ASConstants.FIELD_ITEMS_FOR_SALE, 0d);
        Number localItemSold = (Number) localMap.getOrDefault(ASConstants.FIELD_ITEMS_SOLD, 0d);
        Number localItemReturned = (Number) localMap.getOrDefault(ASConstants.FIELD_ITEMS_RETURNED, 0d);
        localMap.put(ASConstants.FIELD_ITEMS_ON_HOLD, itemOnHold.doubleValue());
        localMap.put(ASConstants.FIELD_ITEMS_UNSELLABLE, itemUnsellable.doubleValue());
        localMap.put(ASConstants.FIELD_ITEMS_FOR_SALE, itemForSale.doubleValue());
        localMap.put(ASConstants.FIELD_ITEMS_IN_STOCK, itemForSale.doubleValue() - (localItemSold.doubleValue() + localItemReturned.doubleValue()));
        localMap.put(ASConstants.FIELD_ITEMS_RETURNED, Math.abs(localItemReturned.doubleValue()));
        localMap.put(ASConstants.FIELD_ITEMS_SOLD, localItemSold.doubleValue());
    }

    public void doUpdateInventory(Double itemCount, String variant, String priceCategory) {

        logger.debug("------------------------------------------------------------------------------");
        logger.debug("Inventory change               : " + itemCount + " " + variant + " " + priceCategory);
        logger.debug("Inventory aspect before changes: " + this);

        if (itemCount == null) return;

        synchronized (this) {
            if (this.containsKey(ASConstants.FIELD_ITEMS_SOLD) && this.get(ASConstants.FIELD_ITEMS_SOLD) != null) {
                if (this.get(ASConstants.FIELD_ITEMS_SOLD) instanceof Integer) {
                    this.put(ASConstants.FIELD_ITEMS_SOLD, new Double("" + (((Integer) this.get(ASConstants.FIELD_ITEMS_SOLD)) + itemCount)));
                } else {
                    this.put(ASConstants.FIELD_ITEMS_SOLD, ((Double) ((Double) this.get(ASConstants.FIELD_ITEMS_SOLD)) + itemCount));
                }
            } else {
                this.put(ASConstants.FIELD_ITEMS_SOLD, itemCount);
            }

            if (this.containsKey(ASConstants.FIELD_ITEMS_IN_STOCK) && this.get(ASConstants.FIELD_ITEMS_IN_STOCK) != null) {
                if (this.get(ASConstants.FIELD_ITEMS_IN_STOCK) instanceof Integer) {
                    this.put(ASConstants.FIELD_ITEMS_IN_STOCK,
                            new Double("" + (((Integer) this.get(ASConstants.FIELD_ITEMS_IN_STOCK)).doubleValue() - itemCount)));
                } else {
                    this.put(ASConstants.FIELD_ITEMS_IN_STOCK, ((Double) ((Double) this.get(ASConstants.FIELD_ITEMS_IN_STOCK)) - itemCount));
                }
            } else {
                this.put(ASConstants.FIELD_ITEMS_IN_STOCK, itemCount * -1);
            }

            if (itemCount < 0) {
                if (this.get(ASConstants.FIELD_ITEMS_RETURNED) instanceof Integer) {
                    this.put(ASConstants.FIELD_ITEMS_RETURNED,
                            new Double("" + (((Integer) this.get(ASConstants.FIELD_ITEMS_RETURNED)).doubleValue() + Math.abs(itemCount))));
                } else {
                    this.put(ASConstants.FIELD_ITEMS_RETURNED,
                            ((Double) ((Double) this.getOrDefault(ASConstants.FIELD_ITEMS_RETURNED, 0d)) + Math.abs(itemCount)));
                }
            }

            if (priceCategory != null) {
                try {
                    List<Map> price_categories = (List<Map>) this.get(ASConstants.FIELD_PRICE_CATEGORIES);

                    if (price_categories != null) {
                        boolean found = false;
                        for (Map<String, Object> price_category : price_categories) {
                            if (price_category.containsKey(ASConstants.FIELD_PRICE_CATEGORY) &&
                                    price_category.get(ASConstants.FIELD_PRICE_CATEGORY).equals(priceCategory)) {
                                found = true;
                                if (price_category.containsKey(ASConstants.FIELD_ITEMS_SOLD)) {
                                    if (price_category.get(ASConstants.FIELD_ITEMS_SOLD) instanceof Integer) {
                                        price_category.put(
                                                ASConstants.FIELD_ITEMS_SOLD,
                                                new Double("" + (((Integer) price_category.get(ASConstants.FIELD_ITEMS_SOLD)) + itemCount)));
                                    } else {
                                        price_category.put(
                                                ASConstants.FIELD_ITEMS_SOLD,
                                                ((Double) ((Double) price_category.get(ASConstants.FIELD_ITEMS_SOLD)) + itemCount));
                                    }
                                }
                                if (price_category.containsKey(ASConstants.FIELD_ITEMS_IN_STOCK)) {
                                    if (price_category.get(ASConstants.FIELD_ITEMS_IN_STOCK) instanceof Integer) {
                                        price_category.put(ASConstants.FIELD_ITEMS_IN_STOCK, new Double("" + (((Integer) price_category.get(
                                                ASConstants.FIELD_ITEMS_IN_STOCK)) - itemCount)));
                                    } else {
                                        price_category.put(
                                                ASConstants.FIELD_ITEMS_IN_STOCK,
                                                ((Double) ((Double) price_category.get(ASConstants.FIELD_ITEMS_IN_STOCK)) - itemCount));
                                    }
                                }
                                if (itemCount < 0) {
                                    if (price_category.get(ASConstants.FIELD_ITEMS_RETURNED) instanceof Integer) {
                                        price_category.put(ASConstants.FIELD_ITEMS_RETURNED,
                                                new Double("" + (((Integer) price_category.get(ASConstants.FIELD_ITEMS_RETURNED)) + Math.abs(itemCount))));
                                    } else {
                                        price_category.put(ASConstants.FIELD_ITEMS_RETURNED,
                                                ((Double) ((Double) price_category.getOrDefault(ASConstants.FIELD_ITEMS_RETURNED, 0d)) + Math.abs(itemCount)));
                                    }
                                }
                            }
                        }
                        if (!found) {
                            price_categories.add(new ConcurrentHashMap<String, Object>() {{
                                put(ASConstants.FIELD_PRICE_CATEGORY, priceCategory);
                                put(ASConstants.FIELD_ITEMS_SOLD, new Double(itemCount));
                                put(ASConstants.FIELD_ITEMS_IN_STOCK, new Double(itemCount * -1));
                                put(ASConstants.FIELD_ITEMS_RESERVED, new Double(0));
                                put(ASConstants.FIELD_ITEMS_RETURNED, new Double(0));
                                put(ASConstants.FIELD_GROSS_SOLD, new Double(0));
                            }});
                        }
                    } else {
                        price_categories = new LinkedList();
                        price_categories.add(new ConcurrentHashMap<String, Object>() {{
                            put(ASConstants.FIELD_PRICE_CATEGORY, priceCategory);
                            put(ASConstants.FIELD_ITEMS_SOLD, new Double(itemCount));
                            put(ASConstants.FIELD_ITEMS_IN_STOCK, new Double(itemCount * -1));
                            put(ASConstants.FIELD_ITEMS_RESERVED, new Double(0));
                            put(ASConstants.FIELD_ITEMS_RETURNED, new Double(0));
                            put(ASConstants.FIELD_GROSS_SOLD, new Double(0));
                        }});
                        super.put(ASConstants.FIELD_PRICE_CATEGORIES, price_categories);
                    }
                } catch (Exception r) {
                    logger.warn("ahem ... " + r, r);
                }
            }
        }
        logger.debug("Inventory aspect after changes : " + this);
    }

    @Override
    public void verify() {

    }

}
