package com.activitystream.core.model.aspects;

import com.activitystream.core.model.core.AbstractMapElement;
import com.activitystream.core.model.interfaces.*;
import com.activitystream.core.model.stream.AbstractBaseEvent;
import com.activitystream.core.model.validation.AdjustedPropertyWarning;
import com.activitystream.core.model.validation.UnsupportedAspectError;
import com.activitystream.sdk.ASConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AspectManager extends AbstractMapElement
        implements EmbeddedStreamElement, CompactableElement, CanContainSubEvents, EnrichableElement {

    protected static final Logger logger = LoggerFactory.getLogger(AspectManager.class);

    public static final Map<String, AspectType> ALL_ASPECTS = new HashMap<>();

    static {
        AspectType[] aspectTypes = {PresentationAspect.ASPECT_TYPE, DimensionsAspect.ASPECT_TYPE, ClassificationAspect.ASPECT_TYPE, ABTestingAspect.ASPECT_TYPE,
                AddressAspect.ASPECT_TYPE,
                DemographyAspect.ASPECT_TYPE, ClientDeviceAspect.ASPECT_TYPE, ClientIpAspect.ASPECT_TYPE, CeiAspect.ASPECT_TYPE, ItemsManager.ASPECT_TYPE,
                GeoLocationAspect.ASPECT_TYPE,
                ContextManager.ASPECT_TYPE, ResolvableAspect.ASPECT_TYPE, LocaleAspect.ASPECT_TYPE, TrafficSourceAspect.ASPECT_TYPE,
                ObservationEventsManager.ASPECT_TYPE, TimedAspect.ASPECT_TYPE,
                ContentAspect.ASPECT_TYPE, TagsAspect.ASPECT_TYPE, SettingsAspect.ASPECT_TYPE, ProductViewAspect.ASPECT_TYPE, InventoryAspect.ASPECT_TYPE,
                MessagingAspect.ASPECT_TYPE,
                AttachmentManager.ASPECT_TYPE, MetricsAspect.ASPECT_TYPE, StatusAspect.ASPECT_TYPE, CustomerPermissionAspect.ASPECT_TYPE};
        for (AspectType type : aspectTypes) {
            ALL_ASPECTS.put(type.getAspectSignature(), type);
        }
    }

    private List<String> allowedAspects = null;

    public AspectManager() {

    }
    public AspectManager(Map map, AbstractMapElement root) {
        this(map, root, null);
    }

    public AspectManager(Map map, AbstractMapElement root, List<String> allowedAspects) {
        super(map, root);
        this.allowedAspects = allowedAspects;
    }


    /************
     * CEP Utility Functions and Getters
     ************/

    public PresentationAspect getPresentation() {
        return (PresentationAspect) get(ASConstants.ASPECTS_PRESENTATION);
    }

    public void setPresentation(PresentationAspect presentationAspect) {
        put(ASConstants.ASPECTS_PRESENTATION, presentationAspect);
    }

    public DimensionsAspect getDimensions() {
        return (DimensionsAspect) get(ASConstants.ASPECTS_DIMENSIONS);
    }

    public void setDimensions(DimensionsAspect dimensionsAspect) {
        put(ASConstants.ASPECTS_DIMENSIONS, dimensionsAspect);
    }

    public ClassificationAspect getClassification() {
        return (ClassificationAspect) get(ASConstants.ASPECTS_CLASSIFICATION);
    }

    public void setClassification(ClassificationAspect classificationAspect) {
        put(ASConstants.ASPECTS_CLASSIFICATION, classificationAspect);
    }

    public ABTestingAspect getABTesting() {
        return (ABTestingAspect) get(ASConstants.ASPECTS_AB_TEST);
    }

    public void setABTesting(ABTestingAspect abTestingAspect) {
        put(ASConstants.ASPECTS_AB_TEST, abTestingAspect);
    }

    public AddressAspect getAddress() {
        return (AddressAspect) get(ASConstants.ASPECTS_ADDRESS);
    }

    public void setAddress(AddressAspect addressAspect) {
        put(ASConstants.ASPECTS_ADDRESS, addressAspect);
    }

    public DemographyAspect getDemography() {
        return (DemographyAspect) get(ASConstants.ASPECTS_DEMOGRAPHY);
    }

    public void setDemography(DemographyAspect demographyAspect) {
        put(ASConstants.ASPECTS_DEMOGRAPHY, demographyAspect);
    }

    public ClientDeviceAspect getClientDevice() {
        return (ClientDeviceAspect) get(ASConstants.ASPECTS_CLIENT_DEVICE);
    }

    public void setClientDevice(ClientDeviceAspect clientDeviceAspect) {
        put(ASConstants.ASPECTS_CLIENT_DEVICE, clientDeviceAspect);
    }

    public ClientIpAspect getClientIp() {
        return (ClientIpAspect) get(ASConstants.ASPECTS_CLIENT_IP);
    }

    public void setClientIp(ClientIpAspect clientIpAspect) {
        put(ASConstants.ASPECTS_CLIENT_IP, clientIpAspect);
    }

    public CeiAspect getCei() {
        return (CeiAspect) get(ASConstants.ASPECTS_CEI);
    }

    public void setCei(CeiAspect ceiAspect) {
        put(ASConstants.ASPECTS_CEI, ceiAspect);
    }

    public GeoLocationAspect getGeoLocation() {
        return (GeoLocationAspect) get(ASConstants.ASPECTS_GEO_LOCATION);
    }

    public void setGeoLocation(GeoLocationAspect geoLocationAspect) {
        put(ASConstants.ASPECTS_GEO_LOCATION, geoLocationAspect);
    }

    public ResolvableAspect getResolvable() {
        return (ResolvableAspect) get(ASConstants.ASPECTS_RESOLVABLE);
    }

    public void setResolvable(ResolvableAspect resolvableAspect) {
        put(ASConstants.ASPECTS_RESOLVABLE, resolvableAspect);
    }

    public LocaleAspect getLocale() {
        return (LocaleAspect) get(ASConstants.ASPECTS_LOCALE);
    }

    public void setLocale(LocaleAspect localeAspect) {
        put(ASConstants.ASPECTS_LOCALE, localeAspect);
    }

    public TimedAspect getTimed() {
        return (TimedAspect) get(ASConstants.ASPECTS_TIMED);
    }

    public void setTimed(TimedAspect timedAspect) {
        put(ASConstants.ASPECTS_TIMED, timedAspect);
    }

    public ContentAspect getContent() {
        return (ContentAspect) get(ASConstants.ASPECTS_CONTENT);
    }

    public void getContent(ContentAspect contentAspect) {
        put(ASConstants.ASPECTS_CONTENT, contentAspect);
    }

    public TagsAspect getTags() {
        return (TagsAspect) get(ASConstants.ASPECTS_TAGS);
    }

    public void setTags(TagsAspect tagsAspect) {
        put(ASConstants.ASPECTS_TAGS, tagsAspect);
    }

    public SettingsAspect getSettings() {
        return (SettingsAspect) get(ASConstants.ASPECTS_SETTINGS);
    }

    public void setSettings(SettingsAspect settingsAspect) {
        put(ASConstants.ASPECTS_SETTINGS, settingsAspect);
    }

    public StatusAspect getStatus() {
        return (StatusAspect) get(ASConstants.ASPECTS_STATUS);
    }

    public void setStatus(StatusAspect statusAspect) {
        put(ASConstants.ASPECTS_STATUS, statusAspect);
    }

    public ProductViewAspect getProductView() {
        return (ProductViewAspect) get(ASConstants.ASPECTS_PRODUCT_VIEW);
    }

    public void getProductView(ProductViewAspect productViewAspect) {
        put(ASConstants.ASPECTS_PRODUCT_VIEW, productViewAspect);
    }

    public TrafficSourceAspect getTrafficSources() {
        return (TrafficSourceAspect) get(ASConstants.ASPECTS_TRAFFIC_SOURCES);
    }

    public void setTrafficSources(TrafficSourceAspect trafficSourceAspect) {
        put(ASConstants.ASPECTS_TRAFFIC_SOURCES, trafficSourceAspect);
    }

    public InventoryAspect getInventory() {
        return (InventoryAspect) get(ASConstants.ASPECTS_INVENTORY);
    }

    public void setInventory(InventoryAspect inventoryAspect) {
        put(ASConstants.ASPECTS_INVENTORY, inventoryAspect);
    }

    public ItemsManager getItems() {
        return (ItemsManager) get(ASConstants.ASPECTS_ITEMS);
    }

    public ObservationEventsManager getObservationItems() {
        return (ObservationEventsManager) get(ASConstants.ASPECTS_OBS_EVENTS);
    }

    public InventoryAspect getOrCreateInventoryAspect() {
        return (InventoryAspect) getOrCreateAspect(ASConstants.ASPECTS_INVENTORY);
    }

    public MetricsAspect getMetrics() {
        return (MetricsAspect) get(ASConstants.ASPECTS_METRICS);
    }

    public MessagingAspect getMessaging() {
        return (MessagingAspect) get(ASConstants.ASPECTS_MESSAGING);
    }

    public ContextManager getContext() {
        return (ContextManager) get(ASConstants.ASPECTS_CONTEXT);
    }

    public Integer getCount() {
        return size();
    }

    public List<String> getActiveAspects() {
        return new LinkedList<>(keySet());
    }

    /************ Enrichment ************/

    @Override
    public void simplify() {
        Iterator<?> aspectIterator = values().iterator();
        while (aspectIterator.hasNext()) {
            AspectInterface aspect = (AspectInterface) aspectIterator.next();
            aspect.simplify();

            if (aspect.isEmpty())
                aspectIterator.remove();
        }
    }

    private void removeEmptyAutoGenerated() {
        values().stream().filter(aspect -> ((AbstractMapAspect) aspect).isAutoGenerated() && ((AbstractMapAspect) aspect).isEmpty()).forEach(aspect -> {
            this.remove(((AbstractMapAspect) aspect).getAspectType().getAspectSignature());
        });
    }

    private void checkAspectCreationForEnrichmentPurposes(BaseStreamElement root, Collection<String> allowedEnrichments) {

        for (Map.Entry<String, AspectType> entry : ALL_ASPECTS.entrySet()) {

            if (containsKey(entry.getKey())) continue; //Already exists

            //Todo - this needs to be optimized
            //if (allowedEnrichments != null && !allowedEnrichments.contains(entry.getValue().getSimpleName())) continue; //Does not need to be checked

            AspectType aspectType = entry.getValue();

            try {
                if (aspectType.shouldCreateForEnrichment(root)) {
                    AspectInterface newAspect = aspectType.newInstance();
                    newAspect.setRoot(this.root);
                    newAspect.markAsAutoGenerated();
                    putAspect(newAspect);
                }
            } catch (Exception e) {
                logger.error("Failed to create aspect for enrichment: {}", entry.getKey(), e);
            }
        }

    }

    /************  Utility functions  ************/

    @Override
    public void compact() {

    }

    @Override
    public List<AbstractBaseEvent> getSubEvents() {
        List<AbstractBaseEvent> subItems = new LinkedList<>();
        for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) this.entrySet()) {
            if (entry.getValue() instanceof CanContainSubEvents) {
                subItems.addAll(((CanContainSubEvents) entry.getValue()).getSubEvents());
            }
        }
        return subItems;
    }

    /************  Access ************/

    public PresentationAspect getOrCreatePresentationAspect() {
        return (PresentationAspect) getOrCreateAspect(ASConstants.ASPECTS_PRESENTATION);
    }

    public ContentAspect getOrCreateContentAspect() {
        return (ContentAspect) getOrCreateAspect(ASConstants.ASPECTS_CONTENT);
    }

    public StatusAspect getOrCreateStatusAspect() {
        return (StatusAspect) getOrCreateAspect(ASConstants.ASPECTS_STATUS);
    }

    public AspectInterface getAspectByType(String aspectType) {
        return (AspectInterface) get(aspectType);
    }

    public AspectInterface getOrCreateAspect(String aspectType) {
        return (AspectInterface) computeIfAbsent(aspectType, type -> {
            AspectInterface aspect = ALL_ASPECTS.get(type).newInstance();
            aspect.setRoot(getRoot());
            /*
            todo - re-implement here
            if (getRoot() != null && getRoot().getRootElement() != null) {
                aspect.loadFromElement(getRoot().getRootElement(), Collections.emptyList(), StreamItemAccessPolicy.FULL_ACCESS);
            }
            */
            return aspect;
        });
    }

    /************  Data Assignment and Validation ************/

    public void putAspect(AspectInterface aspect) {
        put(aspect.getAspectType().getAspectSignature(), aspect);
    }

    @Override
    public Object put(Object key, Object value) {

        String theKey = key.toString();
        String theLCKey = theKey.toLowerCase();
        if (!theKey.equals(theLCKey)) {
            this.addProblem(new AdjustedPropertyWarning("Aspect name '" + theKey + "'converted to lower case"));
            theKey = theLCKey;
        }

        //populate with ready aspect and return
        if (value instanceof AspectInterface) {
            ((AspectInterface) value).setRoot(root);
            return super.put(theKey, value);
        }

        if (this.allowedAspects != null && !this.allowedAspects.contains(theKey)) {
            //logger.warn("Aspect not allowed: "  + theKey ); //+ " " + this.allowedAspects + " on " + this.root);
            this.addProblem(new UnsupportedAspectError("The '" + theKey + "' aspect is not valid or not allowed here"));
            return null;
        }

        switch (theKey) {
            case "location":
                theKey = ASConstants.ASPECTS_ADDRESS;
                break;
            case "geolocation":
                theKey = ASConstants.ASPECTS_GEO_LOCATION;
                break;
            case ASConstants.ASPECTS_TRAFFIC_SOURCE:
                theKey = ASConstants.ASPECTS_TRAFFIC_SOURCES;
                break;
        }

        AspectType aspectType = ALL_ASPECTS.get(theKey);
        if (aspectType == null) {
            logger.error("The '" + theKey + "' Aspect is not valid");
            this.addProblem(new UnsupportedAspectError("The " + theKey + " Aspect is not valid"));
        } else {
            value = aspectType.fromValue(value, root);
        }

        if (value == null || (value instanceof Map && ((Map) value).size() == 0)) return null;

        return super.put(theKey, value);
    }

    @Override
    public void verify() {
        for (Object aspectValue : values()) {
            ((BaseStreamElement) aspectValue).verify();
        }
    }

    public AspectManager mergeAspects(AspectManager other) {
        other.forEach((key, value) -> {
            AspectInterface current = (AspectInterface) get(key);
            if (current == null) {
                put(key, value);
            } else {
                current.mergeAspect((AspectInterface) value);
            }
        });
        return this;
    }

}
