package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.PresentationAspect;
import com.activitystream.sdk.ASEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.junit.Test;

import static com.activitystream.core.model.aspects.PresentationAspect.presentation;


public class PresentationAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(PresentationAspectTest.class);

    @Test
    public void testSimplePresentation() throws Exception {

        ASEntity venue = new ASEntity("Venue", "983983");
        venue.withAspect(presentation()
                .withLabel("Royal Albert Hall")
                .withDescription("The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.")
                .withDetailsUrl("https://www.royalalberthall.com/")
                .withThumbnail("https://cdn.royalalberthall.com/file/1396975600/32830992449")
                .withIcon("ol-venue"));

        Assert.assertEquals(venue.toJSON().equals("{\"entity_ref\":\"Venue/983983\",\"aspects\":{\"presentation\":{\"label\":\"Royal Albert Hall\",\"description\":\"The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.\",\"details_url\":\"https://www.royalalberthall.com/\",\"thumbnail\":\"https://cdn.royalalberthall.com/file/1396975600/32830992449\",\"icon\":\"ol-venue\"}}}"),true);

        ASEntity parsedVenue = ASEntity.fromJSON(venue.toJSON());
        //Round-trip test
        Assert.assertEquals(venue.toJSON().equals(parsedVenue.toJSON()),true);
        Assert.assertEquals(venue.getStreamId().equals(parsedVenue.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(venue.getStreamId().toString().equals("e769e03d-0393-37ce-a40f-7d70b2036906"),true);

        PresentationAspect presentation = new PresentationAspect("some");
        Assert.assertEquals(presentation.getLabel().equals("some"),true);

        presentation = new PresentationAspect("some", "thumbnail","icon","description", "details", null);
        Assert.assertEquals(presentation.getLabel().equals("some"),true);
        Assert.assertEquals(presentation.getThumbnail().equals("thumbnail"),true);
        Assert.assertEquals(presentation.getIcon().equals("icon"),true);
        Assert.assertEquals(presentation.getDescription().equals("description"),true);
        Assert.assertEquals(presentation.getDetailsUrl().equals("details"),true);

    }

    @Test
    public void testPresentationDataBeforeBinding() throws Exception {
        String myLabel = "ABC";

        ASEntity entity = new ASEntity("MyEntity", "123");
        Assert.assertNull(entity.getAspectManager().getPresentation());

        PresentationAspect presentationAspect = new PresentationAspect();

        entity.withAspect(presentationAspect);
        Assert.assertNull(entity.getAspectManager().getPresentation());

        presentationAspect.withLabel(myLabel);
        entity.withAspect(presentationAspect);
        Assert.assertTrue(getPresentationAspectWithLabel(myLabel).getLabel().equals(entity.getAspectManager().getPresentation().getLabel()));
    }

    private PresentationAspect getPresentationAspectWithLabel(String label){
        PresentationAspect presentation = new PresentationAspect();
        presentation.setLabel(label);
        return presentation;
    }
}
