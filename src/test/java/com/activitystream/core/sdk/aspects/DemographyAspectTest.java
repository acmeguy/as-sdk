package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.DemographyAspect;
import com.activitystream.sdk.ASEntity;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import static com.activitystream.core.model.aspects.DemographyAspect.demography;


public class DemographyAspectTest {

    private static final Logger logger = LoggerFactory.getLogger(DemographyAspectTest.class);

    @Test
    public void simpleDimensionsTest() throws Exception {

        ASEntity secretAgent = new ASEntity("Agent", "007");
        secretAgent.withAspect(demography()
                .withGender("male")
                .withBirthDate("1968-04-13")
                .withEmployment("Governmental Employee")
                .withEthnicity("Caucasian")
                .withMaritalStatus("Very Single")
                .withHousing("Lives alone")
                .withMosaicGroup("Wealthy World Traveller")
                .withEducation("Like to have a degree")
                .withIncome("400k$ - 800k$"));

        Assert.assertEquals(secretAgent.toJSON().equals("{\"entity_ref\":\"Agent/007\",\"aspects\":{\"demography\":{\"gender\":\"male\",\"gender_guessed\":false,\"birth_day\":13,\"birth_year\":1968,\"birth_month\":4,\"birth_date\":\"1968-04-13\",\"employment\":\"Governmental Employee\",\"ethnicity\":\"Caucasian\",\"marital_status\":\"Very Single\",\"housing\":\"Lives alone\",\"custom_segment\":\"Wealthy World Traveller\",\"education\":\"Like to have a degree\",\"income\":\"400k$ - 800k$\"}}}"), true);

        ASEntity parsedEntity = ASEntity.fromJSON(secretAgent.toJSON());
        //Round-trip test
        Assert.assertEquals(secretAgent.toJSON().equals(parsedEntity.toJSON()), true);
        Assert.assertEquals(secretAgent.getStreamId().equals(parsedEntity.getStreamId()), true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(secretAgent.getStreamId().toString().equals("e6e3a763-b49e-3115-b997-fc364de9e70f"), true);

        DemographyAspect demography = new DemographyAspect("male", "1968-10-31");
        Assert.assertEquals(demography.getGender().equals("male"), true);
        Assert.assertEquals(demography.getGenderGuessed().equals(false), true);
        Assert.assertEquals(demography.getBirthDate().equals(new DateTime("1968-10-31")), true);
        Assert.assertEquals(demography.getBirthMonth().equals(10), true);
        Assert.assertEquals(demography.getBirthYear().equals(1968), true);
        Assert.assertEquals(demography.getBirthDay().equals(31), true);

        demography.setFamilySize("298");
        Assert.assertEquals(demography.getFamilySize().equals(298), true);
        demography.setEducation("some");
        Assert.assertEquals(demography.getEducation().equals("some"), true);
        demography.setIncome("not much");
        Assert.assertEquals(demography.getIncome().equals("not much"), true);
        demography.setMaritalStatus("yes");
        Assert.assertEquals(demography.getMaritalStatus().equals("yes"), true);
        demography.setEthnicity("Caucasian");
        Assert.assertEquals(demography.getEthnicity().equals("Caucasian"), true);
        demography.setHousing("mouse");
        Assert.assertEquals(demography.getHousing().equals("mouse"), true);

    }
}
