package com.activitystream.core.sdk.aspects;

import com.activitystream.sdk.ASEvent;
import com.activitystream.sdk.ASService;
import com.activitystream.core.model.relations.ASEventRelationTypes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.core.model.aspects.CeiAspect.cei;

public class CeiAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(CeiAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleTagsAspectTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        //Minimum valid Message
        ASEvent asEvent = new ASEvent()
                .withType("as.something.good")
                .withOrigin("your.business")
                .withOccurredAt("2017-01-01T00:00:00.000Z")
                .withRelationIfValid(ASEventRelationTypes.ACTOR, "Customer","314")
                .withRelationIfValid(ASEventRelationTypes.AFFECTS, "Product","plu3983")
                .withAspect(cei()
                        .withCare(0)
                        .withIntent(2)
                        .withEngagement(2)
                        .withRating(2)
                        .withHappiness(2)
                );

        //logger.warn("asEvent " + asEvent.toJSON());

        Assert.assertEquals(asEvent.toJSON().equals("{\"type\":\"as.something.good\",\"origin\":\"your.business\",\"occurred_at\":\"2017-01-01T00:00:00.000Z\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/314\"}},{\"AFFECTS\":{\"entity_ref\":\"Product/plu3983\"}}],\"aspects\":{\"cei\":{\"care\":0.0,\"intent\":2.0,\"engagement\":2.0,\"rating\":2.0,\"happiness\":2.0}}}"),true);

        ASEvent parsedEvent = ASEvent.fromJSON(asEvent.toJSON());
        //Round-trip test
        Assert.assertEquals(asEvent.toJSON().equals(parsedEvent.toJSON()),true);
        Assert.assertEquals(asEvent.getStreamId().equals(parsedEvent.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(asEvent.getStreamId().toString().equals("e774f6c4-704d-3fa7-bd0f-c559eda12cbf"),true);

    }

}
