package com.activitystream.model.internal;

import com.activitystream.model.ASConstants;
import com.activitystream.model.entities.BaseEntity;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.BaseStreamElement;
import com.activitystream.model.interfaces.BaseStreamItem;
import com.activitystream.model.security.ProcessSettings;
import com.activitystream.model.stream.AbstractStreamItem;
import com.activitystream.model.utils.StreamIdUtils;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class InternalEntity extends AbstractStreamItem implements BaseEntity, BaseStreamItem {

    private static final Logger logger = LoggerFactory.getLogger(InternalEntity.class);
    static final List<String> nativeProperties = Arrays.asList(ASConstants.FIELD_PROPERTIES);
    protected static final Set<String> MANAGED_PROPERTIES =
            ImmutableSet.of(ASConstants.FIELD_OCCURRED_AT, ASConstants.FIELD_TYPE, ASConstants.FIELD_ASPECTS, ASConstants.FIELD_INVOLVES,
                    ASConstants.FIELD_RELATIONS, ASConstants.FIELD_ENTITY_REF, ASConstants.FIELD_TYPE_REF, ASConstants.FIELD_ACL, ASConstants.FIELD_PARTITION);

    private static final ProcessSettings INTERNAL_ENTITY_PROCESS_SETTINGS = new ProcessSettings() {{
        active = true;
        store = true;
    }};

    static List<String> entityAllowedRelTypes = null;

    /************  Constructors ************/

    public InternalEntity() {
        super();
        setFixedProperties();
    }

    public InternalEntity(Map map) {
        this(map, null);
    }

    public InternalEntity(Map map, BaseStreamElement root) {
        super(map, root);
        setFixedProperties();
    }

    protected void setFixedProperties() {
        if (getElementType() != null) super.put("_class", getElementType());
    }

    @Override
    public String getRelationsRootType() {
        return ASConstants.FIELD_RELATIONS;
    }

    /************  Utility functions  ************/

    public List<String> getNativeProperties() {
        return nativeProperties;
    }

    @Override
    public List<String> getAllowedRelTypes() {
        if (entityAllowedRelTypes == null) {
            entityAllowedRelTypes = new LinkedList<>();
            entityAllowedRelTypes.addAll(ASConstants.ENTITY_RELATIONS);
            entityAllowedRelTypes.addAll(ASConstants.INTEREST_TYPES);
        }
        return entityAllowedRelTypes;
    }

    @Override
    public List<String> getAllowedAspects() {
        return ASConstants.ALL_ASPECT_FIELDS;
    }

    public EntityReference getEntityReference() {
        return (EntityReference) get(ASConstants.FIELD_ENTITY_REF);
    }


    /************  Stream functions  ************/

    @Override
    public void setMessageKey(String messageKey) {
        logger.warn("Should not be used or at least overwritten");
    }

    @Override
    public String getMessageKey() {
        logger.warn("Should not be used or at least overwritten");
        return null;
    }

    @Override
    public ProcessSettings getProcessSettings() {
        return INTERNAL_ENTITY_PROCESS_SETTINGS;
    }

    @Override
    public UUID getStreamId() {
        if (get(ASConstants.FIELD_ENTITY_ID) != null)
            return StreamIdUtils.calculateStreamId(getElementType(), (String) get(ASConstants.FIELD_ENTITY_ID));
        if (getEntityReference() != null)
            return getEntityReference().getEntityStreamId();
        return null;
    }

    protected Set<String> getManagedProperties() {
        return MANAGED_PROPERTIES;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {
        super.verify();
        if (getAspectManager() != null) getAspectManager().verify();
        if (getRelationsManager() != null) getRelationsManager().verify();
    }

    @Override
    public void simplify() {
        if (hasAspects()) {
            getAspectManager().simplify();
            if (getAspectManager().isEmpty())
                remove(ASConstants.FIELD_ASPECTS);
        }
        remove(ASConstants.FIELD_STREAM_ID_INTERNAL);
    }
}
