package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import com.activitystream.model.config.ASConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.model.aspects.ContentAspect.content;

public class ContentAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ContentAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleTagsAspectTest() throws Exception {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEntity entity = new ASEntity("Venue", "983983")
                .withAspect(content()
                        .withTitle("Some Title")
                        .withSubtitle("Some Subtitle")
                        .withByline("this is the byline for the content")
                        .withContent("<br>Massive content")
                );

        Assert.assertEquals(entity.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"content\":{\"title\":\"Some Title\",\"subtitle\":\"Some Subtitle\",\"byline\":\"this is the byline for the content\",\"content\":\"<br>Massive content\"}}}"),true);

    }

}
