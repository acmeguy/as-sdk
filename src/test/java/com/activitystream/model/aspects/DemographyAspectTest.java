package com.activitystream.model.aspects;

import com.activitystream.model.ASEntity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import static com.activitystream.model.aspects.DemographyAspect.demography;


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

        Assert.assertEquals(secretAgent.toJSON().equals("{\"entity_ref\":\"Agent/007\",\"aspects\":{\"demography\":{\"gender\":\"male\",\"gender_guessed\":false,\"birth_day\":13,\"birth_year\":1968,\"birth_month\":4,\"employment\":\"Governmental Employee\",\"ethnicity\":\"Caucasian\",\"marital_status\":\"Very Single\",\"income\":\"400k$ - 800k$\",\"mosaic_group\":\"Wealthy World Traveller\",\"education\":\"Like to have a degree\"}}}"),true);
    }

}
