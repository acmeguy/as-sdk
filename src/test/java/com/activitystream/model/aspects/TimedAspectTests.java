package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

public class TimedAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(TimedAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test(groups = "unit")
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


    @Test(groups = "unit")
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

    @Test(groups = "unit")
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
