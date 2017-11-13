package com.activitystream.core.model.stream;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.internal.TypeReference;
import com.fasterxml.jackson.annotation.JsonValue;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventTypeReference implements TypeReference<EventTypeReference> {

    public enum Category {
        OBSERVATION("as.oi", true, "ob", ASConstants.AS_OBSERVATION),
        TRANSACTION_EVENT("as.commerce.transaction", true, "tx", ASConstants.AS_TRANSACTION_EVENT),
        CUSTOMER_EVENT(null, false, "et", ASConstants.AS_CUSTOMER_EVENT) {
            @Override
            boolean matchesExclusively(String eventTypeString) {
                return !OBSERVATION.matches(eventTypeString) && !TRANSACTION_EVENT.matches(eventTypeString);
            }

            @Override
            public boolean matches(String eventTypeString) {
                return !OBSERVATION.matchesExclusively(eventTypeString) && !TRANSACTION_EVENT.matchesExclusively(eventTypeString);
            }
        };

        private String branchPoint;
        private boolean internal;
        private String vertexTypePrefix;
        private String rootVertexTypeName;

        Category(String branchPoint, boolean internal, String vertexTypePrefix, String rootVertexTypeName) {
            this.branchPoint = branchPoint;
            this.internal = internal;
            this.vertexTypePrefix = vertexTypePrefix;
            this.rootVertexTypeName = rootVertexTypeName;
        }

        boolean matchesExclusively(String eventTypeString) {
            return eventTypeString.equals(branchPoint) || eventTypeString.startsWith(branchPoint + ".");
        }

        boolean matches(String eventTypeString) {
            return matchesExclusively(eventTypeString) || branchPoint.startsWith(eventTypeString + ".");
        }

        boolean isInternal() {
            return internal;
        }

        String getVertexTypePrefix() {
            return vertexTypePrefix;
        }

        public boolean matchesVertexTypeName(String vertexTypeName) {
            return vertexTypeName.startsWith(vertexTypePrefix);
        }

        String getRootVertexTypeName() {
            return rootVertexTypeName;
        }
    }

    private static final Map<Category, Map<String, EventTypeReference>> knownTypes = new EnumMap<>(Category.class);

    static {
        Arrays.stream(Category.values()).forEach(cat -> knownTypes.put(cat, new ConcurrentHashMap<>()));
    }

    public static final String[] ALL_ROOT_VERTEX_TYPE_NAMES = Arrays.stream(Category.values())
            .map(Category::getRootVertexTypeName)
            .toArray(String[]::new);

    private Category category;
    private String eventTypeString;

    private EventTypeReference(Category category, String eventTypeString) {
        this.category = category;
        this.eventTypeString = eventTypeString;
    }

    public static EventTypeReference resolveTypesString(String eventTypeString) {
        Category category = Arrays.stream(Category.values())
                .filter(cat -> cat.matchesExclusively(eventTypeString))
                .findFirst().orElse(Category.CUSTOMER_EVENT);
        return resolveTypesString(category, eventTypeString);
    }

    public static EventTypeReference resolveTypesString(String eventTypeString, String vertexTypeName) {
        Category category = Arrays.stream(Category.values())
                .filter(cat -> cat.matchesVertexTypeName(vertexTypeName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("'" + vertexTypeName + "' is not a valid event type vertex class"));
        return resolveTypesString(category, eventTypeString);
    }

    public static EventTypeReference resolveTypesString(Category category, String eventTypeString) {
        return knownTypes.get(category).computeIfAbsent(StringUtils.defaultIfEmpty(eventTypeString, ""), (str) -> new EventTypeReference(category, str));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventTypeReference && eventTypeString.equals(((EventTypeReference) obj).eventTypeString);
    }

    @Override
    public int hashCode() {
        return eventTypeString.hashCode();
    }

    @JsonValue
    @Override
    public String toString() {
        return eventTypeString;
    }

    @Override
    public int compareTo(EventTypeReference o) {
        return eventTypeString.compareTo(o.eventTypeString);
    }

    @Override
    public String getVertexTypeName() {
        return Arrays.stream(eventTypeString.split("\\."))
                .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).replace("-", "").toLowerCase())
                .collect(Collectors.joining("", category.getVertexTypePrefix(), ""));
    }

    /**
     * Creates an SQL where clause fragment matching this type.
     */
    public String asSqlConstraint() {
        return "(@class like '" + getVertexTypeName() + "')";
    }

    @Override
    public String getRootVertexTypeName() {
        return category.getRootVertexTypeName();
    }

    @Override
    public EventTypeReference getSuperType() {
        int lastDot = eventTypeString.lastIndexOf('.');
        if (lastDot >= 0)
            return resolveTypesString(category, eventTypeString.substring(0, lastDot));
        return null;
    }

    @Override
    public boolean hasSuperType() {
        return eventTypeString.lastIndexOf('.') >= 0;
    }

    @Override
    public boolean isInternal() {
        return category.isInternal();
    }

    @Override
    public EntityReference asEntityReference() {
        return new EntityReference(ASConstants.AS_EVENT_TYPE, eventTypeString);
    }

    @Override
    public boolean isValidType() {
        return StringUtils.isNotEmpty(eventTypeString) && category.matches(eventTypeString);
    }

    public boolean isValidLeafType() {
        // Don't allow creating events unless the type string maps unambiguously to a single vertex type.
        return StringUtils.isNotEmpty(eventTypeString) && category.matchesExclusively(eventTypeString);
    }
}
