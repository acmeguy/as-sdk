package com.activitystream.model.interfaces;

import com.activitystream.model.validation.MessageProblem;
import com.activitystream.model.validation.MessageValidator;

public interface VerifiableElement extends LinkedElement {

    void verify();

    boolean isValid();

    MessageValidator validator();

    default void addProblem(MessageProblem messageProblem) {
        validator().addProblem(messageProblem);
    }
}
