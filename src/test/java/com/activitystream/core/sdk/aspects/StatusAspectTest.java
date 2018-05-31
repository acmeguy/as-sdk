package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.StatusAspect;
import org.joda.time.DateTime;
import org.junit.Test;
import org.testng.Assert;

public class StatusAspectTest {
    @Test
    public void testStatusAspect() {
        StatusAspect statusAspect = new StatusAspect()
                .withRegisteredAt(DateTime.parse("2018-04-07T17:58:59.283Z"))
                .withVersion(3)
                .withUpdateOccurredAt(DateTime.parse("2018-04-07T17:58:59.283Z"))
                .withCancelled(true)
                .withDeleted();

        Assert.assertEquals(statusAspect.toString(),"{registered_at=2018-04-07T17:58:59.283Z, version=3, update_occurred_at=2018-04-07T17:58:59.283Z, cancelled=true, is_deleted=true}");
    }
}
