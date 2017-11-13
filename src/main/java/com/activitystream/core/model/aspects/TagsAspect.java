package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.validation.UnsupportedAspectError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings("unchecked")
public class TagsAspect extends AbstractListAspect<String> {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_TAGS, TagsAspect::new, null);

    private static final Logger logger = LoggerFactory.getLogger(TagsAspect.class);

    public TagsAspect() {
    }

    @Override
    public void loadFromValue(Object value) {
        if (value != null) {
            if (value instanceof List) {
                try {
                    for (String tag : (List<String>) value) ;
                } catch (Exception e) {
                    this.addProblem(new UnsupportedAspectError("Tags should contain a list of strings, not: " + value + ". " + e));
                }
                addAll((List) value);
            } else if (value instanceof String) {
                Collections.addAll(this, ((String) value).split(" "));
            } else {
                this.addProblem(new UnsupportedAspectError("Tags should contain a list of strings, not: " + value));
                return;
            }
        }
        verify();
    }

    /************
     * Utility functions
     ************/

    public TagsAspect withTags(String ... tags) {
        addAll(Arrays.asList(tags));
        return this;
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************ Assignment & Validation ************/

    @Override
    public void verify() {

    }

    public static TagsAspect tags() {
        return new TagsAspect();
    }

}
