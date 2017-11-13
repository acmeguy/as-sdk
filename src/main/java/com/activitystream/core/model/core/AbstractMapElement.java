package com.activitystream.core.model.core;

import com.activitystream.sdk.ASConstants;
import com.activitystream.core.model.validation.MessageValidator;
import com.activitystream.core.model.aspects.*;
import com.activitystream.core.model.entities.BusinessEntity;
import com.activitystream.core.model.interfaces.*;
import com.activitystream.core.model.security.SecurityScope;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractMapElement extends LinkedHashMap implements BaseStreamElement {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMapElement.class);

    public BaseStreamElement root = null;
    private MessageValidator messageValidator;
    protected SecurityScope securityScope = null;
    boolean isNew = false;

    public AbstractMapElement() {
    }

    public AbstractMapElement(Map values, BaseStreamElement root) {
        this.root = root;
        if (values != null) setMapValues(values);
    }

    /************  Utility Functions  ************/

    @Override
    public void setSecurityScope(SecurityScope scope) {
        this.securityScope = scope;
    }

    public void setMapValues(Map<Object, Object> values) {
        if (values != null) {
            values.forEach((key, value) -> {
                if (!key.equals("relations")) put(key, value);
            });
            //Load the relations after everything else
            values.forEach((key, value) -> {
                if (key.equals("relations")) put(key, value);
            });
            verify();
        }
    }

    @Override
    public boolean traverse(ElementVisitor visitor) {
        if (!visitor.visit(this))
            return false;

        for (Object entry : values()) {
            if (entry instanceof LinkedElement) {
                if (!((LinkedElement) entry).traverse(visitor))
                    return false;
            }
        }

        return true;
    }

    /**
     * Calculate a footprint for the Stream Item
     * The Footprint is used to calculate a unique Stream ID
     */
    public String getFootprint() {
        //Todo - this should be overwritten
        return null;
    }

    /************ Validation and Error Handling ************/

    @Override
    public void verify() {
        validator().markAsVerified();
    }

    @Override
    public boolean isValid() {
        if (validator().getAllMessageExceptions() == null) this.verify();
        if (validator().hasErrors() > 0) super.put("_warnings", validator().getAllMessageExceptions());
        return validator().hasErrors() < 1;
    }

    public boolean isValid(boolean rerun) {
        if (rerun) {
            validator().reset();
            remove("_warnings");
        }
        return isValid();
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

    /************  Access ************/

    protected CharSequence getCharSequence(String property) {
        return StringUtils.defaultString((String) get(property));
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

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
        BaseStreamElement topMost = (root != null) ? root : this;
        while (topMost != null) {
            if (topMost instanceof BusinessEntity) {
                topMostEntity = (BusinessEntity) topMost;
            }
            topMost = topMost.getRoot();
        }
        return topMostEntity;
    }

    /************  Persistence ************/

    /*
    public SavableElement save(Tenant tenant, PersistenceCache cache) {
        return null;
    }
    */

    protected Map getMapChanges(Map previousAspectMap, Map newAspectMap) {

        if (previousAspectMap != null && !previousAspectMap.isEmpty()) {

            Set<String> removedKeys = new LinkedHashSet<>(previousAspectMap.keySet());
            removedKeys.removeAll(newAspectMap.keySet());

            Set<String> addedKeys = new LinkedHashSet<>(newAspectMap.keySet());
            addedKeys.removeAll(previousAspectMap.keySet());

            Set<Map.Entry<String, Object>> changedEntries = new HashSet<>(newAspectMap.entrySet());
            changedEntries.removeAll(previousAspectMap.entrySet());

            if (!addedKeys.isEmpty() || !removedKeys.isEmpty() || !changedEntries.isEmpty()) {
                return new LinkedHashMap() {{
                    put("added", addedKeys);
                    put("removed", removedKeys);
                    put("changed", changedEntries);
                    put("was", previousAspectMap.entrySet());
                }};
            }

        } else if (newAspectMap != null && !newAspectMap.isEmpty()) { //Everything is new
            return new LinkedHashMap() {{
                put("added", newAspectMap.keySet());
                put("was", null);
            }};
        }

        return null;

    }

    /************  Utility Functions ************/

    public boolean hasAspects() {
        return containsKey(ASConstants.FIELD_ASPECTS);
    }

    public AspectManager getAspectManager(boolean store, AbstractMapElement root) {
        AspectManager aspectManager = (AspectManager) get(ASConstants.FIELD_ASPECTS);
        if (aspectManager == null) {
            aspectManager = new AspectManager((Map) null, root);
            if (store) putIfAbsent(ASConstants.FIELD_ASPECTS, aspectManager);
        }
        return aspectManager;
    }

    public AspectManager getAspectManager(boolean store) {
        return getAspectManager(store, this);
    }

    public AspectManager getAspectManager() {
        return getAspectManager(false, null);
    }

    protected Object withMetrics(Map<String, Double> metricsMap, AbstractMapElement root) {
        metricsMap.forEach((metric, value) -> withMetric(metric, value, root));
        return this;
    }

    protected Object withMetrics(AbstractMapElement root, Object... metrics) {
        for (int i = 0; i < metrics.length; i = i + 2) {
            if (metrics[i + 1] == null) continue;
            withMetric((String) metrics[i], ((Number) metrics[i + 1]).doubleValue(), root);
        }
        return this;
    }

    protected Object withMetric(String metric, Number value, AbstractMapElement root) {
        if (value == null) return this;

        MetricsAspect metricAspect = getAspectManager(true, root).getMetrics();
        if (metricAspect == null) {
            metricAspect = new MetricsAspect() {{
                put(metric, value.doubleValue());
            }};
            getAspectManager().put(ASConstants.ASPECTS_METRICS, metricAspect);
        } else {
            metricAspect.put(metric, value.doubleValue());
        }
        return this;
    }

    protected Object addDimensions(Map<String, Object> dimensionsMap, AbstractMapElement root) {
        dimensionsMap.forEach((dimension, value) -> {
            if (value instanceof String) addDimension(dimension, (String) value, root);
        });
        return this;
    }

    protected Object addDimensions(AbstractMapElement root, String... dimensions) {
        for (int i = 0; i < dimensions.length; i = i + 2) {
            if (dimensions[i + 1] != null && !dimensions[i + 1].isEmpty()) addDimension(dimensions[i], dimensions[i + 1], root);
        }
        return this;
    }

    protected Object addDimension(String dimension, String value, AbstractMapElement root) {
        if (value != null && !value.isEmpty()) {
            DimensionsAspect dimensionsAspect = getAspectManager(true, root).getDimensions();
            if (dimensionsAspect == null) {
                dimensionsAspect = new DimensionsAspect() {{
                    put(dimension, value);
                }};
                getAspectManager().put(ASConstants.ASPECTS_DIMENSIONS, dimensionsAspect);
            } else {
                dimensionsAspect.put(dimension, value);
            }
        }
        return this;
    }

    protected Object addAspect(AspectInterface aspect, AbstractMapElement root) {
        AspectManager aspectManager = getAspectManager(true, root);
        aspect.setRoot(root);
        aspectManager.putAspect(aspect);
        return this;
    }

    protected Object addPresentation(String label, String thumbnail, String icon, String description, String detailsUrl, AbstractMapElement root) {
        PresentationAspect presentationAspect = getAspectManager(true, root).getPresentation();
        if (presentationAspect == null) {
            presentationAspect = new PresentationAspect(label, thumbnail, icon, description, detailsUrl, null);
            getAspectManager().put(ASConstants.ASPECTS_PRESENTATION, presentationAspect);
        } else {
            presentationAspect.put(ASConstants.FIELD_LABEL, label);
            presentationAspect.put(ASConstants.FIELD_THUMBNAIL, thumbnail);
            presentationAspect.put(ASConstants.FIELD_ICON, icon);
            presentationAspect.put(ASConstants.FIELD_DESCRIPTION, description);
            presentationAspect.put(ASConstants.FIELD_DETAILS_URL, detailsUrl);
        }
        return this;
    }

    public Object withProperties(Object... properties) {
        for (int i = 0; i < properties.length; i = i + 2) withProperties((String) properties[i], properties[i + 1]);
        return this;
    }

    public Object withProperties(String property, Object value) {
        if (value != null) {
            Map<String, Object> properties = getProperties();
            if (properties == null) {
                properties = new LinkedHashMap<String,Object>() {{
                    put(property, value);
                }};
                put(ASConstants.FIELD_PROPERTIES, properties);
            } else {
                properties.put(property, value);
            }
        }
        return this;
    }

    public Map<String, Object> getProperties() {
        return (Map<String, Object>) get(ASConstants.FIELD_PROPERTIES);
    }
    public Map<CharSequence, CharSequence> getPropertiesWithJsonValues() {
        return null; //todo - impliment with serializer //(Map<String, Object>) get(ASConstants.FIELD_PROPERTIES);
    }

    public Map<String, Object> getProperties(boolean create) {
        Map<String, Object> properties = (Map<String, Object>) get(ASConstants.FIELD_PROPERTIES);
        if (create && properties == null) {
            properties = new LinkedHashMap<String, Object>();
            put(ASConstants.FIELD_PROPERTIES, properties);
        }
        return properties;
    }


}
