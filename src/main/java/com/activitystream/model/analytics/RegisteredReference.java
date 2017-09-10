package com.activitystream.model.analytics;


import com.activitystream.model.entities.EntityReference;

public class RegisteredReference {

    EntityReference registeredReference;
    Long weight;
    String role;

    public RegisteredReference(String role, Long weight, EntityReference registeredReference) {
        this.role = role;
        this.weight = weight;
        this.registeredReference = registeredReference;
    }

    public EntityReference getRegisteredReference() {
        return registeredReference;
    }

    public Long getWeight() {
        return weight;
    }

    public String getRole() {
        return role;
    }
}
