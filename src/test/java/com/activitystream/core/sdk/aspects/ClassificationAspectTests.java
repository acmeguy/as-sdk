package com.activitystream.core.sdk.aspects;

import com.activitystream.sdk.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.junit.Test;

import static com.activitystream.core.model.aspects.ClassificationAspect.classification;

public class ClassificationAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ClassificationAspectTests.class);

    @Test
    public void testSimpleClassificationTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.withAspect(classification()
                .withType("Theater")
                .withVariant("Concert Hall")
                .withCategories("Classical","Pop","Variety","Shows"));
        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"classification\":{\"type\":\"Theater\",\"variant\":\"Concert Hall\",\"categories\":[\"Classical\",\"Pop\",\"Variety\",\"Shows\"]}}}"),true);

        ASEntity customer = new ASEntity("Customer", "ABC123");
        customer.withAspect(classification()
                .withType("Individual")
                .withVariant("A")
                .withCategories("VIP","Regular","Friend & Family")
                .withTags("Early Buyer","Likes Jazz"));

        Assert.assertEquals(customer.toJSON().equals("{\"entity_ref\":\"Customer/ABC123\",\"aspects\":{\"classification\":{\"type\":\"Individual\",\"variant\":\"A\",\"categories\":[\"VIP\",\"Regular\",\"Friend & Family\"],\"tags\":[\"Early Buyer\",\"Likes Jazz\"]}}}"),true);

        ASEntity event = new ASEntity("Event", "SHOW-ABC123");
        event.withAspect(classification()
                .withType("Play")
                .withVariant("Comedy")
                .withCategories("Tragic Comedy","Black Humor")
                );
        Assert.assertEquals(event.toJSON().equals("{\"entity_ref\":\"Event/SHOW-ABC123\",\"aspects\":{\"classification\":{\"type\":\"Play\",\"variant\":\"Comedy\",\"categories\":[\"Tragic Comedy\",\"Black Humor\"]}}}"),true);

        ASEntity parsedEvent = ASEntity.fromJSON(event.toJSON());
        //Round-trip test
        Assert.assertEquals(event.toJSON().equals(parsedEvent.toJSON()),true);
        Assert.assertEquals(event.getStreamId().equals(parsedEvent.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(event.getStreamId().toString().equals("d16bde84-2417-3d4d-9af3-9c10b601c228"),true);

    }

}