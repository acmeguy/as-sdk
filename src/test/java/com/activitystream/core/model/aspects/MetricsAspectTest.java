package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.ASService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.testng.Assert;

import java.util.TimeZone;


public class MetricsAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(MetricsAspectTest.class);

    @Test
    public void testMetricsTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.withMetrics("built",1941, "capacity", 5272);

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"metrics\":{\"built\":1941.0,\"capacity\":5272.0}}}"),true);
    }

    @Test
    public void testMetricsTrackedTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));
        ASEntity venue = new ASEntity("Venue", "983983")
                .withMetrics("built",1941, "capacity", 5272)
                .withOccurredAt("2017-10-31T12:00:00-01:00");

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"metrics\":{\"built\":1941.0,\"capacity\":5272.0}},\"occurred_at\":\"2017-10-31T12:00:00.000-01:00\"}"),true);

        ASEntity parsedVenue = ASEntity.fromJSON(venue.toJSON());
        //Round-trip test
        Assert.assertEquals(venue.toJSON().equals(parsedVenue.toJSON()),true);
        Assert.assertEquals(venue.getStreamId().equals(parsedVenue.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(venue.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"),true);

    }

}
