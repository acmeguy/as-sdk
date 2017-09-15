package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.junit.Test;

import static com.activitystream.model.aspects.AddressAspect.address;


public class AddressAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(AddressAspectTests.class);

    @Test
    public void testSimpleAddressTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.addAspect(address()
                .addAddress("Kensington Gore")
                .addCity("Kensington")
                .addState("Greater London")
                .addPostCode("SW7 2AP")
                .addCountryCode("UK")
                .addCountry("United Kingdom"));

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"address\":{\"address\":\"Kensington Gore\",\"city\":\"Kensington\",\"state\":\"Greater London\",\"zip_code\":\"SW7 2AP\",\"country_code\":\"UK\",\"country\":\"United Kingdom\"}}}"),true);
    }

}
