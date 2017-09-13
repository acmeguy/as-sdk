package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.activitystream.model.aspects.ClassificationAspect.classification;


public class ClassificationAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(ClassificationAspectTest.class);

    @Test(groups = "unit")
    public void testSimpleClassificationTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.addAspect(classification()
                .addType("Theater")
                .addVariant("Concert Hall")
                .addCategories("Classical","Pop","Variety","Shows"));

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"classification\":{\"type\":\"Theater\",\"variant\":\"Concert Hall\",\"categories\":[\"Classical\",\"Pop\",\"Variety\",\"Shows\"]}}}"),true);
    }

}
