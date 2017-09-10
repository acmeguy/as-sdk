package com.activitystream.model.analytics;

import org.apache.avro.specific.SpecificRecordBase;

public class RegisteredTSPart {

    SpecificRecordBase registeredPart;
    Long weight;
    String part;

    public RegisteredTSPart(String part, Long weight, SpecificRecordBase registeredPart) {
        this.part = part;
        this.weight = weight;
        this.registeredPart = registeredPart;
    }

    public SpecificRecordBase getRegisteredPart() {
        return registeredPart;
    }

    public Long getWeight() {
        return weight;
    }

    public String getPart() {
        return part;
    }
}
