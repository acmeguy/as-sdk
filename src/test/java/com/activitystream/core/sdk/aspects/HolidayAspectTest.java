package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.HolidayAspect;
import org.junit.Test;
import org.testng.Assert;

public class HolidayAspectTest {
    @Test
    public void testHolidayAspect() {
        HolidayAspect holidayAspect = new HolidayAspect()
                .withHolidayDate("13-04-2018")
                .withHolidayName("Dragan's birthday")
                .withPublicHoliday()
                .withOffsetDays(-46);

        Assert.assertEquals(holidayAspect.toString(), "{date=13-04-2018, name=Dragan's birthday, is_public=true, offset_days=-46}");

        Assert.assertEquals(holidayAspect.getHolidayDate(), "13-04-2018");

        Assert.assertEquals(holidayAspect.getHolidayName(), "Dragan's birthday");

        Assert.assertTrue(holidayAspect.isPublicHoliday());

        Assert.assertEquals(holidayAspect.getOffsetDays(), new Integer(-46));
    }
}
