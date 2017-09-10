package com.activitystream.model;

import com.activitystream.model.relations.ASEventRelationTypes;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.stream.ImportanceLevel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;

public class SimpleEventTests {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEventTests.class);

    @Test
    public void testBasicMessageValidation() throws IOException {

        //Valid Message
        ASEvent asEvent = new ASEvent("as.commerce.purchase.complete", "as.sdk.test", null, ImportanceLevel.NOT_IMPORTANT, "Customer/314");
        Assert.assertEquals(asEvent.isValid(), true);
        Assert.assertEquals(asEvent.validator().hasErrors(), 0);

        //Invalid Type string
        asEvent = new ASEvent("as.commerce. invalid purchase.complete", "as.sdk.test", null, ImportanceLevel.NOT_IMPORTANT, "Customer/314");
        Assert.assertEquals(asEvent.isValid(), false);
        Assert.assertEquals(asEvent.validator().hasErrors(), 1);
        //logger.debug("Error: " + asEvent.validator().getErrors());

        //Invalid Origin
        asEvent = new ASEvent("as.commerce.purchase.complete", "as.sdk invalid .test", null, ImportanceLevel.NOT_IMPORTANT, "Customer/314");
        Assert.assertEquals(asEvent.isValid(), false);
        Assert.assertEquals(asEvent.validator().hasErrors(), 1);

        //Invalid type string
        asEvent = new ASEvent("2017-01-01T00:00:00.000","as.commerce.purchase.complete$", "as.sdk.test", null, ImportanceLevel.NOT_IMPORTANT, "Customer/314");
        Assert.assertEquals(asEvent.isValid(), false);
        Assert.assertEquals(asEvent.validator().hasErrors(), 1);

    }

    @Test
    public void testJSONSerialisationWithStreamIds() throws IOException {

        ASEvent asEvent = new ASEvent("2017-01-01T00:00:00.000","as.COMMERCE.purchase.completed", "as.sdk.test", null, ImportanceLevel.NOT_IMPORTANT,
                "Customer/314");
        Assert.assertEquals(asEvent.isValid(), true);

        Assert.assertEquals(asEvent.getStreamId().toString(), "12d88deb-801f-3f80-9cf4-6b9ed0afaecc");
        Assert.assertEquals(asEvent.validator().getWarnings().isEmpty(), false); //Should contain warning
        Assert.assertEquals(asEvent.toJSON(),"{\"occurred_at\":\"2017-01-01T00:00:00.000Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"as.sdk" +
                ".test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/314\"}}],\"importance\":2," +
                "\"stream_id\":\"12d88deb-801f-3f80-9cf4-6b9ed0afaecc\"}");

        //Test a full round-trip to and from JSON

        ASEvent roundTripEvent = asEvent.fromJSON(asEvent.toJSON());
        roundTripEvent.remove(ASConstants.FIELD_STREAM_ID); // remove to recalculate
        Assert.assertEquals(roundTripEvent.getStreamId().toString(), "12d88deb-801f-3f80-9cf4-6b9ed0afaecc"); //Recalculated stream id should match
        Assert.assertEquals(roundTripEvent.toJSON().equals(asEvent.toJSON()), true);
        Assert.assertEquals(roundTripEvent.getType().equals(asEvent.getType()), true);
        Assert.assertEquals(roundTripEvent.getOrigin().equals(asEvent.getOrigin()), true);
        Assert.assertEquals(roundTripEvent.getImportance().equals(asEvent.getImportance()), true);
        Assert.assertEquals(roundTripEvent.getStreamId().equals(asEvent.getStreamId()), true);
        Assert.assertEquals((roundTripEvent.getOccurredAt().getMillis() ==  asEvent.getOccurredAt().getMillis()), true);
        Assert.assertEquals(roundTripEvent.getRelationsManager().getFirstRelationsOfType("ACTOR").equals(asEvent.getRelationsManager().getFirstRelationsOfType("ACTOR")), true);

        //Create the smallest possible event the long way
        ASEvent asNewEvent = new ASEvent();
        Assert.assertEquals(asNewEvent.isValid(),false);
        asNewEvent.addRelation(new Relation(ASEventRelationTypes.ACTOR,"Customer/314"));
        Assert.assertEquals(asNewEvent.isValid(),false);
        asNewEvent.setType("as.commerce.purchase.completed");
        Assert.assertEquals(asNewEvent.isValid(true),true);

    }

}
