package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.LinkedElement;
import com.activitystream.model.internal.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
    Wraps Attachments as related entities to a event or another entity.
    Store core data in the Attachment entity but offers override for some properties based on local use.
    The first time it's used determines what properties get set for the initial Attachment entity and relations properties will be used to override them as needed.
    Attachment registration should be treated as immutable, at least until the can be managed as a collective (of root entity and all use)
 */
public class AttachmentManager extends AbstractListAspect<Attachment> implements LinkedElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_ATTACHMENTS, AttachmentManager::new) {
        /*
        @Override
        public boolean isActive(SavableElement element, BaseStreamElement root) {
            return element instanceof OrientVertex && ((OrientVertex) element).countEdges(Direction.OUT, ASConstants.REL_ATTACHED) > 0;
        }
        */
    };

    protected static final Logger logger = LoggerFactory.getLogger(AttachmentManager.class);

    static final List<String> RELATIONS_OVERRIDE_FIELDS =
            Arrays.asList(ASConstants.FIELD_FILENAME, ASConstants.FIELD_SIZE, ASConstants.FIELD_CREATED, ASConstants.FIELD_METADATA,
                    ASConstants.FIELD_PROPERTIES, ASConstants.FIELD_FILENAME, ASConstants.ASPECTS_PRESENTATION);
    static final List<String> RELATIONS_ONLY_FIELDS =
            Arrays.asList(ASConstants.FIELD_FILENAME, ASConstants.FIELD_SIZE, ASConstants.FIELD_CREATED, ASConstants.FIELD_METADATA,
                    ASConstants.FIELD_PROPERTIES, ASConstants.FIELD_FILENAME, ASConstants.ASPECTS_PRESENTATION);

    public AttachmentManager() {
    }

    @Override
    public void loadFromValue(Object attachments) {

        if (attachments instanceof Map) {
            add(new Attachment((Map) attachments, getRoot()));
        } else if (attachments instanceof List) {
            ((List) attachments).forEach(attachment -> {
                if (attachment instanceof Map) add(new Attachment((Map) attachment, getRoot()));
                else logger.error("Unknown attachment value type: " + attachment.getClass());
            });
        }
        verify();
    }

    /************
     * Utilities
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {

    }

}
