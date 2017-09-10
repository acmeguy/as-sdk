package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.interfaces.EnrichableElement;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContentAspect extends AbstractMapAspect implements EnrichableElement {

    public static final AspectType ASPECT_TYPE = new AspectType.Embedded(ASConstants.ASPECTS_CONTENT, ContentAspect::new, AspectType.MergeStrategy.REPLACE);

    public static final Pattern REFERENCE_PATTERN = Pattern.compile("@([a-zA-Z0-9/-]+)");
    private static final Pattern NON_TEXT_PATTERN = Pattern.compile(":\\w+:|" + REFERENCE_PATTERN.pattern());

    public ContentAspect() {
    }

    /************
     * Utility functions
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    /************
     * CEP Utility Functions and Getters
     ************/

    public String getTitle() {
        return (String) get(ASConstants.FIELD_TITLE);
    }

    public String getSubtitle() {
        return (String) get(ASConstants.FIELD_SUBTITLE);
    }

    public String getByline() {
        return (String) get(ASConstants.FIELD_BYLINE);
    }

    public String getContent() {
        return (String) get(ASConstants.FIELD_CONTENT);
    }

    /************ Assignment & Validation ************/

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("The property name: '" + theKey + "' was converted to lower case"));
            theKey = theLCKey;
        }

        switch (theKey) {
            case ASConstants.FIELD_SUBJECT:
                theKey = ASConstants.FIELD_TITLE;
            case ASConstants.FIELD_TITLE:
                value = validator().processString(theKey, value, true);
                break;
            case ASConstants.FIELD_SUBTITLE:
            case ASConstants.FIELD_BYLINE:
            case ASConstants.FIELD_CONTENT:
            case ASConstants.FIELD_DETECTED_LANGUAGE: //Locale string
                value = validator().processString(theKey, value, false);
                break;
            case ASConstants.FIELD_SENTIMENT: //<String, double>
                break;
            default:
                if (!theKey.startsWith("_"))
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Content Aspect"));
        }

        return super.put(theKey, value);
    }

    protected Object directPut(Object key, Object value) {
        return super.put(key, value);
    }

    @Override
    public void verify() {

    }

    /************  Enrichment ************/

    private String getTotalContent() {
        return Stream.of(getTitle(), getSubtitle(), getByline(), getContent())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .map(s -> s.endsWith(".") ? s : (s + "."))
                .collect(Collectors.joining("\n"));
    }

    public String getTotalContentWithoutMarkup() {
        throw new NotImplementedException();
        /*
        todo - implement here
        Parser markdownParser = Parser.builder().build();
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        String textContent = renderer.render(markdownParser.parse(getTotalContent()));
        return NON_TEXT_PATTERN.matcher(textContent).replaceAll(" ");
        */
    }

    /************  Persistence ************/

}
