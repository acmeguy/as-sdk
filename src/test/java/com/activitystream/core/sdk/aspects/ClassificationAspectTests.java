package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.ClassificationAspect;
import com.activitystream.sdk.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.junit.Test;

import java.util.Arrays;

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

        ClassificationAspect classification = new ClassificationAspect("some");
        Assert.assertEquals(classification.getType(),"some");

        classification = new ClassificationAspect("some","thing");
        Assert.assertEquals(classification.getType(),"some");
        Assert.assertEquals(classification.getVariant(),"thing");

        classification = new ClassificationAspect("some","thing");
        classification.withCategories("blue", "black","red");
        Assert.assertEquals(classification.getType(),"some");
        Assert.assertEquals(classification.getVariant(),"thing");
        Assert.assertEquals(classification.getCategories().get(0),"blue");
        Assert.assertEquals(classification.getCategories().get(1),"black");
        Assert.assertEquals(classification.getCategories().get(2),"red");

        classification.withType("another");
        Assert.assertEquals(classification.getType(),"another");
        classification.withTags("one","two");
        Assert.assertEquals(classification.getTags().toArray()[0],"one");
        Assert.assertEquals(classification.getTags().toArray()[1],"two");

    }

}