package com.activitystream.core.sdk.aspects;

import com.activitystream.sdk.ASConstants;
import com.activitystream.sdk.ASEvent;
import com.activitystream.sdk.ASService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.core.model.aspects.ResolvableAspect.resolvable;

public class ResolvableAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ResolvableAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleResolvableAspectTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent miniEvent = new ASEvent(ASEvent.PRE.AS_MARKETING_AD_INTERACTION, "www.web");
        miniEvent.withOccurredAt("2017-01-01T12:00:00")
                .withRelationIfValid(ASConstants.REL_ACTOR,"Customer/983938")
                .withAspect(resolvable("3983"));

        Assert.assertEquals(miniEvent.toJSON().equals("{\"occurred_at\":\"2017-01-01T12:00:00.000Z\",\"type\":\"as.marketing.ad.interaction\",\"origin\":\"www.web\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/983938\"}}],\"aspects\":{\"resolvable\":{\"external_id\":\"3983\"}}}"),true);

        ASEvent parsedASEvent = ASEvent.fromJSON(miniEvent.toJSON());
        //Round-trip test
        Assert.assertEquals(miniEvent.toJSON().equals(parsedASEvent.toJSON()),true);
        Assert.assertEquals(miniEvent.getStreamId().equals(parsedASEvent.getStreamId()),true);
        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(miniEvent.getStreamId().toString().equals("05e7daf0-4e6a-317f-92de-c5e7e8a4815e"),true);

    }

}
