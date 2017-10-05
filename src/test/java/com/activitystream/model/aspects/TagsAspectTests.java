package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import com.activitystream.model.config.ASConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.model.aspects.TagsAspect.tags;

public class TagsAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(TagsAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleTagsAspectTest() throws Exception {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.withAspect(tags().withTags("National"));

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"tags\":[\"National\"]}}"),true);
    }

}
