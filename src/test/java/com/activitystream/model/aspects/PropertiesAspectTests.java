package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import com.activitystream.model.config.ASConfig;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.Collections;
import java.util.TimeZone;

import static com.activitystream.model.aspects.TagsAspect.tags;

public class PropertiesAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simplePropertiesAspectTest() throws Exception {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEntity venue = new ASEntity("Venue", "983983")
                .addProperties("some_boolean",true)
                .addProperties("date", DateTime.parse("2017-01-01T00:00:00.000Z"))
                .addProperties("a_map", Collections.singletonMap("map_value",Collections.singletonMap("nested",true)))
                .addProperties("a_sttring", "Yes, I'm a String")
                .addProperties("one_of_many", true, "two_of_many",true, "four_of_many",false, "item_index",4, "position_name","five");

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"properties\":{\"some_boolean\":true,\"date\":\"2017-01-01T00:00:00.000Z\",\"a_map\":{\"map_value\":{\"nested\":true}},\"a_sttring\":\"Yes, I'm a String\",\"one_of_many\":true,\"two_of_many\":true,\"four_of_many\":false,\"item_index\":4,\"position_name\":\"five\"}}"),true);
    }

}
