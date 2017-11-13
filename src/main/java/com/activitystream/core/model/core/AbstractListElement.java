package com.activitystream.core.model.core;

import com.activitystream.core.model.validation.MessageValidator;
import com.activitystream.core.model.entities.BusinessEntity;
import com.activitystream.core.model.interfaces.BaseStreamElement;
import com.activitystream.core.model.interfaces.LinkedElement;
import com.activitystream.core.model.security.SecurityScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractListElement<T> extends LinkedList<T> implements BaseStreamElement {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractListElement.class);

    public BaseStreamElement root = null;
    private MessageValidator messageValidator;
    protected SecurityScope securityScope = null;

    public AbstractListElement() {
    }

    public AbstractListElement(List values, BaseStreamElement root) {
        addAll(values);
        this.root = root;
        verify();
    }

    /************  Access  ************/


    @Override
    public void setRoot(BaseStreamElement root) {
        this.root = root;
    }

    @Override
    public BaseStreamElement getRoot() {
        return root;
    }

    @Override
    public BusinessEntity getRootBusinessEntity() {
        BusinessEntity topMostEntity = null;
        BaseStreamElement topMost = root;
        while (topMost != null) {
            topMost = topMost.getRoot();
            if (topMost instanceof BusinessEntity) {
                topMostEntity = (BusinessEntity) topMost;
            }
        }
        return topMostEntity;
    }

    /************  Utility Functions  ************/

    @Override
    public boolean traverse(ElementVisitor visitor) {
        if (!visitor.visit(this))
            return false;

        for (Object entry : this) {
            if (entry instanceof LinkedElement) {
                if (!((LinkedElement) entry).traverse(visitor))
                    return false;
            }
        }

        return true;
    }

    @Override
    public void setSecurityScope(SecurityScope scope) {
        this.securityScope = scope;
    }

    /************ Validation and Error Handling ************/

    @Override
    public boolean isValid() {
        return validator().hasErrors() < 1;
    }

    @Override
    public MessageValidator validator() {
        if (messageValidator == null) {
            if (root != null && root != this)
                messageValidator = root.validator();
            else
                messageValidator = new MessageValidator();
        }
        return messageValidator;
    }

}
