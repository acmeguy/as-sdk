package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.junit.Test;

import static com.activitystream.model.aspects.PresentationAspect.presentation;


public class PresentationAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(PresentationAspectTest.class);

    @Test
    public void testSimplePresentation() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.addAspect(presentation()
                .addLabel("Royal Albert Hall")
                .addDescription("The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.")
                .addDetailsUrl("https://www.royalalberthall.com/")
                .addThumbnail("https://cdn.royalalberthall.com/file/1396975600/32830992449")
                .addIcon("ol-venue"));

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"presentation\":{\"label\":\"Royal Albert Hall\",\"description\":\"The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.\",\"details_url\":\"https://www.royalalberthall.com/\",\"thumbnail\":\"https://cdn.royalalberthall.com/file/1396975600/32830992449\",\"icon\":\"ol-venue\"}}}"),true);
    }

}
