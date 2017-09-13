package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import static com.activitystream.model.aspects.PresentationAspect.presentation;


public class MetricsAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(MetricsAspectTest.class);

    @Test
    public void testMetricsTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.addMetric("built",1941, "capacity", 5272);

        logger.warn("" + venue.toJSON());
    }

    @Test
    public void testMetricsTrackedTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983")
                .addMetric("built",1941, "capacity", 5272)
                .addOccurredAt("2017-10-31T12:00:00-01:00");

        logger.warn("" + venue.toJSON());
    }

}
