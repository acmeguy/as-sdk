package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.ASEntity;
import com.activitystream.model.ASEvent;
import com.activitystream.model.ASLineItem;
import com.activitystream.model.config.ASConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.TimeZone;

import static com.activitystream.model.ASLineItem.lineItem;
import static com.activitystream.model.aspects.ItemsManager.items;
import static com.activitystream.model.aspects.ResolvableAspect.resolvable;
import static com.activitystream.model.aspects.TagsAspect.tags;

public class ResolvableAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ResolvableAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simpleResolvableAspectTest() throws Exception {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent miniEvent = new ASEvent(ASEvent.PRE.AS_MARKETING_AD_INTERACTION, "www.web");
        miniEvent.addOccurredAt("2017-01-01T12:00:00")
                .addRelationIfValid(ASConstants.REL_ACTOR,"Customer/983938")
                .addAspect(resolvable("3983"));

        Assert.assertEquals(miniEvent.toJSON().equals("{\"occurred_at\":\"2017-01-01T12:00:00.000Z\",\"type\":\"as.marketing.ad.interaction\",\"origin\":\"www.web\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/983938\"}}],\"importance\":3,\"aspects\":{\"resolvable\":{\"external_id\":\"3983\"}}}"),true);
    }

}
