package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.AddressAspect;
import com.activitystream.sdk.ASEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import static com.activitystream.core.model.aspects.AddressAspect.address;
import static com.activitystream.core.model.aspects.GeoLocationAspect.geoLocation;
import static com.activitystream.core.model.aspects.PresentationAspect.presentation;


public class AddressAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(AddressAspectTest.class);

    @Test
    public void testSimpleAddressTest() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue
                .withAspect(presentation()
                        .withLabel("The Royal Albert Hall"))
                .withAspect(address()
                        .withAddress("Kensington Gore")
                        .withCity("London")
                        .withMunicipality("South Kensington")
                        .withState("Greater London")
                        .withPostCode("SW7 2AP")
                        .withCountryCode("UK")
                        .withCountry("United Kingdom"))
                .withAspect(geoLocation()
                        .withLatitude(51.5009088)
                        .withLongitude(-0.177366));

        //logger.warn("smu " + venue.toJSON());

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"presentation\":{\"label\":\"The Royal Albert Hall\"},\"address\":{\"address\":\"Kensington Gore\",\"city\":\"London\",\"municipality\":\"South Kensington\",\"state\":\"Greater London\",\"zip_code\":\"SW7 2AP\",\"country_code\":\"UK\",\"country\":\"United Kingdom\"},\"geo_location\":{\"latitude\":51.50090789794922,\"longitude\":-0.1773660033941269}}}"), true);

        ASEntity parsedVenue = ASEntity.fromJSON(venue.toJSON());
        Assert.assertEquals(venue.toJSON().equals(parsedVenue.toJSON()), true); //Round-trip test

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(venue.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"), true);

        AddressAspect address = new AddressAspect("address", "address2", "city", "postcode", "country", "countryCode");
        address.withMunicipality("municipality");
        Assert.assertEquals(address.getAddress().equals("address"), true);
        Assert.assertEquals(address.getAddress2().equals("address2"), true);
        Assert.assertEquals(address.getCity().equals("city"), true);
        Assert.assertEquals(address.getMunicipality().equals("municipality"), true);
        Assert.assertEquals(address.getPostcode().equals("postcode"), true);
        Assert.assertEquals(address.getCountry().equals("country"), true);
        Assert.assertEquals(address.getCountryCode().equals("countryCode"), true);
    }

    @Test
    public void testAddressAspectWithAnonymized() throws JsonProcessingException {
        ASEntity customer = new ASEntity()
                .withAspect(new AddressAspect()
                        .withAddressAnonymized());

        Assert.assertEquals(customer.toJSON(), "{\"aspects\":{\"address\":{\"is_anonymized\":true}}}");
    }
}
