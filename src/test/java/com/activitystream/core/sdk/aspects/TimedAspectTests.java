package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.TimeComponent;
import com.activitystream.core.model.aspects.TimedAspect;
import com.activitystream.sdk.ASConstants;
import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.ASService;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.junit.Test;

import java.util.TimeZone;

import static com.activitystream.core.model.aspects.TimedAspect.timed;
import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

public class TimedAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(TimedAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleTimedAspectTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.withAspect(timed()
                .withPeriod("Construction","1867","1871")
                .withPeriod("Inaugurated","1871-03-29")
                .withPeriod("Renovated","1996","2004")
        );

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"timed\":{\"construction\":{\"begins\":\"1867-01-01T00:00:00.000Z\",\"ends\":\"1871-01-01T00:00:00.000Z\",\"duration\":126230400000},\"inaugurated\":{\"begins\":\"1871-03-29T00:00:00.000Z\"},\"renovated\":{\"begins\":\"1996-01-01T00:00:00.000Z\",\"ends\":\"2004-01-01T00:00:00.000Z\",\"duration\":252460800000}}}}"),true);

        ASEntity parsedVenue = ASEntity.fromJSON(venue.toJSON());
        //Round-trip test
        Assert.assertEquals(venue.toJSON().equals(parsedVenue.toJSON()),true);
        Assert.assertEquals(venue.getStreamId().equals(parsedVenue.getStreamId()),true);
        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(venue.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"),true);

    }

    @Test
    public void testTimedAspectConstruction() throws Exception {

        TimedAspect celebrationPeriod = new TimedAspect("took", "2017-10-31", "2017-11-01");
        Assert.assertNotNull(celebrationPeriod);
        Assert.assertEquals(celebrationPeriod.getBegins().toString().equals("2017-10-31T00:00:00.000Z"), true);
        Assert.assertEquals(celebrationPeriod.getEnds().toString().equals("2017-11-01T00:00:00.000Z"), true);

        celebrationPeriod = new TimedAspect("took", "2017-10-31", "2017-11-01T13:00");
        Assert.assertNotNull(celebrationPeriod);
        Assert.assertEquals(celebrationPeriod.getBegins().toString().equals("2017-10-31T00:00:00.000Z"), true);
        Assert.assertEquals(celebrationPeriod.getEnds().toString().equals("2017-11-01T13:00:00.000Z"), true);

        celebrationPeriod = new TimedAspect("took", "2017-10-31", "2017-11-01 13:30:32.123");
        Assert.assertNotNull(celebrationPeriod);
        Assert.assertEquals(celebrationPeriod.getBegins().toString().equals("2017-10-31T00:00:00.000Z"), true);
        Assert.assertEquals(celebrationPeriod.getEnds().toString().equals("2017-11-01T13:30:32.123Z"), true);

    }


    @Test
    public void constructsAspectFromBackwardsCompatibleMap() throws Exception {

        DateTimeZone timeZone = DateTimeZone.forOffsetHours(2);
        DateTime begins = new DateTime(2017, 5, 18, 16, 33, 29, timeZone);
        DateTime ends = new DateTime(2017, 5, 18, 16, 33, 59, timeZone);

        TimedAspect timed = new TimedAspect();
        timed.loadFromValue(ImmutableMap.of(
                ASConstants.FIELD_TYPE, "the-type",
                ASConstants.FIELD_BEGINS, begins,
                ASConstants.FIELD_ENDS, ends));

        TimeComponent component = timed.getComponent("the-type");

        assertNotNull(component);
        assertEquals(component.getBegins(), begins);
        assertEquals(component.getBegins().getZone(), timeZone);
        assertEquals(component.getEnds(), ends);
        assertEquals(component.getEnds().getZone(), timeZone);
        assertEquals(component.getDuration(), Long.valueOf(30000L));
    }

    @Test
    public void providesBackwardsCompatibilityAccessors() throws Exception {

        DateTimeZone timeZone = DateTimeZone.forOffsetHours(2);
        DateTime begins = new DateTime(2017, 5, 18, 16, 36, 19, timeZone);
        DateTime ends = new DateTime(2017, 5, 18, 16, 36, 39, timeZone);

        TimedAspect timed = new TimedAspect();
        timed.loadFromValue(ImmutableMap.of(
                "the-type", ImmutableMap.of(
                        ASConstants.FIELD_BEGINS, begins,
                        ASConstants.FIELD_ENDS, ends)));

        assertEquals(timed.getType(), "the-type");
        assertEquals(timed.getBegins(), begins);
        assertEquals(timed.getBegins().getZone(), timeZone);
        assertEquals(timed.getEnds(), ends);
        assertEquals(timed.getEnds().getZone(), timeZone);
        assertEquals(timed.getDuration(), Long.valueOf(20000L));
    }

}
