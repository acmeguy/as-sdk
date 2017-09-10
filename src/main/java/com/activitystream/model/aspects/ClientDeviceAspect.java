package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.analytics.TimeSeriesEntry;
import com.activitystream.model.entities.BusinessEntity;
import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.interfaces.*;
import com.activitystream.model.utils.StreamIdUtils;
import com.activitystream.model.validation.AdjustedPropertyWarning;
import com.activitystream.model.validation.IgnoredPropertyError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.function.Consumer;

public class ClientDeviceAspect extends AbstractMapAspect implements CompactableElement, LinkedElement, EnrichableElement, AnalyticsElement {

    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_CLIENT_DEVICE, ClientDeviceAspect::new) {
    };

    protected static final Logger logger = LoggerFactory.getLogger(ClientDeviceAspect.class);

    public ClientDeviceAspect() {
    }

    public ClientDeviceAspect(String userAgent, BaseStreamElement root) {
        setRoot(root);
        put(ASConstants.FIELD_USER_AGENT, userAgent);
    }

    @Override
    public void loadFromValue(Object m) {
        if (m instanceof String) put(ASConstants.FIELD_USER_AGENT, m);
        else super.loadFromValue(m);
    }

    /************
     * Utility functions
     ************/

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

    //todo create enrichment getters

    /************
     * CEP Utility Functions and Getters
     ************/


    public String getUserAgent() {
        return (String) get(ASConstants.FIELD_USER_AGENT);
    }

    @Override
    public void compact() {
    }

    private String getUserAgentSignature() {
        return getUserAgent().replaceAll("/", "--").replaceAll(",", "").replaceAll(";", "").replaceAll("\\.", "_").replaceAll("\\(", "[").replaceAll("\\)", "]")
                .replaceAll("  ", " ").replaceAll(" ", "_");
    }

    private EntityReference getUserAgentEntityRef() {
        return new EntityReference(ASConstants.AS_CLIENT_DEVICE, StreamIdUtils.calculateStreamId(getUserAgentSignature()).toString());
    }

    @Override
    public void onEachEntityReference(Consumer<EntityReference> action) {
        if (containsKey(ASConstants.FIELD_USER_AGENT)) {
            EntityReference agentReference = getUserAgentEntityRef();
            agentReference.setDefaults(new HashMap<String, Object>() {{
                put("signature", getUserAgent());
            }});
            action.accept(agentReference);
        }
    }

    @Override
    public void onEachRelationType(StreamItemRelationTypeConsumer action) {
        if (getRoot() instanceof BusinessEntity)
            action.accept(ASConstants.REL_USES_DEVICE, ((BusinessEntity) getRoot()).getElementType(), ASConstants.AS_CLIENT_DEVICE);
    }

    /************ Enrichment & Analytics ************/

    @Override
    public void addTimeSeriesDimensions(TimeSeriesEntry entry) {
        final String[] fields =
                new String[]{ASConstants.FIELD_USER_AGENT, "browser", "browser_vendor", "browser_version", "device", "device_vendor", "os", "os_version"};

        if (entry.getTimeSeriesType().equals("pageviews") || entry.getTimeSeriesType().equals(ASConstants.TIME_SERIES_EVENTS)) {
            Map<String, Object> device = new LinkedHashMap<>();
            device.put(ASConstants.FIELD_USER_AGENT, this.get(ASConstants.FIELD_USER_AGENT));
            for (String field : fields) {
                if (containsKey("_" + field) && get("_" + field) != null) device.put(field, get("_" + field));
            }
            entry.put("client_device", device);
        }
    }

    @Override
    public void populateTimeSeriesEntry(TimeSeriesEntry entry, String context, long depth) {
        throw new NotImplementedException();
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
            case ASConstants.FIELD_USER_AGENT:
            case "bind_to":
            case ASConstants.FIELD_TRACK_FOR:
                break;
            default:
                if (!theKey.startsWith("_")) {
                    this.addProblem(new IgnoredPropertyError("The " + theKey + " property is not supported for the Client Device Aspect"));
                    return null;
                }
        }
        return super.put(theKey, value);
    }

    @Override
    public void verify() {

    }
}
