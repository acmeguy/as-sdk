package com.activitystream.model;

import com.activitystream.model.relations.ASEntityRelationTypes;
import com.activitystream.model.relations.ASEventRelationTypes;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.stream.ImportanceLevel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;

import static com.activitystream.model.aspects.ClassificationAspect.classification;
import static com.activitystream.model.aspects.ClientDeviceAspect.clientDevice;
import static com.activitystream.model.aspects.ClientIpAspect.clientIP;
import static com.activitystream.model.aspects.PresentationAspect.presentation;

public class SimpleEventTests {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEventTests.class);

    @Test
    public void basicASEventTests() throws IOException {

        //Minimum valid Message
        ASEvent webVisitStarts = new ASEvent()
                .addType(ASEvent.PAST.AS_CRM_VISIT_STARTED)
                .addOrigin("wwww.mysite.domain")
                .addOccurredAt("2017-01-01T00:00:00.000Z")
                .addRelationIfValid(ASEventRelationTypes.ACTOR,"Customer","007")
                .addAspect(classification()
                        .addType("virtual")
                        .addVariant("web"))
                .addAspect(clientIP("127.0.0.1"))
                .addAspect(clientDevice("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"));

        Assert.assertEquals(webVisitStarts.toJSON().equals("{\"type\":\"as.crm.visit.started\",\"origin\":\"wwww.mysite.domain\",\"occurred_at\":\"2017-01-01T00:00:00.000Z\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/007\"}}],\"aspects\":{\"classification\":{\"type\":\"virtual\",\"variant\":\"web\"},\"client_ip\":{\"ip\":\"127.0.0.1\"},\"client_device\":{\"user_agent\":\"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36\"}}}"),true);

        ASEntity customer = new ASEntity("Customer","30893928")
                .addAspect(presentation()
                        .addLabel("John Doe"))
                .addRelationIfValid(ASEntityRelationTypes.AKA,"Email", "john.doe@gmail.com")
                .addRelationIfValid(ASEntityRelationTypes.AKA,"Phone", "+150012348765");

        webVisitStarts = new ASEvent()
                .addType(ASEvent.PAST.AS_CRM_VISIT_STARTED)
                .addOrigin("wwww.mysite.domain")
                .addOccurredAt("2017-01-01T00:00:00.000Z")
                .addRelation(ASEventRelationTypes.ACTOR,customer)
                .addAspect(classification()
                        .addType("virtual")
                        .addVariant("web"))
                .addAspect(clientIP("127.0.0.1"))
                .addAspect(clientDevice("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"));

        Assert.assertEquals(webVisitStarts.toJSON().equals("{\"type\":\"as.crm.visit.started\",\"origin\":\"wwww.mysite.domain\",\"occurred_at\":\"2017-01-01T00:00:00.000Z\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/30893928\",\"aspects\":{\"presentation\":{\"label\":\"John Doe\"}},\"relations\":[{\"AKA\":{\"entity_ref\":\"Email/john.doe@gmail.com\"}},{\"AKA\":{\"entity_ref\":\"Phone/+150012348765\"}}]}}],\"aspects\":{\"classification\":{\"type\":\"virtual\",\"variant\":\"web\"},\"client_ip\":{\"ip\":\"127.0.0.1\"},\"client_device\":{\"user_agent\":\"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36\"}}}"),true);
    }

    @Test
    public void testBasicMessageValidation() throws IOException {

        //Minimum valid Message
        ASEvent asEvent = new ASEvent()
                .addType("as.application.authentication.login")
                .addOrigin("your.web.application")
                .addOccurredAt("2017-01-01T00:00:00.000Z")
                .addImportance(ImportanceLevel.NOT_IMPORTANT)
                .addRelationIfValid(ASEventRelationTypes.ACTOR, "Customer","314");

        Assert.assertEquals(asEvent.isValid(), true);
        Assert.assertEquals(asEvent.validator().hasErrors(), 0);
        Assert.assertEquals(asEvent.toJSON().equals("{\"type\":\"as.application.authentication.login\",\"origin\":\"your.web.application\",\"occurred_at\":\"2017-01-01T00:00:00.000Z\",\"importance\":2,\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/314\"}}]}"), true);

        //Valid Message
        asEvent = new ASEvent("as.commerce.purchase.complete", "as.sdk.test", null, ImportanceLevel.NOT_IMPORTANT, "Customer/314");
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
