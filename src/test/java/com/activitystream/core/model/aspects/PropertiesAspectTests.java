package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.ASService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.Collections;
import java.util.TimeZone;

public class PropertiesAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simplePropertiesAspectTest() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEntity venue = new ASEntity("Venue", "983983")
                .withProperties("some_boolean",true)
                .withProperties("date", DateTime.parse("2017-01-01T00:00:00.000Z"))
                .withProperties("a_map", Collections.singletonMap("map_value",Collections.singletonMap("nested",true)))
                .withProperties("a_sttring", "Yes, I'm a String")
                .withProperties("one_of_many", true, "two_of_many",true, "four_of_many",false, "item_index",4, "position_name","five");

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"properties\":{\"some_boolean\":true,\"date\":\"2017-01-01T00:00:00.000Z\",\"a_map\":{\"map_value\":{\"nested\":true}},\"a_sttring\":\"Yes, I'm a String\",\"one_of_many\":true,\"two_of_many\":true,\"four_of_many\":false,\"item_index\":4,\"position_name\":\"five\"}}"),true);

        ASEntity parsedVenue = ASEntity.fromJSON(venue.toJSON());
        //Round-trip test
        Assert.assertEquals(venue.toJSON().equals(parsedVenue.toJSON()),true);
        Assert.assertEquals(venue.getStreamId().equals(parsedVenue.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(venue.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"),true);

    }

}
