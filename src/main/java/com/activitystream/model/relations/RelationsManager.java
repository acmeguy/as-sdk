package com.activitystream.model.relations;

import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.core.AbstractListElement;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.validation.InvalidPropertyContentError;

import java.util.*;


public class RelationsManager extends AbstractListElement<Relation> implements EmbeddedStreamElement {

    List<String> validRelationsTypes = null;

    public RelationsManager(List relations, List<String> validRelationsTypes, BaseStreamElement root) {
        this.validRelationsTypes = validRelationsTypes;
        setRoot(root);
        if (relations != null) {
            for (Object relation : relations) {
                Relation baseRelation = (relation instanceof Relation) ? (Relation) relation : new Relation(relation, this.root);
                addRelation(baseRelation);
            }
        }
        verify();
    }

    public RelationsManager(String type, List relations, List<String> validRelationsTypes, BaseStreamElement root) {
        setRoot(root);
        for (Object relation : relations) {
            if (relation instanceof Relation) {
                logger.warn("Setting a relation as map: " + relation + "  " + ((Relation) relation).isEmpty());
                add((Relation) relation);
                continue;
            }
            if (relation instanceof Map && ((Map) relation).isEmpty()) {
                logger.error("Trying to set an empty map for relation of type: " + type);
                continue;
            }
            add(new Relation(type, relation, this.root));
        }
    }

    public RelationsManager(Object relations, List<String> validRelationsTypes, BaseStreamElement root) {
        this.validRelationsTypes = validRelationsTypes;
        setRoot(root);
        if (relations instanceof Relation) {
            ((Relation) relations).setRoot(root);
            add((Relation) relations);
        } else if (relations instanceof Map) {
            //todo - make sure this is not used differently elsewhere
            for (Map.Entry<String, Object> relation : (Set<Map.Entry<String, Object>>) ((Map) relations).entrySet()) {
                if (relation instanceof Map && ((Map) relation).isEmpty()) {
                    //todo - register error
                    continue;
                }
                add(new Relation(relation.getKey(), relation.getValue(), this.root));
            }
        } else if (relations instanceof List) {
            for (Object relation : (List) relations) {
                if (relation instanceof Relation) {
                    ((Relation) relation).setRoot(root);
                    add((Relation) relation);
                } else if (relation instanceof Map) {
                    Map relMap = (Map) relation;
                    if (relMap.isEmpty()) continue; //ignore
                    add(new Relation(relMap, this.root));
                } else {
                    logger.debug("WTF Relation! : " + relation);
                }
            }
        }
        verify();
    }

    public RelationsManager(List<String> validRelationsTypes, BaseStreamElement root) {
        this.validRelationsTypes = validRelationsTypes;
        setRoot(root);
    }

    public RelationsManager addRelation(Relation relation) {
        if (relation.isValid() && (this.validRelationsTypes == null || this.validRelationsTypes.contains(relation.getRelationsType().getRootRelationsType()))) {
            add(relation);
        } else if (this.validRelationsTypes != null) {
            root.addProblem(new InvalidPropertyContentError(
                    "Invalid Relationship Type. '" + relation.getRelationsType().getRootRelationsType() + "' is not in list: " +
                            this.validRelationsTypes.toString()));
        }
        if (relation.getRoot() == null) {
            relation.setRoot(getRoot());
        }
        return this;
    }

    public RelationsManager addRelations(String type, Object value) {
        if (type != null && value instanceof List) {
            for (Object relation : (List) value) {
                this.add(new Relation(type, relation, this.root));
            }
        } else if (value instanceof String) {
            this.add(new Relation(type, value, this.root));
        } else {
            logger.error("Unhandled relations: " + type + " " + value + " " + value.getClass().getSimpleName());
        }
        return this;
    }

    @Override
    public void simplify() {
        for (Relation relation : this) {
            relation.simplify();
        }
    }

    /************  Utilities ************/

    @Override
    public ListIterator listIterator(int index) {
        if (this.securityScope != null) {
            //todo - optimize - implement this as an actually filtered list iterator
            //When Security Scope is set then - filter the list into another list and pass that along for processing
            List<Relation> fitleredRelations = new LinkedList<>();
            for (Object entry : super.toArray()) {
                Relation relation = (Relation) entry;

                relation = this.securityScope.simplifyRelation(relation);

                if (!this.securityScope.filterRelation(relation)) {
                    fitleredRelations.add(relation);
                }

            }
            return fitleredRelations.listIterator();
        }
        return super.listIterator(index);
    }


    public int countRelationsOfType(String... types) {
        //todo - optimize with adding edge numbers without getting the relations
        return getRelationsOfType(types).size();
    }

    public boolean hasRelationsOfType(String type) {
        return traversal().anyEntityRelationsMatch(relation -> {
            RelationsType relationsType = relation.getRelationsType();
            return relationsType.getRootRelationsType().equals(type) || relationsType.getRelationsType().equals(type) ||
                    relationsType.getRelationsTypeString().equals(type);
        });
    }

    public List<Relation> getRelationsOfType(String... types) {
        List<Relation> relations = new LinkedList<>();
        for (String type : types) {
            traversal().forEachEntityRelation(relation -> {
                RelationsType relationsType = relation.getRelationsType();
                if (relationsType.getRootRelationsType().equals(type) || relationsType.getRelationsType().equals(type) ||
                        relationsType.getRelationsTypeString().equals(type)) {
                    relations.add(relation);
                }
            });
        }
        return relations;
    }

    public Relation getFirstRelationsOfType(String... types) {
        for (String type : types) {
            Optional<Relation> found = traversal().findFirstEntityRelation(relation -> {
                RelationsType relationsType = relation.getRelationsType();
                return relationsType.getRootRelationsType().equals(type) || relationsType.getRelationsType().equals(type) ||
                        relationsType.getRelationsTypeString().equals(type);
            });
            if (found.isPresent()) return found.get();
        }
        return null;
    }

    public void removeIdenticalRelations(Relation relation) {
        int found;
        do {
            found = -1;
            for (int i = 0; i < this.size(); i++) {
                Relation localRelation = this.get(i);
                if (localRelation.equals(relation)) {
                    found = i;
                    break;
                }
            }
            if (found > -1) this.remove(found);

        } while (found > -1);
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {
        for (Relation relation : this) relation.verify();
    }

    public boolean equals(RelationsManager relations) {
        return super.equals(relations);
    }
}
