package com.activitystream.model.aspects;

import com.activitystream.model.ASEvent;
import com.activitystream.model.config.ASService;
import com.activitystream.model.relations.ASEventRelationTypes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.TimeZone;

import static com.activitystream.model.aspects.ABTestingAspect.abTest;


public class ABAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ABAspectTests.class);

    @Test
    public void testSimpleABTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        //Minimum valid Message
        ASEvent asEvent = new ASEvent()
                .withType("as.something.good")
                .withOrigin("your.business")
                .withOccurredAt("2017-01-01T00:00:00.000Z")
                .withRelationIfValid(ASEventRelationTypes.ACTOR, "Customer","314")
                .withRelationIfValid(ASEventRelationTypes.AFFECTS, "Product","plu3983")
                .withAspect(abTest()
                        .withTestId("92982")
                        .withVariant("Blue")
                        .withOutcome("yes")
                        .withAmount(3)
                        .withMetric(1)
                );

        Assert.assertEquals(asEvent.toJSON().equals("{\"type\":\"as.something.good\",\"origin\":\"your.business\",\"occurred_at\":\"2017-01-01T00:00:00.000Z\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/314\"}},{\"AFFECTS\":{\"entity_ref\":\"Product/plu3983\"}}],\"aspects\":{\"ab_test\":{\"id\":\"92982\",\"variant\":\"Blue\",\"outcome\":\"yes\",\"amount\":3.0,\"metric\":1.0}}}"),true);

        ASEvent parsedEvent = ASEvent.fromJSON(asEvent.toJSON());
        Assert.assertEquals(asEvent.toJSON().equals(parsedEvent.toJSON()),true); //Round-trip test

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(asEvent.getStreamId().toString().equals("e774f6c4-704d-3fa7-bd0f-c559eda12cbf"),true);

        Assert.assertEquals(asEvent.isValid(),true);
    }

}
