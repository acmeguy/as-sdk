package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.ASService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.core.model.aspects.ContentAspect.content;

public class ContentAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ContentAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleTagsAspectTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEntity entity = new ASEntity("Venue", "983983")
                .withAspect(content()
                        .withTitle("Some Title")
                        .withSubtitle("Some Subtitle")
                        .withByline("this is the byline for the content")
                        .withContent("<br>Massive content")
                );

        Assert.assertEquals(entity.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"content\":{\"title\":\"Some Title\",\"subtitle\":\"Some Subtitle\",\"byline\":\"this is the byline for the content\",\"content\":\"<br>Massive content\"}}}"),true);

        ASEntity parsedEntity = ASEntity.fromJSON(entity.toJSON());
        //Round-trip test
        Assert.assertEquals(entity.toJSON().equals(parsedEntity.toJSON()),true);
        Assert.assertEquals(entity.getStreamId().equals(parsedEntity.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(entity.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"),true);

    }

}
