package com.activitystream.core.model.security;

import com.activitystream.core.model.relations.Relation;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SecurityScope {

    Map<String, String> entityTypeTranslations = new LinkedHashMap<>();
    List<String> blockedEntityTypes = new LinkedList<>();
    List<String> simplifiedEntityTypes = new LinkedList<>();
    List<String> accessControlPrivileges = new LinkedList<>();

    public SecurityScope() {

    }

    public SecurityScope(Map<String, String> entityTypeTranslations, List<String> blockedEntityTypes, List<String> accessControlPrivileges,
                         List<String> simplifiedEntityTypes) {
        this.entityTypeTranslations = entityTypeTranslations;
        this.blockedEntityTypes = blockedEntityTypes;
        this.accessControlPrivileges = accessControlPrivileges;
        this.simplifiedEntityTypes = simplifiedEntityTypes;
    }

    public Map<String, String> getEntityTypeTranslations() {
        return entityTypeTranslations;
    }

    public void setEntityTypeTranslations(Map<String, String> entityTypeTranslations) {
        this.entityTypeTranslations = entityTypeTranslations;
    }

    public List<String> getBlockedEntityTypes() {
        return blockedEntityTypes;
    }

    public void setBlockedEntityTypes(List<String> blockedEntityTypes) {
        this.blockedEntityTypes = blockedEntityTypes;
    }

    public List<String> getAccessControlPrivileges() {
        return accessControlPrivileges;
    }

    public void setAccessControlPrivileges(List<String> accessControlPrivileges) {
        this.accessControlPrivileges = accessControlPrivileges;
    }

    public List<String> getSimplifiedEntityTypes() {
        return simplifiedEntityTypes;
    }

    public void setSimplifiedEntityTypes(List<String> simplifiedEntityTypes) {
        this.simplifiedEntityTypes = simplifiedEntityTypes;
    }

    public boolean filterRelation(Relation relation) {
        return relation.getRelatedBusinessEntity() != null &&
                blockedEntityTypes.contains(relation.getRelatedBusinessEntity().getEntityReference().getEntityTypeString());
    }

    public Relation simplifyRelation(Relation relation) {
        if (simplifiedEntityTypes != null && relation.getRelatedBusinessEntity() != null &&
                simplifiedEntityTypes.contains(relation.getRelatedBusinessEntity().getEntityReference().getEntityTypeString())) {
            return new Relation(relation.getRelationsType().getRelationsType(), relation.getRelatedBusinessEntity().getEntityReference().getEntityReference());
        } else {
            return relation;
        }
    }

    public String getAdjustedEntityReference(String type, String id) {
        return this.entityTypeTranslations.getOrDefault(type, type) + "/" + id.toLowerCase();
    }


}
