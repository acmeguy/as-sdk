package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASEntity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;


public class DimensionsAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(DimensionsAspectTest.class);

    @Test
    public void simpleDimensionsTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.withDimension("house_color","white");
        venue.addDimensions("door_faces","north", "door_color","brown");

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"dimensions\":{\"house_color\":\"white\",\"door_faces\":\"north\",\"door_color\":\"brown\"}}}"),true);

        ASEntity parsedEntity = ASEntity.fromJSON(venue.toJSON());
        //Round-trip test
        Assert.assertEquals(venue.toJSON().equals(parsedEntity.toJSON()),true);
        Assert.assertEquals(venue.getStreamId().equals(parsedEntity.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(venue.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"),true);

    }

}
