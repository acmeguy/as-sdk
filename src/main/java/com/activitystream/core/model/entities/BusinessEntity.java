package com.activitystream.core.model.entities;

import com.activitystream.core.model.stream.AbstractStreamItem;
import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.aspects.*;
import com.activitystream.core.model.core.AbstractMapElement;
import com.activitystream.core.model.interfaces.*;
import com.activitystream.core.model.relations.Relation;
import com.activitystream.core.model.relations.RelationsManager;
import com.activitystream.core.model.security.ProcessSettings;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.validation.InvalidPropertyContentError;
import com.activitystream.core.model.validation.MissingPropertyError;
import com.google.common.collect.ImmutableSet;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.activitystream.sdk.ASConstants.*;

public class BusinessEntity extends AbstractStreamItem
        implements BaseEntity, HasAspects, BaseStreamItem, EnrichableElement, ManagedRelationsElement {

    public final static List<String> EXPANDED_RELATIONS = Arrays.asList("OUT:CLOSE", "BOTH:IS", "OUT:HOSTED_AT", "OUT:LOCATED_AT", "IN:POWERED_BY");

    private static final ProcessSettings ENTITY_PROCESS_SETTINGS = new ProcessSettings() {{
        active = true;
        enrich = true;
        store = true;
        statistics = true;
    }};

    private static final ProcessSettings ENTITY_SNAPSHOT_PROCESS_SETTINGS = new ProcessSettings() {{
        active = true;
        enrich = true;
        store = true;
        statistics = true;
        broadcast = true;
        analyse = true;
    }};

    private static final List<String> nativeProperties = Arrays.asList(ASConstants.FIELD_PROPERTIES);
    private static final Set<String> managedProperties =
            ImmutableSet.of(ASConstants.FIELD_OCCURRED_AT, FIELD_TYPE, ASConstants.FIELD_ASPECTS, ASConstants.FIELD_INVOLVES, ASConstants.FIELD_RELATIONS,
                    ASConstants.FIELD_ENTITY_REF, ASConstants.FIELD_ACL, ASConstants.FIELD_PARTITION);

    private static final Logger logger = LoggerFactory.getLogger(BusinessEntity.class);

    private String messageKey = null;

    private boolean redundant = false;
    private boolean unSavable = false;

    static List<String> entityAllowedRelTypes = null;
    private List<EntityChangeMap> changes = new LinkedList<>();
    private Boolean requireIndexUpdate = false;

    final List<String> SNAPSHOT_TRIGGERS = Arrays.asList("dimensions", "metrics", "inventory");

    public BusinessEntity() {
        super();
    }

    public BusinessEntity(Map map) {
        super(map, null);
    }

    public BusinessEntity(Map map, BaseStreamElement root) {
        super(map, root);
    }

    public BusinessEntity(String value) {
        this(value, null);
    }

    public BusinessEntity(String value, BaseStreamElement root) {
        setRoot(root);
        put(ASConstants.FIELD_ENTITY_REF, new EntityReference(value, this));
    }

    public BusinessEntity(Object value, BaseStreamElement root) {
        setRoot(root);
        if (value instanceof Map) {
            super.setMapValues((Map<Object, Object>) value);
        } else if (value instanceof String) {
            put(ASConstants.FIELD_ENTITY_REF, new EntityReference((String) value));
        } else if (value instanceof EntityReference) {
            put(ASConstants.FIELD_ENTITY_REF, value);
        } else {
            logger.error("BusinessEntity - WTF : " + value);
        }
    }

    public RelationsManager getRelationsManager(boolean create) {
        if (get(getRelationsRootType()) == null && create) {
            super.put(getRelationsRootType(), new RelationsManager(null, this));
        }
        return (RelationsManager) get(getRelationsRootType());
    }

    public RelationsManager getRelationsManager() {
        return getRelationsManager(false);
    }

    public boolean hasRelations() {
        return containsKey(getRelationsRootType());
    }

    private Object getAspectField(String aspectName, String fieldName) {
        AspectManager aspectManager = getAspectManager();
        if (aspectManager == null) {
            return null;
        }

        Map aspect = (Map) aspectManager.get(aspectName);

        if (aspect == null) {
            return null;
        }

        if (aspect.containsKey(fieldName)) {
            return aspect.get(fieldName);
        }

        return null;
    }


    private String getEntityReferenceByRelationType(String relationType) {
        BusinessEntity be = getEntityByRelationType(relationType);
        if (be == null) {
            return null;
        }

        return be.getEntityReferenceFromEntity();
    }

    private BusinessEntity getEntityByRelationType(String relationType) {
        if (!hasRelations())
            return null;
        Relation relation = getRelationsManager().getFirstRelationsOfType(relationType);
        if (relation != null && relation.isRelatedItemABusinessEntity())
            return relation.getRelatedBusinessEntity();
        return null;
    }

    private String getEntityReferenceFromEntity() {
        return getEntityReference().getEntityReference();
    }

    /************  Utility functions  ************/

    @Override
    public List<String> getAllowedRelTypes() {
        if (entityAllowedRelTypes == null) {
            entityAllowedRelTypes = new LinkedList<>();
            entityAllowedRelTypes.addAll(ASConstants.ENTITY_RELATIONS);
            entityAllowedRelTypes.addAll(ASConstants.INTEREST_TYPES);
        }
        return entityAllowedRelTypes;
    }

    public String getRelationsRootType() {
        return ASConstants.FIELD_RELATIONS;
    }

    public boolean hasEntityReference() {
        return containsKey(ASConstants.FIELD_ENTITY_REF);
    }

    public EntityReference getEntityReference() {
        return (EntityReference) get(ASConstants.FIELD_ENTITY_REF);
    }

    @Override
    public void simplify() {
        if (hasAspects()) {
            getAspectManager().simplify();
            if (getAspectManager().isEmpty())
                remove(ASConstants.FIELD_ASPECTS);
        }
        if (hasRelations()) {
            getRelationsManager().simplify();
        }

        remove("_stream_id");
        remove("_update_at");
    }

    @Override
    public String getElementType() {
        return this.getEntityReference().getEntityTypeString();
    }

    public Object addLabel(Object label) {
        PresentationAspect presentation = getAspectManager(true).getOrCreatePresentationAspect();
        presentation.put("label", label);
        return presentation;
    }

    public Object addPatch(Object patch) {
        return patch;
    }

    public BusinessEntity withRelation(String type, Object value) {
        return withRelation(type, value, false);
    }

    public BusinessEntity withReverseRelation(String type, Object value) {
        return withRelation(type, value, true);
    }

    private BusinessEntity withRelation(String type, Object value, boolean reverse) {
        Relation relation = new Relation(type, value);
        if (reverse) relation.reverse();
        this.getRelationsManager(true).addRelation(relation);
        return this;
    }

    public BusinessEntity withRelationIfValid(String type, String entityType, String entityId) {
        return withRelationIfValid(type, entityType, entityId, null, false);
    }

    public BusinessEntity withReverseRelationIfValid(String type, String entityType, String entityId) {
        return withRelationIfValid(type, entityType, entityId, null, true);
    }

    public BusinessEntity withRelationIfValid(String type, String entityType, String entityId, Map<String,Object> relationsProperties) {
        return withRelationIfValid(type, entityType, entityId, relationsProperties, false);
    }

    public BusinessEntity withReverseRelationIfValid(String type, String entityType, String entityId, Map<String,Object> relationsProperties) {
        return withRelationIfValid(type, entityType, entityId, relationsProperties, true);
    }

    private BusinessEntity withRelationIfValid(String type, String entityType, String entityId, Map<String,Object> relationsProperties, Boolean reverse) {
        if (!entityType.isEmpty() && entityId != null && !entityId.isEmpty()) {
            Relation newRelation = new Relation(type, new EntityReference(entityType, entityId, this), this);
            if (reverse) newRelation.reverse();
            if (relationsProperties != null) {
                newRelation.directPut("properties", relationsProperties);
            }
            this.getRelationsManager(true).addRelation(newRelation);
        }
        return this;
    }

    public BusinessEntity withRelationIfValid(String type, EntityReference entityRef) {
        return withRelationIfValid(type, entityRef, false);
    }

    public BusinessEntity withReverseRelationIfValid(String type, EntityReference entityRef) {
        return withRelationIfValid(type, entityRef, true);
    }

    private BusinessEntity withRelationIfValid(String type, EntityReference entityRef, boolean reverse) {
        if (entityRef != null) {
            Relation rel = new Relation(type, entityRef);
            if (reverse) rel.reverse();
            this.getRelationsManager(true).addRelation(rel);
        }
        return this;
    }

    public Object withRelations(String type, Object value) {
        RelationsManager relationsManager = (RelationsManager) get("relations");
        if (relationsManager == null) relationsManager = new RelationsManager(ASConstants.ENTITY_RELATIONS, this);
        if (value instanceof List) {
            for (Object singleRelation : ((List) value)) {
                relationsManager.add(new Relation(type, singleRelation, this));
            }
        } else {
            relationsManager.add(new Relation(type, value, this));
        }
        return put("relations", relationsManager);
    }

    /**
     * The Partition is a structure to partition information for different sub-tenants
     * The "_common" partition is seen by all.
     * Each subtenant has it's own, dedicated partition and this should not be specified unless you know what you are doing :)
     * @return
     */
    @Override
    public String getPartition() {
        if (get(ASConstants.FIELD_PARTITION) != null)
            return (String) get(ASConstants.FIELD_PARTITION);
        BaseStreamItem parent = getParentStreamItem();
        return parent != null ? parent.getPartition() : null;
    }

    /**
     * Sets the access partition for the Entity
     * _common is the common partition and each tenant/sub-tenant has his primary partition
     * Partitions are used to fragment data belonging to different sub-tenants under one tenant
     * @param partition
     */
    public void setPartition(String partition) {
        put(ASConstants.FIELD_PARTITION, partition);
    }

    /**
     * A Stream ID is a "Named UUID" calculated from the Entity Reference.
     * The Entity Reference consists of two parts <Entity Type>/<Entity ID> like: "Customer/398398"
     * The Stream id is globally unique.
     */
    @Override
    public UUID getStreamId() {
        return getEntityReference().getEntityStreamId();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BusinessEntity) {
            return this.getEntityReference().equals(((BusinessEntity) o).getEntityReference());
        }
        return super.equals(o);
    }

    public Relation getFirstRelationsOfType(String... type) {

        if (this.getRelationsManager() != null) {
            Relation relation = this.getRelationsManager().getFirstRelationsOfType(type);
            if (relation != null)
                return relation;
        }

        return null;
    }

    public List<Relation> getRelationsOfType(String... type) {
        return (this.getRelationsManager() != null) ? this.getRelationsManager().getRelationsOfType(type) : null;
    }

    /************  Access ************/

    /**
     * @return Returns the date that the Entity was initially received by AS
     */
    @Override
    public DateTime getReceivedAt() {
        return (DateTime) getOrDefault(ASConstants.FIELD_RECEIVED_AT, get("_received_at"));
    }

    /**
     * @return Returns the date that the Entity was registered
     */
    public DateTime getRegisteredAt() {
        return getStatusAspect().getRegisteredAt();
    }

    @Override
    public DateTime getOccurredAt() {
        return getStatusAspect().getUpdateOccurredAt();
    }

    @Override
    public String getMessageKey() {
        return this.messageKey;
    }

    /************  Processing ************/

    @Override
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public ProcessSettings getProcessSettings() {
        return getOccurredAt() != null ? ENTITY_SNAPSHOT_PROCESS_SETTINGS : ENTITY_PROCESS_SETTINGS;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {
        if (!containsKey(ASConstants.FIELD_ENTITY_REF))
            addProblem(new MissingPropertyError("Field '" + ASConstants.FIELD_ENTITY_REF + "' is required."));

        else if ( !((EntityReference) get(ASConstants.FIELD_ENTITY_REF)).isValid())
            addProblem(new MissingPropertyError("Field '" + ASConstants.FIELD_ENTITY_REF + "' is not valid."));

    }

    public Object directPut(Object key, Object value) {
        return super.put(key, value);
    }

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();

        if (!theKey.equals(theLCKey)) {
            addProblem(new AdjustedPropertyWarning("Property name '" + theKey + "' converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case FIELD_TYPE:
                return null; //ignore message key for entities (common when sent via message queue)
            case ASConstants.FIELD_REGISTERED_AT:
                return null;
            case ASConstants.FIELD_OCCURRED_AT:
                break;
            case "_received_at":
                value = validator().processIsoDateTime(theKey, value, true);
                break;
            case ASConstants.FIELD_PARTITION:
                value = validator().processLowerCaseString(theKey, value, false);
                break;
            case ASConstants.FIELD_ENTITY_REF:
                value = validator().processEntityReference(theKey, value);
                break;
            case "label":
                if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) return null;
                this.addLabel(value);
                return null;
            case ASConstants.FIELD_RELATIONS:
                if (value == null) {
                    addProblem(new InvalidPropertyContentError("Relations can not be populated with a null value"));
                    return null;
                }
                RelationsManager relationManager = getRelationsManager();
                if (relationManager == null) {
                    value = new RelationsManager(value, getAllowedRelTypes(), this);
                } else {
                    relationManager.addAll(new RelationsManager(value, getAllowedRelTypes(), this));
                    value = relationManager;
                }
                break;
            case ASConstants.FIELD_ASPECTS:
                Map aspects = getAspectManager(false);
                value = new AspectManager(validator().processMap(theKey, value, true), this);
                if (aspects != null) ((Map) value).putAll(aspects);
                if (((Map) value).isEmpty()) {
                    remove(ASConstants.FIELD_ASPECTS);
                    return null;
                }
                break;
            case "_exceptions":
                break;
            case ASConstants.FIELD_ORIGIN:
                return null;
            case ASConstants.FIELD_PROPERTIES:
                if (value == null) {
                    //logger.warn("ehm " );
                    //addException(new InvalidPropertyContentError("Properties can not be populated with a null value"));
                    //ignore empty property
                    return null;
                } else if (!(value instanceof Map)) {
                    addProblem(new InvalidPropertyContentError(
                            "Properties must be populated with a map. Not a " + value.getClass() + ", value was: " + value));
                    return null;
                }
                break;
            case ASConstants.FIELD_ACL:
                if (value == null) {
                    addProblem(new InvalidPropertyContentError(key + " can not be populated with a null value"));
                    return null;
                } else if (!(value instanceof List)) {
                    addProblem(new InvalidPropertyContentError(key + " must be populated with a list. Not a " + value.getClass()));
                    return null;
                }
                break;
            default:
                if (ASConstants.ALL_ENTITY_RELATIONS.contains(key.toString())) {
                    relationManager = getRelationsManager();
                    if (relationManager == null)
                        relationManager = new RelationsManager(ASConstants.ALL_ENTITY_RELATIONS, this);
                    relationManager.addRelations(key.toString(), value);

                    theKey = ASConstants.FIELD_RELATIONS;
                    value = relationManager;
                } else {
                    //allow enrichment fields (All prefixed with "_")
                    if (!theKey.startsWith("_")) {
                        addProblem(new InvalidPropertyContentError("Ignored Property: " + theKey + " (contains: " + value + ")"));
                        return null;
                    }
                }
        }
        if (value == null || (value instanceof Collection && ((Collection) value).isEmpty())) return null;

        return super.put(theKey, value);
    }

    /************  Persistence ************/

    private DateTime getSnapshotDate() {
        DateTime occurredAt = null;
        DateTime receivedAt = null;
        BaseStreamElement element = this;
        while (occurredAt == null && element != null) {
            if (element instanceof BaseStreamItem) {
                occurredAt = ((BaseStreamItem) element).getOccurredAt();
                receivedAt = ((BaseStreamItem) element).getReceivedAt();
            }
            element = element.getRoot() == element ? null : element.getRoot();
        }
        return occurredAt != null ? occurredAt : receivedAt != null ? receivedAt : new DateTime();
    }

    public boolean isRedundant() {
        return redundant;
    }

    public void setRedundant(boolean redundant) {
        this.redundant = redundant;
    }

    public boolean isUnSavable() {
        return unSavable;
    }

    public void setUnSavable(boolean unsavable) {
        this.unSavable = unsavable;
    }

    public StatusAspect getStatusAspect() {
        return getAspectManager(true).getOrCreateStatusAspect();
    }

    /************  Changes ************/

    public List<EntityChangeMap> getChanges() {
        return this.changes;
    }

    public void registerChanges(EntityChangeMap changes) {
        registerChanges(changes, false);
    }

    public void registerChanges(EntityChangeMap changes, boolean updateIndex) {
        this.changes.add(changes);
        this.requireIndexUpdate = this.requireIndexUpdate || updateIndex;
    }

    public boolean hasChanged() {
        return this.changes != null && this.changes.size() > 0;
    }

    public boolean changesTriggerFulltextIndexing() {
        boolean indexChanges = false;
        for (EntityChangeMap entityChangeMap : getChanges()) {
            indexChanges = !entityChangeMap.getTriggersReIndexing().equals(EntityChangeMap.ACTION.IGNORE);
            if (indexChanges) break;
        }
        return indexChanges;
    }

    static List<String> OVERRIDES_FOR_INTERNAL_ENTITIES = Arrays.asList("ASUser", "ASIntelligence");

    public boolean isInternalEntity() {
        return OVERRIDES_FOR_INTERNAL_ENTITIES.contains(this.getEntityReference().getEntityTypeString());
    }

    public Set<String> getSnapshotTypes() {
        return getChanges().stream()
                .filter(e -> !e.getTriggersTimeSeriesUpdate().equals(EntityChangeMap.ACTION.IGNORE))
                .map(EntityChangeMap::getAspect)
                .collect(Collectors.toSet());
    }

    /************  UtilityFunctions ************/

    public BusinessEntity addMetrics(Map metricsMap) {
        super.withMetrics(metricsMap, this);
        return this;
    }

    public BusinessEntity withMetrics(Object... metrics) {
        super.withMetrics(this, metrics);
        return this;
    }

    public BusinessEntity withMetric(String metric, Number value) {
        super.withMetric(metric, value, this);
        return this;
    }

    public BusinessEntity setNow() {
        this.put(ASConstants.FIELD_OCCURRED_AT, new DateTime());
        return this;
    }

    public BusinessEntity withRelation(Relation relation, BaseStreamItem root) {
        this.getRelationsManager(true).addRelation(relation);
        return this;
    }

    public BusinessEntity addDimensions(Map dimensionsMap) {
        super.addDimensions(dimensionsMap, this);
        return this;
    }

    public BusinessEntity addDimensions(String... dimensions) {
        super.addDimensions(this, dimensions);
        return this;
    }

    public BusinessEntity withDimension(String dimension, String value) {
        super.addDimension(dimension, value, this);
        return this;
    }

    public BusinessEntity withAspect(AspectInterface aspect) {
        if (!aspect.isEmpty()) super.addAspect(aspect, this);
        return this;
    }

    public BusinessEntity withPresentation(String label, String thumbnail, String icon, String description, String detailsUrl) {
        super.addPresentation(label, thumbnail, icon, description, detailsUrl, this);
        return this;
    }

    public BusinessEntity addClassification(String type, String variant, List<String> categories, List<String> tags) {
        ClassificationAspect classification = super.getAspectManager().getClassification();
        if (classification == null) classification = new ClassificationAspect();

        classification.put(FIELD_TYPE, type);
        classification.put(FIELD_VARIANT, variant);
        classification.put(FIELD_CATEGORIES, categories);
        classification.put(FIELD_TAGS, tags);

        super.getAspectManager().setClassification(classification);
        return this;
    }

    @Override
    public BusinessEntity withProperties(Object... properties) {
        super.withProperties(properties);
        return this;
    }

    @Override
    public BusinessEntity withProperties(String property, Object value) {
        super.withProperties(property, value);
        return this;
    }

    @Override public BaseStreamItem getParentStreamItem() {
        return null;
    }

    @Override public void prepareForFederation() {

    }

}
