package com.activitystream.model;

import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.stream.TransactionEvent;
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
         * Items in the line were purchased
         */
        PURCHASED,
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
         * Items in the line were returned (applies to PURCHASED, RENTED, GOT, LEASED and WON)
         */
        RETURNED,
        /**
         * Items in the line were ordered
         */
        ORDERED,
        /**
         * A order for the items in the line were cancelled (applies to ORDERED only)
         */
        CANCELLED,
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
        addItemCount(itemCount);
        addItemPrice(itemPrice);
        addProduct(type, product);
    }

    public ASLineItem(LINE_TYPES type, ASEntity product, String itemCount, String itemPrice) {
        addProduct(type, product);
        addItemCount(itemCount);
        addItemPrice(itemPrice);
    }

    public ASLineItem(LINE_TYPES type, ASEntity product, Number itemCount, String itemPrice) {
        addProduct(type, product);
        addItemCount(itemCount.doubleValue());
        addItemPrice(itemPrice);
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
     * @return the same class for chaining
     */
    @Deprecated
    public ASLineItem addTransactionType(String transactionType) {
        super.setTransactionType(transactionType);
        return this;
    }

    /**
     * Sets the type of relations will be created between the actor and the product in this line
     * @param type Relationship type
     * @return the same class for chaining
     */
    public ASLineItem addTransactionType(LINE_TYPES type) {
        super.setTransactionType(type.toString());
        return this;
    }

    /**
     * What product is the target of this line item
     * @param purchaseRelation the main relationship for the line item
     * @return the same class for chaining
     */
    public ASLineItem addProduct(Relation purchaseRelation) {
        super.setPurchaseRelation(purchaseRelation);
        return this;
    }

    /**
     * A utility function for setting the Line Item type and the product at the same time.
     * Please refer to the respective functions for documentation.
     * @param transactionType
     * @param product The product which is the target of this line item
     * @return the same class for chaining
     */
    @Deprecated
    public ASLineItem addProduct(String transactionType, ASEntity product) {
        super.setPurchaseRelation(new Relation(transactionType, product));
        return this;
    }

    public ASLineItem addProduct(LINE_TYPES type, ASEntity product) {
        getRelationsManager(true).add(new Relation(type.toString(), product));
        return this;
    }

    /**
     * A product variant, like ticket type, is a variant of the product without being a separatly registered product.
     * Products are usually a complete entity but sometimes the variant (color, size) are treated as variant of a single product.
     * Inventory calculations are done down to the variant level.
     * @param variant
     * @return
     */
    public ASLineItem addVariant(String variant) {
        super.setVariant(variant);
        return this;
    }

    /**
     * How many items of the product are the subjects of this line item.
     * @param count
     * @return
     */
    public ASLineItem addItemCount(Double count) {
        super.setItemCount(count);
        return this;
    }

    /**
     * What is the price of the individual item.
     * If individual items of the same product have different prices then create multiple line items
     * @param price
     * @return
     */
    public ASLineItem addItemPrice(Double price) {
        super.setItemPrice(price);
        return this;
    }

    /**
     * What is the price of the individual item.
     * If individual items of the same product have different prices then create multiple line items
     * @param price
     * @return
     */
    public ASLineItem addItemPrice(String price) {
        super.setItemPrice(price);
        return this;
    }

    /**
     * What currency is used to pay for the product
     * @param currency
     * @return
     */
    public ASLineItem addCurrency(String currency) {
        super.setCurrency(currency);
        return this;
    }

    /**
     * What price type is being applied to the line item.
     * Some products have different price types available in ticketing
     * @param priceType
     * @return
     */
    public ASLineItem addPriceType(String priceType) {
        super.setPriceType(priceType);
        return this;
    }

    /**
     * What price category is being applied to the line item.
     * Some products have different price categories available in ticketing
     * @param priceCategory
     * @return
     */
    public ASLineItem addPriceCategory(String priceCategory) {
        super.setPriceCategory(priceCategory);
        return this;
    }

    /**
     * If the revenue generated by this line should be categorised on a particular account key than that can be specified here
     * @param accountingKey
     * @return
     */
    public ASLineItem addAccountingKey(String accountingKey) {
        super.setAccountingKey(accountingKey);
        return this;
    }

    /**
     * If this products belonging to this line item are complimentary then that can be specified here.
     * This usually means that the price of the line item is 0 but that is not enforced here
     * @param complimentary
     * @return
     */
    public ASLineItem addComplimentary(Boolean complimentary) {
        super.setComplimentary(complimentary);
        return this;
    }

    /**
     * Specify the discount that should be deducted from the total price of this line as a percentage of the itemPrice * itemCount amount
     * @param commission
     * @return
     */
    public ASLineItem addCommissionFixed(Double commission) {
        super.setCommissionFixed(commission);
        return this;
    }

    /**
     * Specify the discount that should be deducted from the total price of this line as a fixed amount
     * @param commission
     * @return
     */
    public ASLineItem addCommissionFixed(String commission) {
        super.setCommissionFixed(commission);
        return this;
    }

    /**
     * Specify how much commission should be calculated from the total for this line item
     * Commission is not added or deducted fromthe price. It's used to calculate the yield of the product.
     * @param commission
     * @return
     */
    public ASLineItem addCommissionPercentage(Double commission) {
        super.setCommissionPercentage(commission);
        return this;
    }

    /**
     * If this line item should have a particular description then add it here.
     * This should not be used if the description can easily be assembled/templated from any of the details of the line item or the product
     * @param description
     * @return
     */
    public ASLineItem addDescription(String description) {
        super.setDescription(description);
        return this;
    }

    /**
     * Specifies how much tax should be added on top of the total for this line item
     * total price = ((item_count * item_price) - discount) + ((item_count * item_price) - discount) * taxPercentage
     * @param taxPercentage
     * @return
     */
    public ASLineItem addTaxPercentage(Double taxPercentage) {
        super.setTaxPercentage(taxPercentage);
        return this;
    }

    /**
     * If the product has a start date/time then this is it.
     * Please specify this for any shows or events (if known)
     * @param validFrom
     * @return
     */
    public ASLineItem addValidFrom(DateTime validFrom) {
        super.setValidFrom(validFrom);
        return this;
    }

    /**
     * If the product has a end date/time then this is it
     * Please specify this for shows and events (if known)
     * @param validUntil
     * @return
     */
    public ASLineItem addValidUntil(DateTime validUntil) {
        super.setValidUntil(validUntil);
        return this;
    }


    /**
     * Line ids are used to identify duplicates
     * A order with 10 lines may. for example, have a unique id ofr each item of the order.
     * @param ids
     * @return
     */
    public ASLineItem addLineIds(Set<String> ids) {
        super.setLineIds(ids);
        return this;
    }

    public ASLineItem addLineId(String id) {
        super.appendLineId(id);
        return this;
    }

    /**
     * Information regarding which payment method is used to pay for this line item
     * This is often the same for the whole order but not without exceptions
     * @param paymentMethod
     * @return
     */
    public ASLineItem addPaymentMethod(String paymentMethod) {
        super.setPaymentMethod(paymentMethod);
        return this;
    }

    /**
     * If the payment is made with a card then this is a obfuscated code for that card.
     * This is not the credit card number it self but a card number always produces the same code
     * @param cardToken
     * @return
     */
    public ASLineItem addCardToken(String cardToken) {
        super.setCardToken(cardToken);
        return this;
    }


    //Special purpose relations

    /**
     * The business entity responsible for selling the production belonging to this line item
     * @param soldByEntityRef
     * @return
     */
    public ASLineItem addSoldBy(EntityReference soldByEntityRef) {
        addRelationIfValid(REL_TYPES.SOLD_BY.toString(), soldByEntityRef);
        return this;
    }

    /**
     * The business entity used to rate this entity (Offer / Rating table)
     * @param offerEntityRef
     * @return
     */
    public ASLineItem addOffer(EntityReference offerEntityRef) {
        addRelationIfValid(REL_TYPES.RATED_BY.toString(), offerEntityRef);
        return this;
    }

    //Relations and aspects

    public ASLineItem addRelationIfValid(String type, String entityType, String entityId) {
        return addRelationIfValid(type, entityType, entityId, (Map) null);
    }

    public ASLineItem addRelationIfValid(String type, String entityType, String entityId, String mustBeValue) {
        if (mustBeValue != null && !mustBeValue.isEmpty()) addRelationIfValid(type, entityType, entityId, (Map) null);
        return this;
    }

    public ASLineItem addDimensions(Map dimensionsMap) {
        super.addDimensions(dimensionsMap, this);
        return this;
    }

    public ASLineItem addDimensions(String... dimensions) {
        super.addDimensions(this, dimensions);
        return this;
    }

    public ASLineItem addDimension(String dimension, String value) {
        if (value != null && !value.isEmpty()) super.addDimension(dimension, value, this);
        return this;
    }

    public ASLineItem addRelationIfValid(String type, String entityType, String entityId, Map<String,Object> relationsProperties) {
        if (!entityType.isEmpty() && entityId != null && !entityId.isEmpty()) {
            Relation newRelation = new Relation(type, new EntityReference(entityType, entityId, this), this);
            if (relationsProperties != null) {
                newRelation.directPut("properties", relationsProperties);
            }
            this.getRelationsManager(true).addRelation(newRelation);
        }
        return this;
    }

    public ASLineItem addRelationIfValid(String type, EntityReference entityRef) {
        if (entityRef != null) {
            this.getRelationsManager(true).addRelation(new Relation(type, entityRef));
        }
        return this;
    }


}
