package com.activitystream.core.sdk;

import com.activitystream.sdk.ASService;
import com.activitystream.core.model.relations.ASEventRelationTypes;
import com.activitystream.sdk.ASEvent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.activitystream.core.model.aspects.ClassificationAspect.classification;
import static com.activitystream.core.model.aspects.ClientDeviceAspect.clientDevice;
import static com.activitystream.core.model.aspects.ClientIpAspect.clientIP;

public class SendEventTests {

    private static final Logger logger = LoggerFactory.getLogger(SendEventTests.class);

    @Test
    public void sendBasicEventTests() throws IOException {

        ASService.credentials("axs", "axsapikey", ASService.ServiceLevel.DEVELOPMENT);

        //Minimum valid Message
        ASEvent webVisitStarts = new ASEvent()
                .withType(ASEvent.PRE.AS_CRM_VISIT_STARTED)
                .withOrigin("wwww.mysite.domain")
                .withOccurredAt("2017-01-01T00:00:00.000Z")
                .withRelationIfValid(ASEventRelationTypes.ACTOR,"Customer","007")
                .withAspect(classification()
                        .withType("virtual")
                        .withVariant("web"))
                .withAspect(clientIP("127.0.0.1"))
                .withAspect(clientDevice("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"));

        try {
            webVisitStarts.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
