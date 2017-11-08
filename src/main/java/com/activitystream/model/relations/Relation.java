package com.activitystream.model.relations;

import com.activitystream.model.*;
import com.activitystream.model.core.AbstractMapElement;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.stream.AbstractBaseEvent;
import com.activitystream.model.stream.AbstractStreamItem;
import com.activitystream.model.stream.CustomerEvent;
import com.activitystream.model.stream.TransactionEvent;
import com.activitystream.model.validation.InvalidPropertyContentError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class Relation extends AbstractMapElement implements EmbeddedStreamElement, EnrichableElement {

    protected static final Logger logger = LoggerFactory.getLogger(Relation.class);
    private final static List<String> EXPANDED_ASPECTS =
            Arrays.asList(ASConstants.ASPECTS_PRESENTATION, ASConstants.ASPECTS_CLIENT_DEVICE, ASConstants.ASPECTS_TIMED, ASConstants.ASPECTS_CLIENT_IP,
                    ASConstants.ASPECTS_TRAFFIC_SOURCE, ASConstants.ASPECTS_DIMENSIONS, ASConstants.ASPECTS_METRICS, ASConstants.ASPECTS_CLASSIFICATION,
                    ASConstants.ASPECTS_ADDRESS,
                    ASConstants.ASPECTS_DEMOGRAPHY, ASConstants.ASPECTS_INVENTORY);

    private RelationsType relationsType = null;
    private boolean isExpanded = false;
    private Direction direction = null;

    public Relation(Map relations, BaseStreamElement root) {
        setRoot(root);

        if (relations instanceof Relation) {
            logger.error("Trying to create a relation with a relation 1 : " + relations);
        } else {
            setMappedRelations(relations);
        }
    }

    public Relation(Object value, BaseStreamElement root) {
        setRoot(root);
        if (value instanceof Relation) {
            logger.error("Trying to create a relation with a relation 2 : " + value);
        } else if (value instanceof Map) {
            setMappedRelations((Map) value);
        } else if (value instanceof List) {
            logger.warn("WTF - relations is a list: " + value);
        } else {
            logger.warn("WTF item information can not be: " + value);
            addProblem(new InvalidPropertyContentError("ItemRelations information are incorrect: " + value));
        }
        verify();
    }

    public Relation(String type, Object value) {
        this(type, value, null);
    }

    public Relation(String type, BusinessEntity entity) {
        this(type, entity, null);
    }

    public Relation(String type, Object value, BaseStreamElement root) {
        setRoot(root);

        Map relationsMap = new LinkedHashMap<>();
        relationsMap.put(type, value);

        if (value instanceof Relation) {
            logger.error("Trying to create a relation with a relation 3 : " + value);
        } else if (value instanceof BusinessEntity) {
            this.setRelationsType(RelationsType.resolveTypesString(type));
            this.setRelatedItem((BusinessEntity) value);
        } else if (value instanceof Map) {
            this.setMappedRelations(relationsMap);
        } else if (value instanceof String) {
            this.setRelationsType(RelationsType.resolveTypesString(type));
            this.setRelatedItem(entityForValue(value, root));
        } else if (value instanceof EntityReference) {
            this.setRelationsType(RelationsType.resolveTypesString(type));
            this.setRelatedItem(entityForValue(value, root));
        } else if (value instanceof List) {
            logger.error("Unsupported list value for relations: " + value);
        } else {
            addProblem(new InvalidPropertyContentError("ItemRelations information are incorrect: " + value));
        }
    }

    /************  Access  ************/


    public BaseStreamElement getRelatedEntityItem() {
        return (this.isRelatedItemABusinessEntity()) ? (BusinessEntity) this.getRelatedItem() : null;
    }

    public BusinessEntity getRelatedBusinessEntity() {
        return (BusinessEntity) getRelatedEntityItem();
    }


    public boolean isRelatedItemABusinessEntity() {
        return this.getRelatedItem() != null && this.getRelatedItem() instanceof BusinessEntity;
    }

    public RelationsType getRelationsType() {
        return this.relationsType;
    }

    public void setRelationsType(RelationsType relType) {
        this.relationsType = relType;
    }

    public BaseStreamElement getRelatedItem() {
        if (this.getRelationsType() != null && this.get(this.getRelationsType()) instanceof BaseStreamElement)
            return (BaseStreamElement) get(this.getRelationsType());
        return null;
    }

    public void setRelatedItem(BaseStreamElement relatedItem) {
        if (this.getRelationsType() != null) put(this.getRelationsType(), relatedItem);
        else logger.error("No Relationship Type specified before setting related entity!!!!");
    }

    public String getDirection() {
        return (String) get("$direction");
    }

    public Relation reverse() {
        String currentDirection = (String) get("$direction");
        if (currentDirection == null || currentDirection.equals(Direction.OUT.toString())) {
            directPut("$direction", Direction.IN.toString());
        } else {
            remove("$direction");
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Relation) {
            return ((Relation) o).getRelationsType().getRelationsType().equals(this.getRelationsType().getRelationsType()) &&
                    ((Relation) o).getRelatedItem().equals(this.getRelatedItem());
        }
        return super.equals(o);
    }

    @Override
    public void simplify() {
        BusinessEntity entity = getRelatedBusinessEntity();
        if (entity != null) super.put(this.getRelationsType(), new BusinessEntity(entity.getEntityReference().getEntityReference(), this));
    }


    /************  Utility functions  ************/

    public boolean isValid() {
        return true;
    }

    public boolean isOfType(String type) {
        if (this.getRelationsType() == null) return false;

        for (String relType : this.getRelationsType().getRelationsType().split(":")) {
            if (relType.equalsIgnoreCase(type)) return true;
        }
        return false;
    }

    public boolean isVisible() {
        BusinessEntity entity = getRelatedBusinessEntity();
        if (entity != null)
            return entity.getStatusAspect().isVisible();
        return true;
    }


    /**
     * Goes through the relation information and transforms it into valid relations.
     * Takes the event relation and puts it first.
     * Then it takes alternative relations and adds it to the related entity (if multiple reltions used)
     *
     * @param valueMap
     */
    public void setMappedRelations(Map<Object, Object> valueMap) {

        if (valueMap == null || valueMap.isEmpty()) {
            //logger.error("No relations can be created for an empty map: " + valueMap + " for " + this.root);
            return;
        }

        if ((valueMap.containsKey("type") || valueMap.containsKey("role")) && (valueMap.containsKey("entity") || valueMap.containsKey("entity_ref"))) {
            //Explicit
            String relType = (String) valueMap.getOrDefault("type", valueMap.get("role"));
            valueMap.remove("type");
            valueMap.remove("role");
            this.setRelationsType(RelationsType.resolveTypesString(relType));

            if (valueMap.containsKey("entity")) {
                this.setRelatedItem(entityForValue(valueMap.get("entity"), this));
                valueMap.remove("entity");
            } else if (valueMap.containsKey("entity_ref")) {
                this.setRelatedItem(entityForValue(valueMap.get("entity_ref"), this));
                valueMap.remove("entity_ref");
            }
            put(this.getRelationsType(), this.getRelatedItem());
        } else {
            //Implicit
            //todo - refactor to use iterator that support remove()

            boolean eventScope = (this.root != null && (this.root instanceof CustomerEvent || this.root instanceof TransactionEvent));

            List<String> foundKeys = new LinkedList<>();

            //try to resolve event actor/role first
            if (eventScope) {
                for (Map.Entry entry : valueMap.entrySet()) {
                    String key = entry.getKey().toString();
                    if (!key.equals(key.toUpperCase())) continue; //consider only allcaps keys as relations types

                    if (RelationsType.isEventRelationsKey(key)) {
                        foundKeys.add(key);
                        this.setRelationsType(RelationsType.resolveTypesString(entry.getKey().toString()));
                        this.setRelatedItem(entityForValue(entry.getValue(), this));
                        break; //only include first *event* relations
                    }
                }
            }

            //If no event (primary) relation was found then find the first relation listsited
            if (this.getRelatedItem() == null) {
                //Find the first relations
                for (Map.Entry entry : valueMap.entrySet()) {
                    String key = entry.getKey().toString();
                    if (!key.equals(key.toUpperCase())) continue; //consider only allcaps keys as relations types

                    foundKeys.add(key);
                    this.setRelationsType(RelationsType.resolveTypesString(entry.getKey().toString()));
                    this.setRelatedItem(entityForValue(entry.getValue(), root));
                    break; //only include first *any* relations
                }
            }

            //If there is only one key there then it must be the relationship... right?
            //remove this lenient workaround ASAP
            if (foundKeys.isEmpty() && valueMap.size() == 1) {
                String key = (String) valueMap.keySet().iterator().next();
                this.setRelationsType(RelationsType.resolveTypesString(key.toUpperCase()));
                this.setRelatedItem(entityForValue(valueMap.get(key), root));
                foundKeys.add(key);
            }

            //Remove the used relations from the map

            for (String key : foundKeys) valueMap.remove(key);

            if (foundKeys.isEmpty()) {
                logger.error("No primary relations found in: " + valueMap);
                return;
            }
            foundKeys.clear();

            //Add the main property to this Relations map
            put(this.getRelationsType(), this.getRelatedItem());

            //Add any remaining relations to the Related entity

            if (this.isRelatedItemABusinessEntity()) {
                BusinessEntity instEntity = (BusinessEntity) this.getRelatedItem();
                for (Map.Entry entry : valueMap.entrySet()) {
                    String key = entry.getKey().toString();
                    if (key.equals(key.toUpperCase())) {
                        BusinessEntity relEntity = entityForValue(entry.getValue(), this);
                        if (!instEntity.getEntityReference().getEntityReference().equals(relEntity.getEntityReference().getEntityReference())) {
                            //foundKeys.add(key);
                            //instEntity.withRelations(key, relEntity);
                        }
                    }
                }
            }

            for (String key : foundKeys) valueMap.remove(key);
        }

        //Set the rest of the properties from the list
        if (!valueMap.isEmpty()) setMapValues(valueMap);
    }

    public String getFootprint() {
        String ref = this.getRelationsType().getRelationsType() + "-";
        if (this.isRelatedItemABusinessEntity()) ref += ((BusinessEntity) this.getRelatedItem()).getEntityReference();
        else ref += this.getRelatedItem().toString();
        if (containsKey(ASConstants.FIELD_EXTERNAL_ID) && get(ASConstants.FIELD_EXTERNAL_ID) != null) {
            ref += "(" + get(ASConstants.FIELD_EXTERNAL_ID) + ")";
        }
        return ref;
    }

    private BusinessEntity entityForValue(Object value, BaseStreamElement root) {
        BusinessEntity entity = null;
        if (value instanceof Map) {
            entity = new BusinessEntity((Map) value, root);
        } else if (value instanceof String) {
            entity = new BusinessEntity((String) value, root);
        } else if (value instanceof EntityReference) {
            entity = new BusinessEntity(value, root);
        } else if (value instanceof List) {
            logger.warn("Relations is a list !!!!");
        }
        return entity;
    }

    @Override
    public void onEachRelationType(StreamItemRelationTypeConsumer action) {
        String rootType = getRoot() instanceof BaseStreamItem ? ((BaseStreamItem) getRoot()).getElementType() : ASConstants.AS_STREAM_ITEM;
        String relatedType = isRelatedItemABusinessEntity() ? getRelatedBusinessEntity().getElementType() : ASConstants.AS_STREAM_ITEM;

        SaveDirection dir = checkReverseRelation();

        if (dir.incomingRelation) {
            action.accept(dir.fullRelationType, relatedType, rootType);
        } else {
            action.accept(dir.fullRelationType, rootType, relatedType);
        }
    }

    @Override
    public void onEachEntityRelation(Consumer<Relation> action) {
        action.accept(this);
    }

    private SaveDirection checkReverseRelation() {
        SaveDirection dir = new SaveDirection();
        dir.leafRelationType = this.getRelationsType().getRelationsType();
        dir.fullRelationType = this.getRelationsType().getRelationsTypeString();
        dir.incomingRelation = false;

        if (this.root instanceof AbstractBaseEvent && this.getRelationsType().getRelationsTypeString().startsWith(ASConstants.REL_ACTOR))
            dir.incomingRelation = true;
        else if (Direction.IN.equals(this.direction))
            dir.incomingRelation = true;

        if (ASConstants.REVERSED_RELATIONS.containsKey(dir.leafRelationType)) {
            // These relations are all predefined so the leaf and full types will always be the same.
            dir.fullRelationType = dir.leafRelationType = ASConstants.REVERSED_RELATIONS.get(dir.leafRelationType);
            dir.incomingRelation = true;
        }

        return dir;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {

    }

    @Override
    public Object put(Object key, Object value) {

        if (key == null) {
            //Todo - whi is this happening?
            logger.error("key, Object value: " + key + ", " + value);
            return null;
        }

        String theKey = key.toString();

        if (this.isRelatedItemABusinessEntity()) {
            BusinessEntity relatedEntity = this.getRelatedBusinessEntity();
            if (theKey.equals("label")) {
                return relatedEntity.addLabel(value);
            } else if (theKey.equals("_patch")) {
                return relatedEntity.addPatch(value);
            } else if (theKey.equals("$direction")) {
                /*
                try {
                    this.direction = Direction.valueOf((String) value);
                } catch (IllegalArgumentException e) {
                    addProblem(new InvalidPropertyContentError("Direction is not know: " + value));
                    return null;
                }
                */
                return null;
            } else if (root instanceof AbstractStreamItem && RelationsType.isEntityRelationsKey(theKey) && value != relatedEntity) {
                //todo - fix incorrect assignment
                return relatedEntity.withRelations(theKey, value);
            }
            //else this is a normal property and we should just store it
        }

        return super.put(key, value);
    }

    public Object directPut(Object key, Object value) {
        return super.put(key, value);
    }

    @Override
    public String toString() {
        if (this.isRelatedItemABusinessEntity()) {
            return "Relation{" + "entity_ref=" + this.getRelatedBusinessEntity().getEntityReference().getEntityReference() + ", link=" +
                    relationsType.getRelationsTypeString() + (this.containsKey("properties") ? ", properties=" + this.get("properties") : "") + '}';
        } else {
            return "Relation{" + "stream_id=" + getRelatedItem() + ", role=" + (relationsType != null ? relationsType.getRelationsTypeString() : "missing") +
                    (this.containsKey("properties") ? ", properties=" + this.get("properties") : "") + '}';
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    private static class SaveDirection {

        /**
         * If true, the relation is from (OUT) the related item to (IN) the root item.
         *
         * Otherwise (the default) the relation is from (OUT) the root to (IN) the related item.
         */
        boolean incomingRelation;
        String leafRelationType;
        String fullRelationType;
    }
}
