package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.junit.Test;

import static com.activitystream.model.aspects.AddressAspect.address;
import static com.activitystream.model.aspects.GeoLocationAspect.geoLocation;
import static com.activitystream.model.aspects.PresentationAspect.presentation;


public class AddressAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(AddressAspectTests.class);

    @Test
    public void testSimpleAddressTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue
                .withAspect(presentation()
                        .withLabel("The Royal Albert Hall"))
                .withAspect(address()
                        .withAddress("Kensington Gore")
                        .withCity("Kensington")
                        .withState("Greater London")
                        .withPostCode("SW7 2AP")
                        .withCountryCode("UK")
                        .withCountry("United Kingdom"))
                .withAspect(geoLocation()
                        .withLatitude(51.5009088)
                        .withLongitude(-0.177366));

        //logger.warn("smu " + venue.toJSON());

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"presentation\":{\"label\":\"The Royal Albert Hall\"},\"address\":{\"address\":\"Kensington Gore\",\"city\":\"Kensington\",\"state\":\"Greater London\",\"zip_code\":\"SW7 2AP\",\"country_code\":\"UK\",\"country\":\"United Kingdom\"},\"geo_location\":{\"latitude\":51.50090789794922,\"longitude\":-0.1773660033941269}}}"),true);
    }

}
