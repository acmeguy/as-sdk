package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.ASEntity;
import com.activitystream.model.entities.EntityReference;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.testng.Assert.*;

public class TrafficSourceAspectTest {

    private EntityReference entityRef = new EntityReference("Customer/1");

    @Test
    public void constructsTrafficSourceFromMap() {

        Map<String, Object> map = ImmutableMap.<String, Object>builder()
                .put(ASConstants.FIELD_TYPE, "the-type")
                .put(ASConstants.FIELD_REFERRER, "the-referrer")
                .put(ASConstants.FIELD_CAMPAIGN, "the-campaign")
                .put(ASConstants.FIELD_SOURCE, "the-source")
                .put(ASConstants.FIELD_MEDIUM, "the-medium")
                .put(ASConstants.FIELD_CONTENT, new HashMap<>(ImmutableMap.of("content-key", "content-value")))
                .put(ASConstants.FIELD_TERM, "the-term")
                .build();

        ASEntity entity = entity(map);
        assertTrue(entity.isValid());

        TrafficSourceAspect aspect = entity.getAspectManager().getTrafficSources();
        assertNotNull(aspect);
        assertEquals(aspect.size(), 1);

        TrafficSource source = aspect.get(0);
        assertEquals(source.getType(), "the-type");
        assertEquals(source.getReferrer(), "the-referrer");
        assertEquals(source.getCampaign(), "the-campaign");
        assertEquals(source.getSource(), "the-source");
        assertEquals(source.getMedium(), "the-medium");
        assertEquals(source.getTerm(), "the-term");
    }

    @Test
    public void integerTrafficSourceIsInvalid() {
        ASEntity entity = new ASEntity(ImmutableMap.of(
                ASConstants.FIELD_ENTITY_REF, entityRef,
                ASConstants.FIELD_ASPECTS, ImmutableMap.of(
                        ASConstants.ASPECTS_TRAFFIC_SOURCE, 42)));
        assertFalse(entity.isValid());
    }

    @Test
    public void constructsTrafficSourceFromList() {

        List<Map<String, Object>> list = Collections.singletonList(ImmutableMap.<String, Object>builder()
                .put(ASConstants.FIELD_TYPE, "the-type")
                .put(ASConstants.FIELD_REFERRER, "the-referrer")
                .put(ASConstants.FIELD_CAMPAIGN, "the-campaign")
                .put(ASConstants.FIELD_SOURCE, "the-source")
                .put(ASConstants.FIELD_MEDIUM, "the-medium")
                .put(ASConstants.FIELD_CONTENT, new HashMap<>(ImmutableMap.of("content-key", "content-value")))
                .put(ASConstants.FIELD_TERM, "the-term")
                .build());

        ASEntity entity = entity(list);
        assertTrue(entity.isValid());

        TrafficSourceAspect aspect = entity.getAspectManager().getTrafficSources();
        assertNotNull(aspect);
        assertEquals(aspect.size(), 1);
    }

    private ASEntity entity(Map<String, Object> trafficSource) {
        return new ASEntity(ImmutableMap.of(
                ASConstants.FIELD_ENTITY_REF, entityRef,
                ASConstants.FIELD_ASPECTS, ImmutableMap.of(
                        ASConstants.ASPECTS_TRAFFIC_SOURCE, trafficSource)));
    }

    private ASEntity entity(List<Map<String, Object>> trafficSources) {
        return new ASEntity(ImmutableMap.of(
                ASConstants.FIELD_ENTITY_REF, entityRef,
                ASConstants.FIELD_ASPECTS, ImmutableMap.of(
                        ASConstants.ASPECTS_TRAFFIC_SOURCES, trafficSources)));
    }
}
