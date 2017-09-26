package com.activitystream.model.stream;

import com.activitystream.model.ASConstants;
import com.activitystream.model.aspects.*;
import com.activitystream.model.config.ASConfig;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.relations.RelationsManager;
import com.activitystream.model.relations.RelationsType;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import com.activitystream.model.validation.InvalidPropertyContentError;
import com.activitystream.model.validation.MissingPropertyError;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class TransactionEvent extends AbstractBaseEvent implements BaseSubEvent, ProgressElement, EnrichableElement {

    protected static final List<String> redundantProperties =
            Arrays.asList(ASConstants.FIELD_TYPE, ASConstants.FIELD_OCCURRED_AT, ASConstants.FIELD_REGISTERED_AT, ASConstants.FIELD_RECEIVED_AT,
                    ASConstants.FIELD_ORIGIN, ASConstants.FIELD_PARTITION);

    protected static final List<String> extraItemProperties = Arrays.asList(
            ASConstants.FIELD_ITEM_COUNT, ASConstants.FIELD_ITEM_PRICE, ASConstants.FIELD_PRICE_CATEGORY, ASConstants.FIELD_PRICE_TYPE,
            ASConstants.FIELD_ACCOUNTING_KEY, ASConstants.FIELD_DESCRIPTION, ASConstants.FIELD_CURRENCY, ASConstants.FIELD_VARIANT,
            ASConstants.FIELD_PAYMENT_METHOD, ASConstants.FIELD_PAYMENT_METHOD, ASConstants.FIELD_VALID_FROM, ASConstants.FIELD_VALID_UNTIL,
            ASConstants.FIELD_DISCOUNT_PERCENTAGE, ASConstants.FIELD_COMMISSION_FIXED,
            ASConstants.FIELD_COMMISSION_PERCENTAGE, ASConstants.FIELD_TAX_PERCENTAGE, ASConstants.FIELD_PROPERTIES, ASConstants.FIELD_RECEIVED_AT,
            ASConstants.FIELD_REGISTERED_AT
    );

    protected boolean detached = false;

    protected static final Logger logger = LoggerFactory.getLogger(TransactionEvent.class);

    private final static String UNKNOWN_PLACEHOLDER = "unknown";
    private final static String[] BUYER_RELATIONS = (String[]) Arrays.asList("ACTOR", "BUYER").toArray();

    //todo - show the team the surprising "= null trick" :)
    private Relation purchaseRelation;
    private Relation buyerRelation;

    public TransactionEvent() {
        setCurrency(ASConfig.getDefaultCurrency());
    }

    public TransactionEvent(Map map, BaseStreamElement root) {
        super(map, root);
        setCurrency(ASConfig.getDefaultCurrency());
    }

    public TransactionEvent(Map map) {
        super(map, null);
        setCurrency(ASConfig.getDefaultCurrency());
    }

    @Override
    public List<String> getAllowedRelTypes() {
        //if (root != null && root != this) return ASConstants.TRADE_TYPES;
        //else return super.getAllowedRelTypes();
        return super.getAllowedRelTypes();
    }

    @Override
    public EventTypeReference getEventType() {
        return EventTypeReference.resolveTypesString(EventTypeReference.Category.TRANSACTION_EVENT, getType());
    }

    @Override
    public String getType() {
        String type = super.getType();
        if (type == null) {
            type = "as.commerce.transaction." + this.getTransactionType().toLowerCase();
            this.setMessageKey(type);
            this.put(ASConstants.FIELD_TYPE, type);
        }
        return type;

    }

    /************
     * CEP Utility Functions and Getters
     ************/

    static final List<String> PURCHASE_REVERSE_FIELDS =
            Arrays.asList(ASConstants.FIELD_ITEM_COUNT, ASConstants.FIELD_TOTAL_IN_STOCK, ASConstants.FIELD_TOTAL_FOR_SALE);

    static RelationsType REVERSED_PURCHASED = new RelationsType(ASConstants.REL_RETURNED);
    static RelationsType REVERSED_RESERVED = new RelationsType(ASConstants.REL_UN_RESERVED);
    static RelationsType REVERSED_CREATES = new RelationsType(ASConstants.REL_REMOVES);
    static RelationsType REVERSED_RENTED = new RelationsType(ASConstants.REL_RETURNED);
    static List<String> REVERSED_FIELDS = PURCHASE_REVERSE_FIELDS;

    public void reverse() {

        for (Relation relation : this.getRelationsManager()) {

            relation.remove("_rel_path");
            relation.remove("_dir");
            relation.remove("_stream_id");
            relation.remove("_update_at");

            RelationsType newType = null;
            RelationsType oldType = relation.getRelationsType();

            switch (relation.getRelationsType().getRelationsType()) {
                case ASConstants.REL_PURCHASED:
                    newType = REVERSED_PURCHASED;
                    break;
                case ASConstants.REL_RESERVED:
                    newType = REVERSED_RESERVED;
                    break;
                case ASConstants.REL_CREATES:
                    newType = REVERSED_CREATES;
                    break;
                case ASConstants.REL_RENTED:
                    newType = REVERSED_RENTED;
                    break;
                case ASConstants.REL_CARTED:
                    newType = new RelationsType(ASConstants.REL_UN_CARTED);
                    break;
            }

            if (newType != null) {
                Object removed = relation.remove(oldType);
                relation.directPut(newType, removed);
                relation.setRelationsType(newType);
            }
        }

        //Todo - support other types than PURCHASED
        for (String field : REVERSED_FIELDS) {
            if (this.containsKey(field)) {
                Object value = get(field);
                if (value instanceof Double) {
                    this.put(field, ((Double) value) * -1);
                } else if (value instanceof Long) {
                    this.put(field, ((Long) value) * -1);
                } else if (value instanceof Integer) {
                    this.put(field, ((Integer) value) * -1);
                } else {
                    logger.warn("Could not reverse quantity for: " + value + ", its a " + value.getClass().getSimpleName());
                }
            }
        }
    }

    @Override
    public List<String> getExtraItemProperties() {
        return extraItemProperties;
    }

    public DateTime getValidFrom() {
        return (DateTime) get(ASConstants.FIELD_VALID_FROM);
    }

    public void setValidFrom(DateTime validFrom) {
        put(ASConstants.FIELD_VALID_FROM, validFrom);
    }

    public DateTime getValidUntil() {
        return (DateTime) get(ASConstants.FIELD_VALID_UNTIL);
    }

    public void setValidUntil(DateTime validUntil) {
        put(ASConstants.FIELD_VALID_UNTIL, validUntil);
    }

    public String getPriceCategory() {
        return (String) getOrDefault(ASConstants.FIELD_PRICE_CATEGORY, UNKNOWN_PLACEHOLDER);
    }

    public void setPriceCategory(String priceCategory) {
        if (priceCategory != null && !priceCategory.isEmpty()) put(ASConstants.FIELD_PRICE_CATEGORY, priceCategory);
        else remove(ASConstants.FIELD_PRICE_CATEGORY);
    }

    public String getPriceType() {
        return (String) getOrDefault(ASConstants.FIELD_PRICE_TYPE, UNKNOWN_PLACEHOLDER);
    }

    public void setPriceType(String priceType) {
        if (priceType != null || priceType.isEmpty()) put(ASConstants.FIELD_PRICE_TYPE, priceType);
        else remove(ASConstants.FIELD_PRICE_TYPE);
    }

    public String getAccountingKey() {
        return (String) getOrDefault(ASConstants.FIELD_ACCOUNTING_KEY, UNKNOWN_PLACEHOLDER);
    }

    public void setAccountingKey(String accountingKey) {
        if (accountingKey != null || accountingKey.isEmpty()) put(ASConstants.FIELD_ACCOUNTING_KEY, accountingKey);
        else remove(ASConstants.FIELD_ACCOUNTING_KEY);
    }


    public String getDescription() {
        return (String) get(ASConstants.FIELD_DESCRIPTION);
    }

    public String setDescription(String description) {
        return (String) put(ASConstants.FIELD_DESCRIPTION, description);
    }

    public String getCurrency() {
        return (String) getOrDefault(ASConstants.FIELD_CURRENCY, UNKNOWN_PLACEHOLDER);
    }

    public void setCurrency(String currency) {
        if (currency != null && !currency.isEmpty()) put(ASConstants.FIELD_CURRENCY, currency);
        else remove(ASConstants.FIELD_CURRENCY);
    }

    public String getVariant() {
        return (String) get(ASConstants.FIELD_VARIANT);
    }

    public void setVariant(String variant) {
        if (variant != null && !variant.isEmpty()) put(ASConstants.FIELD_VARIANT, variant);
        else remove(ASConstants.FIELD_VARIANT, variant);
    }

    public String getPaymentMethod() {
        return (String) get(ASConstants.FIELD_PAYMENT_METHOD);
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod != null && !paymentMethod.isEmpty()) put(ASConstants.FIELD_PAYMENT_METHOD, paymentMethod);
        else remove(ASConstants.FIELD_PAYMENT_METHOD);
    }

    public String getCardToken() {
        return (String) get(ASConstants.FIELD_CARD_TOKEN);
    }

    public void setCardToken(String cardToken) {
        if (cardToken != null && !cardToken.isEmpty()) put(ASConstants.FIELD_CARD_TOKEN, cardToken);
        else remove(ASConstants.FIELD_CARD_TOKEN);
    }

    public Set<String> getLineIds() {
        return (Set<String>) getOrDefault(ASConstants.FIELD_LINE_IDS, new HashSet<String>());
    }

    public void setLineIds(String ids) {
        if (ids == null || ids.isEmpty()) {
            remove(ASConstants.FIELD_LINE_IDS);
            return;
        }

        HashSet<String> lineIds = new HashSet<>();
        lineIds.addAll(Arrays.asList(ids.split(",")));
        setLineIds(lineIds);
    }

    public void setLineIds(Set<String> ids) {
        if (ids != null && ids.isEmpty()) put(ASConstants.FIELD_LINE_IDS, ids);
        else remove(ASConstants.FIELD_LINE_IDS);
    }

    public void addToLineIds(Set<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            Set<String> lineIds = getLineIds();
            lineIds.addAll(ids);
            setLineIds(lineIds);
        }
    }

    public void appendLineId(String id) {
        if (id != null && !id.isEmpty()) {
            Set<String> lineIds = getLineIds();
            lineIds.add(id);
            super.put(ASConstants.FIELD_LINE_IDS, lineIds);
        }
    }

    /**
     * Returns the first Entity Ref String resolved for a given role
     *
     * @param roles
     * @return
     */
    public String getEntityRefStringForRole(String... roles) {
        EntityReference primaryEntityReference = getPrimaryEntityReferenceForRole(roles);
        if (primaryEntityReference != null) return primaryEntityReference.getEntityReference();
        else return null;
    }

    /**
     * Returns the first Entity Reference resolved for a given role
     *
     * @param roles
     * @return
     */
    public EntityReference getPrimaryEntityReferenceForRole(String... roles) {
        Relation primaryRelationForRole = getPrimaryRelationForRole(roles);
        if (primaryRelationForRole != null) return primaryRelationForRole.getRelatedBusinessEntity().getEntityReference();
        else return null;
    }

    /**
     * Returns the first Entity Relation resolved for a given role
     *
     * @param roles
     * @return
     */
    public Relation getPrimaryRelationForRole(String... roles) {
        Relation roleRelation = getRelationsManager().getFirstRelationsOfType(roles);
        if (roleRelation == null && getParentEvent() != null) roleRelation = getParentEvent().getRelationsManager().getFirstRelationsOfType(roles);
        //todo - select primary relation based on something more elaborate than order (or warn when multiple)?
        return roleRelation;
    }

    public AspectInterface getEventAspectByType(String aspectType) {
        AspectInterface aspect = getAspectManager().getAspectByType(aspectType);
        if (aspect == null && getParentEvent() != null) aspect = getParentEvent().getAspectManager().getAspectByType(aspectType);
        return aspect;
    }

    public List<Relation> getRelationsForRole(String... roles) {
        List<Relation> relations = getRelationsManager().getRelationsOfType(roles);
        if (getParentEvent() != null) relations.addAll(getParentEvent().getRelationsManager().getRelationsOfType(roles));
        return relations;
    }

    public String getSuppliedBy() {
        return getEntityRefStringForRole("MANAGED_BY");
    }

    public String getOrganizer() {
        return getEntityRefStringForRole("SUPPLIED_BY");
    }

    public String getCustomer() {
        return getEntityRefStringForRole("ACTOR:BUYER", "BUYER");
    }

    public String getSeller() {
        return getEntityRefStringForRole("SOLD_BY");
    }

    public String getLocatedAt() {
        return getEntityRefStringForRole("LOCATED_AT");
    }

    public String getProduct() {
        return getEntityRefStringForRole((String[]) ASConstants.TRADE_TYPES.toArray());
    }

    public BusinessEntity getProductEntity() {
        for (Relation relation : getRelationsManager()) {
            if (ASConstants.PRODUCT_RELATIONS.contains(relation.getRelationsType().getRelationsType())) return (BusinessEntity) relation.getRelatedItem();
        }
        return null;
    }

    public Relation getTransactionRelation() {
        //return getPrimaryRelationForRole((String[]) ASConstants.PRODUCT_RELATIONS.toArray());
        for (Relation relation : getRelationsManager()) {
            if (ASConstants.PRODUCT_RELATIONS.contains(relation.getRelationsType().getRelationsType())) return relation;
        }
        return null;
    }

    public String getOrderId() {
        EntityReference orderReference = getOrderReference();
        return (orderReference != null) ? getOrderReference().getEntityReference() : null;
    }

    //todo - make this an explicit role
    public EntityReference getOrderReference() {
        List<Relation> relations = getRelationsForRole("INVOLVES", "AFFECTS");
        for (Relation relation : relations) {
            BusinessEntity ent = relation.getRelatedBusinessEntity();
            if (ent.getEntityReference().getEntityReference().toLowerCase().startsWith("order")) return ent.getEntityReference();
        }
        return null;
    }


    public String getClientIP() {
        LinkedHashMap res = (LinkedHashMap) get("_client_ip");
        if (res == null) return null;
        return (String) res.get("ip");
    }

    public Double getClientLatitude() {
        LinkedHashMap res = (LinkedHashMap) get("_client_ip");
        if (res == null) return null;
        return (Double) res.get("_latitude");
    }

    public Double getClientLongitude() {
        LinkedHashMap res = (LinkedHashMap) get("_client_ip");
        if (res == null) return null;
        return (Double) res.get("_longitude");
    }

    public RelationsManager getSerialNumbers() {
        return (RelationsManager) get(ASConstants.FIELD_SERIAL_NUMBERS);
    }

    public RelationsManager getInvolves() {
        return (RelationsManager) get(ASConstants.FIELD_INVOLVES);
    }

    public Double getItemCount() {
        return (Double) getOrDefault(ASConstants.FIELD_ITEM_COUNT, 0d);
    }

    public void setItemCount(Double count) {
        if (count != null) put(ASConstants.FIELD_ITEM_COUNT, count);
        else remove(ASConstants.FIELD_ITEM_COUNT);
    }

    public void addItemCount(Double count) {
        if (count != null) put(ASConstants.FIELD_ITEM_COUNT, count);
        else remove(ASConstants.FIELD_ITEM_COUNT);
    }

    public Double getItemPrice() {
        return (Double) getOrDefault(ASConstants.FIELD_ITEM_PRICE, 0d);
    }

    public void setItemPrice(String itemPrice) {
        put(ASConstants.FIELD_ITEM_PRICE, Double.parseDouble(itemPrice));
    }

    public void setItemPrice(Number price) {
        if (price != null) put(ASConstants.FIELD_ITEM_PRICE, price.doubleValue());
        else remove(ASConstants.FIELD_ITEM_PRICE);
    }

    public String getItemName() {
        return this.getAspectManager().getPresentation().getLabel();
    }

    public DateTime getItemTimedBegins() {
        return this.getAspectManager().getTimed().getBegins();
    }

    double getCommissionFixed() {
        return (double) getOrDefault(ASConstants.FIELD_COMMISSION_FIXED, 0d);
    }

    public void setCommissionFixed(Double commission) {
        if (commission != null) put(ASConstants.FIELD_COMMISSION_FIXED, commission);
        else remove(ASConstants.FIELD_COMMISSION_FIXED);
    }

    public void setCommissionFixed(String commission) {
        if (commission != null && !commission.isEmpty()) {
            try {
                setCommissionFixed(NumberFormat.getInstance().parse(commission).doubleValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    boolean getComplimentary() {
        return (boolean) getOrDefault(ASConstants.FIELD_COMPLIMENTARY, false);
    }

    public void setComplimentary(Boolean complimentary) {
        if (complimentary != null && complimentary) {
            put(ASConstants.FIELD_COMPLIMENTARY, complimentary);
            if (complimentary) setItemPrice(0);
        }
        else remove(ASConstants.FIELD_COMPLIMENTARY);
    }

    double getCommissionPercentage() {
        return (double) getOrDefault(ASConstants.FIELD_COMMISSION_PERCENTAGE, 0d);
    }

    public void setCommissionPercentage(String commission) {
        if (commission != null && !commission.isEmpty()) {
            try {
                setCommissionPercentage(NumberFormat.getInstance().parse(commission).doubleValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCommissionPercentage(Double commission) {
        if (commission != null) put(ASConstants.FIELD_COMMISSION_PERCENTAGE, commission);
        else remove(ASConstants.FIELD_COMMISSION_PERCENTAGE);
    }

    double getDiscountPercentage() {
        return (double) getOrDefault(ASConstants.FIELD_DISCOUNT_PERCENTAGE, 0d);
    }

    double getTaxPercentage() {
        return (double) getOrDefault(ASConstants.FIELD_TAX_PERCENTAGE, 0d);
    }

    public void setTaxPercentage(Double taxPercentage) {
        if (taxPercentage != null) put(ASConstants.FIELD_TAX_PERCENTAGE, taxPercentage);
        else remove(ASConstants.FIELD_TAX_PERCENTAGE);
    }

    public Double getTotalInStock() {
        return (Double) get(ASConstants.FIELD_TOTAL_IN_STOCK);
    }

    public Double getTotalForSale() {
        return (Double) get(ASConstants.FIELD_ITEM_COUNT) * (Double) get(ASConstants.FIELD_ITEM_PRICE);
    }

    //todo - find something to do with this
    public Map<String, String> getDimensions() {
        return getAspectManager().getDimensions();
    }



    /************  Progress ************/

    @Override
    public Map progressSummaryMap() {
        throw new NotImplementedException();
        /*
        todo - reimplement elsewhere
        Map progressSummary = WorkflowManager.createProgressSummary((OrientVertex) this.rootElement);
        if (progressSummary != null) super.put("_progress", progressSummary);
        return progressSummary;
        */
    }

    @Override
    public Map createProgressMap() {
        throw new NotImplementedException();
        /*
        todo - reimplement elsewhere
        Map progressSummary = WorkflowManager.createProgressSummary((OrientVertex) this.rootElement, true);
        if (progressSummary != null) super.put("_progress", progressSummary);
        return progressSummary;
        */
    }


    /************  Enrichment Function ************/

    @Override
    public void simplify() {
        super.simplify();
        remove(ASConstants.FIELD_SERIAL_NUMBERS);
    }

    /************  Utility functions ************/

    @Override
    public Map<String, Object> getEventSummaryMap() {
        Map<String, Object> result = super.getEventSummaryMap();
        result.put(ASConstants.FIELD_ITEM_COUNT, getItemCount());
        result.put(ASConstants.FIELD_ITEM_PRICE, getItemPrice());
        result.put(ASConstants.FIELD_PRICE_CATEGORY, getPriceCategory());
        return result;
    }

    /************  Access ************/

    public RelationsManager getRelationsManager() {
        return (RelationsManager) get(ASConstants.FIELD_INVOLVES);
    }

    public String getTransactionType() {
        return (this.getPurchaseRelation() != null) ? this.getPurchaseRelation().getRelationsType().getRelationsType() : null;
    }

    public void setTransactionType(String transactionType) {
        if (transactionType != null && !transactionType.isEmpty()) put(ASConstants.FIELD_TRANSACTION_TYPE, transactionType);
        else remove(ASConstants.FIELD_TRANSACTION_TYPE);
    }


    @Override
    public Object put(Object key, Object value) {
        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_RECEIVED_AT:
            case ASConstants.FIELD_REGISTERED_AT:
            case ASConstants.FIELD_OCCURRED_AT:
                //todo - configure better nanosecond support
                value = validator().processIsoDateTime(theKey, value, true);
                break;
            case "source":
                theKey = ASConstants.FIELD_ORIGIN;
            case ASConstants.FIELD_ORIGIN:
                value = validator().processLowerCaseString(theKey, value, true);
                break;
            case ASConstants.FIELD_STREAM_ID_INTERNAL:
                theKey = ASConstants.FIELD_STREAM_ID;
                value = validator().processString(theKey, value, false);
                break;
            case "associates":
            case ASConstants.FIELD_RELATIONS:
                theKey = ASConstants.FIELD_INVOLVES;
            case ASConstants.FIELD_INVOLVES:
                value = new RelationsManager(value, null, this);
                break;
            case "event":
                theKey = ASConstants.FIELD_TYPE;
            case ASConstants.FIELD_TYPE:
                value = validator().processLowerCaseString(theKey, value, true);
                break;
            case "price_type_description":
                theKey = ASConstants.FIELD_PRICE_TYPE;
            case ASConstants.FIELD_PRICE_TYPE:
            case ASConstants.FIELD_PRICE_CATEGORY:
            case ASConstants.FIELD_ACCOUNTING_KEY:
            case ASConstants.FIELD_DESCRIPTION:
            case ASConstants.FIELD_CURRENCY:
            case ASConstants.FIELD_VARIANT:
            case ASConstants.FIELD_PAYMENT_METHOD:
            case ASConstants.FIELD_CARD_TOKEN:
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_SERIAL_NUMBERS:
                value = new RelationsManager(ASConstants.REL_SERIAL, (List) value, null, this.root);
                break;
            case ASConstants.FIELD_LINE_IDS:
                value = validator().processSimpleValueList(theKey, value, false);
                break;
            case ASConstants.FIELD_COMPLIMENTARY:
            case "complementary":
            case "is_comp":
                theKey = ASConstants.FIELD_COMPLIMENTARY;
                value = validator().processBoolean(theKey, value, false);
                break;
            case ASConstants.FIELD_ITEM_COUNT:
            case ASConstants.FIELD_ITEM_PRICE:
            case ASConstants.FIELD_COMMISSION_FIXED:
            case ASConstants.FIELD_COMMISSION_PERCENTAGE:
            case ASConstants.FIELD_DISCOUNT_PERCENTAGE:
            case ASConstants.FIELD_TAX_PERCENTAGE:
            case ASConstants.FIELD_TOTAL_IN_STOCK:
            case ASConstants.FIELD_TOTAL_FOR_SALE:
                value = validator().processDouble(theKey, value, false, null, null);
                break;
            case ASConstants.FIELD_VALID_FROM:
            case ASConstants.FIELD_VALID_UNTIL:
                value = validator().processIsoDateTime(theKey, value, false);
                break;
            case ASConstants.ASPECTS_DIMENSIONS:
                return getAspectManager(true).put(ASConstants.ASPECTS_DIMENSIONS, value);
            case ASConstants.ASPECTS_CLIENT_IP:
                value = new ClientIpAspect(value, root);
                break;
            case ASConstants.FIELD_PROPERTIES:
                value = validator().processMap(theKey, value, false);
                break;
            case ASConstants.FIELD_STREAM_ID:
                if (value instanceof String)
                    value = UUID.fromString((String) value);
                if (!(value instanceof UUID)) {
                    addProblem(new InvalidPropertyContentError("Stream ID must be a UUID."));
                }
                break;
            case ASConstants.FIELD_ASPECTS:
                if (value instanceof Map) value = new AspectManager((Map) value, this);
                else this.addProblem(new IgnoredPropertyError("Aspects can only be set using a map value"));
                break;
            default:
                String keyPart = ((String) key).split(":")[0];
                if (ASConstants.TRADE_TYPES.contains(keyPart)) {
                    theKey = (String) key; //keep the original key for purchase relations
                    if (this.purchaseRelation != null) {
                        this.addProblem(new IgnoredPropertyError("Item can not have multiple purchase actions " + key));
                        return null;
                    }
                    Relation pRelation = new Relation(theKey, value, this);
                    setPurchaseRelation(pRelation);
                    super.put(theKey, pRelation.getRelatedItem());
                } else if (!theKey.startsWith("_")) {
                    logger.error("The " + theKey + " property is not supported for the Item Aspect");
                    this.addProblem(new IgnoredPropertyError("The '" + theKey + "' property is not supported for the Item Aspect"));
                    return null;
                }
        }
        return super.put(theKey, value);
    }

    public BusinessEntity getPurchaser() {

        BusinessEntity purchaser = null;

        Relation purchaserRelation = getPrimaryRelationForRole("ACTOR", "BUYER");
        purchaser = purchaserRelation.getRelatedBusinessEntity();

        //todo - check for proxy on purchase
        boolean foundProxy = false;
        for (Relation pRel : getRelationsForRole("PROXY_FOR")) {
            purchaser = (BusinessEntity) pRel.getRelatedItem();
            foundProxy = true;
            break;
        }
        /*
        if (!foundProxy && purchaser.getRootElement() != null) {
            for (Edge edge : ((OrientVertex) purchaser.getRootElement()).getEdges(Direction.OUT, "PROXY_FOR")) {
                purchaser = new BusinessEntity(edge.getVertex(Direction.IN), null);
                logger.error("Found persisted proxy for buyer that was not included in the message");
                break;
            }
        }
        */
        return purchaser;
    }


    public Relation getBuyerRelation() {

        if (this.buyerRelation == null && this.getRelationsManager() != null) {
            this.buyerRelation = this.getRelationsManager().getFirstRelationsOfType(BUYER_RELATIONS);
        }
        if (this.buyerRelation == null) {
            logger.warn("this: " + this);
        }
        return this.buyerRelation;
    }

    public String getProductGroup() {
        BusinessEntity product = (BusinessEntity) getPurchaseRelation().getRelatedItem();
        Relation groupRelation = product.getFirstRelationsOfType("PART_OF");
        return (groupRelation != null) ? groupRelation.getRelatedBusinessEntity().getEntityReference().getEntityReference() : null;
    }

    public Relation getPurchaseRelation() {
        if (this.purchaseRelation == null && this.getRelationsManager() != null) {
            this.purchaseRelation = this.getRelationsManager().getFirstRelationsOfType(ASConstants.TRADE_TYPES.toArray(new String[0]));
        }
        if (this.purchaseRelation == null) {
            logger.warn("this: " + this);
        }
        return this.purchaseRelation;
    }

    public void setPurchaseRelation(Relation purchaseRelation) {
        this.purchaseRelation = purchaseRelation;
    }


    /************ Persistence ************/

    @Override
    public void verify() {
        //super.verify();
        //todo - impliment verification for transactions

        //Check if item count matches expected range
        String transactionType = getTransactionType();
        if (transactionType == null) {
            addProblem(new MissingPropertyError("Transaction type can not be null"));
            return;
        }

        switch (transactionType) {
            case ASConstants.REL_PURCHASED:
            case ASConstants.REL_RENTED:
            case ASConstants.REL_GOT:
            case ASConstants.REL_LEASED:
            case ASConstants.REL_WON:
                if (getItemCount() < 0) {
                    logger.debug("Forcing item count to be positive for " + transactionType + " item line");
                    setItemCount(getItemCount() * -1); //Force purchased items to be positive
                }
                break;
            case ASConstants.REL_RETURNED:
            case ASConstants.REL_UN_CARTED:
            case ASConstants.REL_UN_RESERVED:
                if (getItemCount() > 0) {
                    logger.debug("Forcing item count to be negative for " + transactionType + " item line");
                    setItemCount(getItemCount() * -1); //Force returned items to be negative
                }
                break;
            case ASConstants.REL_RESOLD:
            case ASConstants.REL_REPURCHASED: //Do nothing (Change of ownership only)
                break;
        }

    }

    @Override
    public boolean traverse(ElementVisitor visitor) {
        if (!super.traverse(visitor))
            return false;

        if (this.purchaseRelation != null) {
            if (!this.purchaseRelation.traverse(visitor))
                return false;
        }

        return true;
    }

    @Override
    public String getFootprint(DateTime occurredAt, String granularity) {
        String footprint = super.getFootprint(occurredAt, granularity);
        if (footprint != null) {
            if (get(ASConstants.FIELD_PRICE_CATEGORY) != null) footprint += "--price_category-" + get(ASConstants.FIELD_PRICE_CATEGORY);
            if (get(ASConstants.FIELD_ITEM_COUNT) != null)
                footprint += "--total-" + get(ASConstants.FIELD_ITEM_COUNT) + "@" + getOrDefault(ASConstants.FIELD_ITEM_PRICE, "0");

            CustomerEvent parentEvent = getParentEvent();
            if (parentEvent == null)
                return null;
            footprint += "--parent-" + parentEvent.getStreamId();

            // If footprints of sub-events are not unique, add a line number
            Map<String, AbstractBaseEvent> duplicateGuard = parentEvent.getFootprintDuplicateGuard();
            if (duplicateGuard.getOrDefault(footprint, this) != this) {
                String candidate;
                int line = 1;

                do {
                    line++;
                    candidate = footprint + "--line-" + line;
                } while (duplicateGuard.getOrDefault(candidate, this) != this);

                logger.info("Found sub-event with identical footprint '{}', changing to '{}'", footprint, candidate);
                footprint = candidate;
            }

            duplicateGuard.put(footprint, this);
        }
        return footprint;
    }

    @Override
    public CustomerEvent getParentEvent() {
        BaseStreamItem parent = getParentStreamItem();
        return parent instanceof CustomerEvent ? (CustomerEvent) parent : null;
    }

    private void cleanStreamId() {
        this.remove(ASConstants.FIELD_STREAM_ID);
    }


    @Override
    public void detach() {
        //copy data to sub-event
        if (detached) return;

        CustomerEvent parentEvent = getParentEvent();

        //parent relations
        if (parentEvent != null) {
            this.setMessageKey("as.commerce.transaction." + this.getTransactionType().toLowerCase());
            this.put(ASConstants.FIELD_TYPE, getMessageKey());
            this.put(ASConstants.FIELD_OCCURRED_AT, parentEvent.getOccurredAt());
            this.put(ASConstants.FIELD_ORIGIN, parentEvent.getOrigin());

            if (parentEvent.getAspectManager().getClientIp() != null) {
                this.put("_" + ASConstants.ASPECTS_CLIENT_IP, parentEvent.getAspectManager().getClientIp());
            }

            if (parentEvent.getAspectManager().getClientDevice() != null) {
                this.put("_" + ASConstants.ASPECTS_CLIENT_DEVICE, parentEvent.getAspectManager().getClientDevice());
            }

        }

        //Purchase relations
        //logger.warn("3 " +  this.getPurchaseRelation());
        Relation purchaseRelation = this.getPurchaseRelation();
        if (purchaseRelation != null && !this.getRelationsManager().contains(purchaseRelation)) {
            //logger.warn("2 " +  this.getPurchaseRelation());
            this.getRelationsManager().add(0, this.getPurchaseRelation());
            String pType = getPurchaseRelation().getRelationsType().getRelationsType();
            if (containsKey(pType)) this.remove(pType);
            if (containsKey("TRADE:" + pType)) this.remove("TRADE:" + pType);
        }

        //Serial numbers
        RelationsManager serialManager = (RelationsManager) get(ASConstants.FIELD_SERIAL_NUMBERS);
        if (serialManager != null) {
            serialManager.forEach(relation -> this.getRelationsManager().add(relation));
        }

        remove(ASConstants.FIELD_SERIAL_NUMBERS);
        cleanStreamId();

        detached = true;

        this.verify();
    }

    public void addParentRelations() {
        if (this.getParentEvent() != null) {
            this.getRelationsManager().addAll(this.getParentEvent().getRelationsManager());
        }
    }

    @Override
    public void reattach(CustomerEvent parentEvent) {
        if (parentEvent == null) return;
        parentEvent.traversal().forEachEntityRelation(relation -> this.getRelationsManager().removeIdenticalRelations(relation));
    }

    @Override
    public void prepareForFederation() {

    }

    public boolean equals(TransactionEvent transactionLine) {
        return
                isSame(transactionLine.getTransactionType(),this.getTransactionType()) &&
                isSame(transactionLine.getProduct(),this.getProduct()) &&
                isSame(transactionLine.getVariant(),this.getVariant()) &&
                isSame(transactionLine.getPriceType(),this.getPriceType()) &&
                isSame(transactionLine.getPriceCategory(),this.getPriceCategory()) &&
                isSame(transactionLine.getItemPrice(),this.getItemPrice()) &&
                isSame(transactionLine.getComplimentary(),this.getComplimentary()) &&
                isSame(transactionLine.getCommissionFixed(),this.getCommissionFixed()) &&
                isSame(transactionLine.getCommissionPercentage(),this.getCommissionPercentage()) &&
                isSame(transactionLine.getDescription(),this.getDescription()) &&
                isSame(transactionLine.getTaxPercentage(),this.getTaxPercentage()) &&
                isSame(transactionLine.getValidFrom(),this.getValidFrom()) &&
                isSame(transactionLine.getValidUntil(),this.getValidUntil()) &&
                isSame(transactionLine.getDimensions(),this.getDimensions());
    }

    private boolean isSame(Object one, Object other) {
        if (one == null && other != null) return false;
        else if (one == null && other == null) return true;
        return one.equals(other);
    }

}
