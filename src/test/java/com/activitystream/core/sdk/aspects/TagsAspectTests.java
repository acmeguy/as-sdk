package com.activitystream.core.sdk.aspects;

import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.ASService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.core.model.aspects.TagsAspect.tags;

public class TagsAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(TagsAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleTagsAspectTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.withAspect(tags().withTags("National"));

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"tags\":[\"National\"]}}"),true);

        ASEntity parsedVenue = ASEntity.fromJSON(venue.toJSON());
        //Round-trip test
        Assert.assertEquals(venue.toJSON().equals(parsedVenue.toJSON()),true);
        Assert.assertEquals(venue.getStreamId().equals(parsedVenue.getStreamId()),true);
        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(venue.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"),true);

    }

}
