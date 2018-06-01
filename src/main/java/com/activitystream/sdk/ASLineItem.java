package com.activitystream.sdk;

import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.interfaces.BaseStreamElement;
import com.activitystream.core.model.relations.Relation;
import com.activitystream.core.model.stream.TransactionEvent;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Set;

public class ASLineItem extends TransactionEvent {

    /**
     * Popular relationship types for line items
     * You can add use your own relationship types by providing a string value of some valid event-entity relationship type
     */
    public static enum REL_TYPES {
        /**
         * THe offer used for price calculation
         */
        RATED_BY,
        /**
         * The entity responsible for the sale
         */
        SOLD_BY,
        /**
         * An entity or entity relation, like ticket, that is created as a result of this purchase
         */
        CREATES,
        /**
         * An entity or entity relationship removed as as a part of this cancellation
         */
        REMOVES
    }

    /**
     * A conclusive list of valid line-item->product relationship types
     */
    public static enum LINE_TYPES {
        /**
         * Items in the line were put in a cart (as a pre-purchase step)
         */
        CARTED,
        /**
         * Items in the line were put in a un-carted (as a pre-purchase step)
         */
        UNCARTED,
        /**
         * Items in the line were reserved with the intent of later purchase
         */
        RESERVED,
        /**
         * Items in the line were un-reserved with the intent of cancelling a future purchase
         */
        UNRESERVED,
        /**
         * Items in the line were purchased
         */
        PURCHASED,
        /**
         * Items in the line were returned (applies to PURCHASED, RENTED, GOT, LEASED and WON)
         */
        RETURNED,
        /**
             * Items in the line were rented for a short period
         */
        RENTED,
        /**
         * Items in the line were gotten without a financial transaction taking place.
         */
        GOT,
        /**
         * Items in the line were leased for a short long period
         */
        LEASED,
        /**
         * Items in the line were won in some way
         */
        WON,
        /**
         * Items in the line were ordered
         */
        ORDERED,
        /**
         * A order for the items in the line were cancelled (applies to ORDERED only)
         */
        CANCELLED,
        /**
         * Items in the line were not available when the customer showd interest in them
         */
        UNAVAILABLE,
        /**
         * Items in the line were re-sold by the SOLD_BY entity (Aftermarket tracking)
         */
        RESOLD,
        /**
         * Items in the line were re-purchased by a new owner(Aftermarket tracking)
         */
        REPURCHASED
    }

    public ASLineItem(LINE_TYPES type, ASEntity product, double itemCount, double itemPrice) {
        withItemCount(itemCount);
        withItemPrice(itemPrice);
        withProduct(type, product);
    }

    public ASLineItem(LINE_TYPES type, ASEntity product, String itemCount, String itemPrice) {
        withProduct(type, product);
        withItemCount(itemCount);
        withItemPrice(itemPrice);
    }

    public ASLineItem(LINE_TYPES type, ASEntity product, Number itemCount, String itemPrice) {
        withProduct(type, product);
        withItemCount(itemCount.doubleValue());
        withItemPrice(itemPrice);
    }

    public ASLineItem() {

    }

    public ASLineItem(Map map, BaseStreamElement root) {
        super(map, root);
    }

    public ASLineItem(Map map) {
        super(map);
    }


    /**
     * What is the tpe of this line item.
     * @param transactionType Relationship type
     * @return this ASLineItem for chaining
     */
    @Deprecated
    public ASLineItem withTransactionType(String transactionType) {
        super.setTransactionType(transactionType);
        return this;
    }

    /**
     * Sets the type of relations will be created between the actor and the product in this line
     * @param type Relationship type
     * @return this ASLineItem for chaining
     */
    public ASLineItem withTransactionType(LINE_TYPES type) {
        super.setTransactionType(type.toString());
        return this;
    }

    /**
     * What product is the target of this line item
     * @param purchaseRelation the main relationship for the line item
     * @return this ASLineItem for chaining
     */
    public ASLineItem withProduct(Relation purchaseRelation) {
        super.setPurchaseRelation(purchaseRelation);
        return this;
    }

    public ASLineItem withProduct(String entityRef) {
        withProduct(getTransactionType(),new ASEntity(entityRef));
        return this;
    }

    /**
     * A utility function for setting the Line Item type and the product at the same time.
     * Please refer to the respective functions for documentation.
     * @param transactionType
     * @param product The product which is the target of this line item
     * @return this ASLineItem for chaining
     */
    @Deprecated
    public ASLineItem withProduct(String transactionType, ASEntity product) {
        super.setPurchaseRelation(new Relation(transactionType, product));
        return this;
    }

    public ASLineItem withProduct(LINE_TYPES type, ASEntity product) {
        getRelationsManager(true).addRelation(new Relation(type.toString(), product));
        return this;
    }

    /**
     * A product variant, like ticket type, is a variant of the product without being a separatly registered product.
     * Products are usually a complete entity but sometimes the variant (color, size) are treated as variant of a single product.
     * Inventory calculations are done down to the variant level.
     * @param variant
     * @return this ASLineItem for chaining
     */
    public ASLineItem withVariant(String variant) {
        super.setVariant(variant);
        return this;
    }

    /**
     * How many items of the product are the subjects of this line item.
     * @param count
     * @return this ASLineItem for chaining
     */
    public ASLineItem withItemCount(Number count) {
        if (count != null) super.setItemCount(count.doubleValue());
        else super.setItemCount(null);
        return this;
    }

    /**
     * How many items of the product are the subjects of this line item.
     * @param count
     * @return this ASLineItem for chaining
     */
    public ASLineItem withItemCount(String count) {
        if (count != null) withItemCount(Double.parseDouble(count));
        return this;
    }

    /**
     * What is the price of the individual item.
     * If individual items of the same product have different prices then create multiple line items
     * @param price
     * @return this ASLineItem for chaining
     */
    public ASLineItem withItemPrice(Number price) {
        super.setItemPrice(price);
        return this;
    }

    /**
     * What is the price of the individual item.
     * If individual items of the same product have different prices then create multiple line items
     * @param price
     * @return this ASLineItem for chaining
     */
    public ASLineItem withItemPrice(String price) {
        super.setItemPrice(price);
        return this;
    }

    /**
     * What currency is used to pay for the product
     * @param currency
     * @return this ASLineItem for chaining
     */
    public ASLineItem withCurrency(String currency) {
        super.setCurrency(currency);
        return this;
    }

    /**
     * What price type is being applied to the line item.
     * @param priceType Price Type refers to the buyer type or speciality pricing ("Children", "Seniors") while Price Category refers to product specific
     *                  pricing (Zone or Treatment)
     * @return this ASLineItem for chaining
     */
    public ASLineItem withPriceType(String priceType) {
        super.setPriceType(priceType);
        return this;
    }

    /**
     * What price category is being applied to the line item.
     * Some products have different price categories available in ticketing
     * @param priceCategory
     * @return this ASLineItem for chaining
     */
    public ASLineItem withPriceCategory(String priceCategory) {
        super.setPriceCategory(priceCategory);
        return this;
    }

    /**
     * If the revenue generated by this line should be categorised on a particular account key than that can be specified here
     * @param accountingKey
     * @return this ASLineItem for chaining
     */
    public ASLineItem withAccountingKey(String accountingKey) {
        super.setAccountingKey(accountingKey);
        return this;
    }

    /**
     * If this products belonging to this line item are complimentary then that can be specified here.
     * This usually means that the price of the line item is 0 but that is not enforced here
     * @param complimentary
     * @return this ASLineItem for chaining
     */
    public ASLineItem withComplimentary(Boolean complimentary) {
        super.setComplimentary(complimentary);
        return this;
    }

    /**
     * If this products belonging to this line item are complimentary then that can be specified here.
     * This usually means that the price of the line item is 0 but that is not enforced here
     * @return this ASLineItem for chaining
     */
    public ASLineItem withAsComplimentary() {
        super.setComplimentary(true);
        return this;
    }

    /**
     * Specify the discount that should be deducted from the total price of this line as a percentage of the itemPrice * itemCount amount
     * @param commission
     * @return this ASLineItem for chaining
     */
    public ASLineItem withFixedCommission(Double commission) {
        super.setCommissionFixed(commission);
        return this;
    }

    /**
     * Specify the discount that should be deducted from the total price of this line as a fixed amount
     * @param commission
     * @return this ASLineItem for chaining
     */
    public ASLineItem withFixedCommission(String commission) {
        super.setCommissionFixed(commission);
        return this;
    }

    /**
     * Specify how much commission should be calculated from the total for this line item
     * Commission is not added or deducted fromthe price. It's used to calculate the yield of the product.
     * @param commission
     * @return this ASLineItem for chaining
     */
    public ASLineItem withCommission(Double commission) {
        super.setCommissionPercentage(commission);
        return this;
    }

    /**
     * If this line item should have a particular description then add it here.
     * This should not be used if the description can easily be assembled/templated from any of the details of the line item or the product
     * @param description
     * @return this ASLineItem for chaining
     */
    public ASLineItem withDescription(String description) {
        super.setDescription(description);
        return this;
    }

    /**
     * Specifies how much tax should be added on top of the total for this line item
     * total price = ((item_count * item_price) - discount) + ((item_count * item_price) - discount) * taxPercentage
     * @param taxPercentage
     * @return this ASLineItem for chaining
     */
    public ASLineItem withTax(Double taxPercentage) {
        super.setTaxPercentage(taxPercentage);
        return this;
    }

    /**
     * Discount amount of the price.
     * **/
    public ASLineItem withDiscountAmount(Double discountAmount) {
        super.setDiscountAmount(discountAmount);
        return this;
    }

    /**
     * Percentage discount on price.
     * **/
    public ASLineItem withDiscountPercentage(Double discountPercentage) {
        super.setDiscountPercentage(discountPercentage);
        return this;
    }

    /**
     * If the product has a start date/time then this is it.
     * Please specify this for any shows or events (if known)
     * @param validFrom
     * @return this ASLineItem for chaining
     */
    public ASLineItem withValidFrom(DateTime validFrom) {
        super.setValidFrom(validFrom);
        return this;
    }

    /**
     * If the product has a end date/time then this is it
     * Please specify this for shows and events (if known)
     * @param validUntil
     * @return this ASLineItem for chaining
     */
    public ASLineItem withValidUntil(DateTime validUntil) {
        super.setValidUntil(validUntil);
        return this;
    }


    /**
     * Line ids are used to identify duplicates
     * A order with 10 lines may. for example, have a unique id ofr each item of the order.
     * @param ids
     * @return this ASLineItem for chaining
     */
    public ASLineItem withLineIds(Set<String> ids) {
        super.addToLineIds(ids);
        return this;
    }

    public ASLineItem withLineId(String id) {
        super.appendLineId(id);
        return this;
    }

    /**
     * Information regarding which payment method is used to pay for this line item
     * This is often the same for the whole order but not without exceptions
     * @param paymentMethod
     * @return this ASLineItem for chaining
     */
    public ASLineItem withPaymentMethod(String paymentMethod) {
        super.setPaymentMethod(paymentMethod);
        return this;
    }

    /**
     * If the payment is made with a card then this is a obfuscated code for that card.
     * This is not the credit card number it self but a card number always produces the same code
     * @param cardToken
     * @return this ASLineItem for chaining
     */
    public ASLineItem withCardToken(String cardToken) {
        super.setCardToken(cardToken);
        return this;
    }


    //Special purpose relations

    @Override
    public ASLineItem withRelation(Relation relation) {
        super.withRelation(relation);
        return this;
    }

    /**
     * The business entity responsible for selling the production belonging to this line item
     * @param soldByEntityRef
     * @return this ASLineItem for chaining
     */
    public ASLineItem withSoldBy(EntityReference soldByEntityRef) {
        withRelationIfValid(REL_TYPES.SOLD_BY.toString(), soldByEntityRef);
        return this;
    }

    /**
     * The business entity used to rate this entity (Offer / Rating table)
     * @param offerEntityRef
     * @return this ASLineItem for chaining
     */
    public ASLineItem withOffer(EntityReference offerEntityRef) {
        withRelationIfValid(REL_TYPES.RATED_BY.toString(), offerEntityRef);
        return this;
    }

    //Relations and aspects

    public ASLineItem withDefaults() {
        setCurrency((String) this.getOrDefault(ASConstants.FIELD_CURRENCY, ASService.getDefaultCurrency()));
        return this;
    }

    public ASLineItem withRelationIfValid(String type, String entityType, String entityId) {
        return withRelationIfValid(type, entityType, entityId, (Map) null);
    }

    public ASLineItem withRelationIfValid(String type, String entityType, String entityId, String mustBeValue) {
        if (mustBeValue != null && !mustBeValue.isEmpty()) withRelationIfValid(type, entityType, entityId, (Map) null);
        return this;
    }

    public ASLineItem withDimensions(Map dimensionsMap) {
        super.addDimensions(dimensionsMap, this);
        return this;
    }

    public ASLineItem withDimensions(String... dimensions) {
        super.addDimensions(this, dimensions);
        return this;
    }

    public ASLineItem withDimension(String dimension, String value) {
        if (value != null && !value.isEmpty()) super.addDimension(dimension, value, this);
        return this;
    }

    public ASLineItem withRelationIfValid(String type, String entityType, String entityId, Map<String,Object> relationsProperties) {
        if (!entityType.isEmpty() && entityId != null && !entityId.isEmpty()) {
            Relation newRelation = new Relation(type, new EntityReference(entityType, entityId, this), this);
            if (relationsProperties != null) {
                newRelation.directPut("properties", relationsProperties);
            }
            this.getRelationsManager(true).addRelation(newRelation);
        }
        return this;
    }

    public ASLineItem withRelationIfValid(String type, EntityReference entityRef) {
        if (entityRef != null) {
            this.getRelationsManager(true).addRelation(new Relation(type, entityRef));
        }
        return this;
    }

    public ASLineItem withRelationIfValid(String type, String entityRef) {
        if (entityRef != null) {
            this.getRelationsManager(true).addRelation(new Relation(type, entityRef));
        }
        return this;
    }

    public static ASLineItem lineItem() {
        return new ASLineItem();
    }

}
