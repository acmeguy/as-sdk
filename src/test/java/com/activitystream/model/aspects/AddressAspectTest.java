package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.activitystream.model.aspects.AddressAspect.address;
import static com.activitystream.model.aspects.PresentationAspect.presentation;


public class AddressAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(AddressAspectTest.class);

    @Test(groups = "unit")
    public void testSimpleAddressTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.addAspect(address()
                .addAddress("Kensington Gore")
                .addCity("Kensington")
                .addState("Greater London")
                .addPostCode("SW7 2AP")
                .addCountryCode("UK")
                .addCountry("United Kingdom"));

        logger.warn("Address: " + venue.toJSON());

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"presentation\":{\"label\":\"Royal Albert Hall\",\"description\":\"The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.\",\"details_url\":\"https://www.royalalberthall.com/\",\"thumbnail\":\"https://cdn.royalalberthall.com/file/1396975600/32830992449\",\"icon\":\"ol-venue\"}}}"),true);
    }

}
