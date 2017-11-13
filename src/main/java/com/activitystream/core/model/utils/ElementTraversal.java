package com.activitystream.core.model.utils;

import com.activitystream.core.model.interfaces.LinkedElement;
import com.activitystream.core.model.entities.BusinessEntity;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.relations.Relation;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ElementTraversal {

    private final LinkedElement root;

    public ElementTraversal(LinkedElement root) {
        this.root = root;
    }

    public void forEachElement(Consumer<LinkedElement> action) {
        traverse(t -> {
            action.accept(t);
            return true;
        });
    }

    public void forEachEntityReference(Consumer<EntityReference> action) {
        traverse(element -> {
            element.onEachEntityReference(action);
            return true;
        });
    }

    public boolean anyAnyEntityReferencesMatch(Predicate<EntityReference> predicate) {
        boolean[] result = new boolean[]{false};
        traverse(element -> {
            element.onEachEntityReference(reference -> {
                if (predicate.test(reference))
                    result[0] = true;
            });
            return !result[0];
        });
        return result[0];
    }

    public void forEachBusinessEntity(Consumer<BusinessEntity> action) {
        traverse(element -> {
            if (element instanceof BusinessEntity)
                action.accept((BusinessEntity) element);
            return true;
        });
    }

    public boolean allBusinessEntitiesMatch(Predicate<BusinessEntity> predicate) {
        boolean[] result = new boolean[]{true};
        traverse(element -> {
            if (element instanceof BusinessEntity && !predicate.test((BusinessEntity) element)) {
                result[0] = false;
                return false;
            }
            return true;
        });
        return result[0];
    }

    public void forEachRelationType(LinkedElement.StreamItemRelationTypeConsumer action) {
        traverse(element -> {
            element.onEachRelationType(action);
            return true;
        });
    }

    public void forEachEntityRelation(Consumer<Relation> action) {
        traverse(element -> {
            element.onEachEntityRelation(action);
            return true;
        });
    }

    public boolean anyEntityRelationsMatch(Predicate<Relation> predicate) {
        boolean[] result = new boolean[]{false};
        traverse(element -> {
            element.onEachEntityRelation(relation -> {
                if (predicate.test(relation))
                    result[0] = true;
            });
            return !result[0];
        });
        return result[0];
    }

    public Optional<Relation> findFirstEntityRelation(Predicate<Relation> predicate) {
        Relation[] result = new Relation[]{null};
        traverse(element -> {
            element.onEachEntityRelation(relation -> {
                if (result[0] == null && predicate.test(relation))
                    result[0] = relation;
            });
            return result[0] == null;
        });
        return Optional.ofNullable(result[0]);
    }

    private void traverse(LinkedElement.ElementVisitor visitor) {
        root.traverse(visitor);
    }
}
