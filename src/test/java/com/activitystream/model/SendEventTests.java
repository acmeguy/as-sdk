package com.activitystream.model;

import com.activitystream.model.config.ASService;
import com.activitystream.model.relations.ASEntityRelationTypes;
import com.activitystream.model.relations.ASEventRelationTypes;
import com.activitystream.model.relations.Relation;
import com.activitystream.model.stream.ImportanceLevel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;

import static com.activitystream.model.aspects.ClassificationAspect.classification;
import static com.activitystream.model.aspects.ClientDeviceAspect.clientDevice;
import static com.activitystream.model.aspects.ClientIpAspect.clientIP;
import static com.activitystream.model.aspects.PresentationAspect.presentation;

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
