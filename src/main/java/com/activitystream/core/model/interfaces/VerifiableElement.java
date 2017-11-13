package com.activitystream.core.model.interfaces;

import com.activitystream.core.model.validation.MessageProblem;
import com.activitystream.core.model.validation.MessageValidator;

public interface VerifiableElement extends LinkedElement {

    void verify();

    boolean isValid();

    MessageValidator validator();

    default void addProblem(MessageProblem messageProblem) {
        validator().addProblem(messageProblem);
    }
}
