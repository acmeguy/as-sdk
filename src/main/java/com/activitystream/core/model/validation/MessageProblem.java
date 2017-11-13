package com.activitystream.core.model.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageProblem {

    private String message;

    public MessageProblem(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "message='" + message + '\'' +
                '}';
    }
}
