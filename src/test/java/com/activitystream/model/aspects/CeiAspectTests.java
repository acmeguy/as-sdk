package com.activitystream.model.aspects;

import com.activitystream.model.ASEvent;
import com.activitystream.model.config.ASConfig;
import com.activitystream.model.relations.ASEventRelationTypes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.model.aspects.CeiAspect.cei;

public class CeiAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(CeiAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleTagsAspectTest() throws Exception {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

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

    }

}
