package com.activitystream.model;

import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.relations.ASEntityRelationTypes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;
import java.util.LinkedHashMap;

import static com.activitystream.model.aspects.AddressAspect.address;
import static com.activitystream.model.aspects.ClassificationAspect.classification;
import static com.activitystream.model.aspects.DemographyAspect.demography;
import static com.activitystream.model.aspects.PresentationAspect.presentation;
import static com.activitystream.model.aspects.TrafficSource.trafficSource;
import static com.activitystream.model.aspects.TrafficSourceAspect.trafficSources;

public class SimpleEntityTests {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEntityTests.class);

    @Test
    public void createVariousEntities() throws IOException {

        ASEntity customer = new ASEntity();
        Assert.assertEquals(customer.isValid(), false);
        Assert.assertEquals(customer.validator().hasErrors(), 1);

        //Testing different coding styles
        customer = new ASEntity()
                .withEntityReference("Customer","0071")
                .withAspect(presentation()
                        .withLabel("John McDoe")
                );
        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\"}}}");

        //Testing different coding styles
        customer = new ASEntity("Customer","0071","John McDoe", "John is great");
        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\",\"description\":\"John is great\"}}}");

        //Testing different coding styles
        ASEntity sameCustomer = new ASEntity(new EntityReference("Customer","0071"),"John McDoe", "John is great");
        Assert.assertEquals(sameCustomer.toJSON().equals(customer.toJSON()), true);
        Assert.assertEquals(sameCustomer.getStreamId().toString().equals("08c80573-6440-3c74-8fbe-e1d7738d75b3"), true);

        ASEntity customerWithRelations = new ASEntity("Customer","0071");
        customerWithRelations.withRelation(ASEntityRelationTypes.AKA,"Email/stebax@gmail.com"); //Relations :: Simple example
        customerWithRelations.withRelation(ASEntityRelationTypes.AKA,new ASEntity("Phone","+1-555011011","Johns Mobile Phone")); //Relations :: Example Using An embedded entity (They can be as complex as you like)

        //Test the JSON output
        Assert.assertEquals(customerWithRelations.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"relations\":[{\"AKA\":{\"entity_ref\":\"Email/stebax@gmail" +
                ".com\"}},{\"AKA\":{\"entity_ref\":\"Phone/+1-555011011\",\"aspects\":{\"presentation\":{\"label\":\"Johns Mobile Phone\"}}}}]}");

        //Test the Stream ID
        Assert.assertEquals(customerWithRelations.getStreamId().toString().equals("08c80573-6440-3c74-8fbe-e1d7738d75b3"), true);

    }

    @Test
    public void createCustomerEntityWithAspects() throws IOException {
        ASEntity customer = new ASEntity("Agent","007")
                .withAspect(presentation()
                        .withLabel("James Bond")
                        .withThumbnail("http://www.007.com/wp-content/uploads/2014/01/Roger-Moore-james-bond-BW.jpg")
                        .withIcon("007-icon")
                        .withDescription("Dude working for a Queen killing people, mostly")
                        .withDetailsUrl("http://www.007.com/"))
                .withAspect(demography()
                        .withGender("male")
                        .withBirthDate("1968-04-13")
                        .withEmployment("Governmental Employee")
                        .withEthnicity("Caucasian")
                        .withMaritalStatus("Very Single")
                        .withHousing("Lives alone")
                        .withMosaicGroup("Wealthy World Traveller")
                        .withEducation("Like to have a degree")
                        .withIncome("400k$ - 800k$"))
                .withAspect(address()
                        .withAddress("61 Horseferry Road")
                        .withPostCode("SW1")
                        .withCity("Westminster")
                        .withRegion("London")
                        .withState("Greater London")
                        .withCountry("United Kingdom")
                        .withCountryCode("UK")
                ).withAspect(classification()
                        .withType("individual")
                        .withVariant("employee")
                        .withCategories("agent","secret")
                        .withTags("kills bad guys","travels","mostly obeys","womanizer","loyal","utility")
                )
                .withRelationIfValid(ASEntityRelationTypes.AKA,"Email","007@mi5.gov.uk")
                .withRelationIfValid(ASEntityRelationTypes.AKA,"Phone","+441007007007", "secure_line",true)
                .withPartition("mi5_only");

        Assert.assertEquals(customer.toJSON().equals("{\"entity_ref\":\"Agent/007\",\"aspects\":{\"presentation\":{\"label\":\"James Bond\",\"thumbnail\":\"http://www.007.com/wp-content/uploads/2014/01/Roger-Moore-james-bond-BW.jpg\",\"icon\":\"007-icon\",\"description\":\"Dude working for a Queen killing people, mostly\",\"details_url\":\"http://www.007.com/\"},\"demography\":{\"gender\":\"male\",\"gender_guessed\":false,\"birth_day\":13,\"birth_year\":1968,\"birth_month\":4,\"employment\":\"Governmental Employee\",\"ethnicity\":\"Caucasian\",\"marital_status\":\"Very Single\",\"income\":\"400k$ - 800k$\",\"mosaic_group\":\"Wealthy World Traveller\",\"education\":\"Like to have a degree\"},\"address\":{\"address\":\"61 Horseferry Road\",\"zip_code\":\"SW1\",\"city\":\"Westminster\",\"region\":\"London\",\"state\":\"Greater London\",\"country\":\"United Kingdom\",\"country_code\":\"UK\"},\"classification\":{\"type\":\"individual\",\"variant\":\"employee\",\"categories\":[\"agent\",\"secret\"],\"tags\":[\"kills bad guys\",\"travels\",\"mostly obeys\",\"womanizer\",\"loyal\",\"utility\"]}},\"relations\":[{\"AKA\":{\"entity_ref\":\"Email/007@mi5.gov.uk\"}},{\"AKA\":{\"entity_ref\":\"Phone/+441007007007\"},\"properties\":{\"secure_line\":true}}],\"partition\":\"mi5_only\"}"),true);
    }

    @Test
    public void createEntitiesWithAspects() throws IOException {

        ASEntity customer = new ASEntity("Customer","0071","John McDoe")
                .withAspect(demography()
                        .withGender("female")
                        .withIncome("100-200")
                        .withMosaicGroup("B")
                        .withBirthYear(1999)
                        .withBirthMonth(12)
                        .withMaritalStatus("Married")
                        .withEducation("Some"));

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\"},\"demography\":{\"gender\":\"female\",\"gender_guessed\":false,\"income\":\"100-200\",\"mosaic_group\":\"B\",\"birth_year\":1999,\"birth_month\":12,\"marital_status\":\"Married\",\"education\":\"Some\"}}}");

        customer = new ASEntity("Customer","0071","John McDoe")
                .withDimension("big","one")
                .withDimension("bigger","one")
                .withMetric("bigger",1);

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\"},\"dimensions\":{\"big\":\"one\",\"bigger\":\"one\"},\"metrics\":{\"bigger\":1.0}}}");

        customer = new ASEntity("Customer","0071")
                .withAspect(presentation()
                        .withLabel("John McDoe")
                        .withIcon("some_icon.png")
                        .withDescription("John is great")
                        .withDetailsUrl("https://en.wikipedia.org/wiki/john.mcdoe")
                );

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\",\"icon\":\"some_icon.png\",\"description\":\"John is great\",\"details_url\":\"https://en.wikipedia.org/wiki/john.mcdoe\"}}}");

        customer = new ASEntity("Customer","0071")
                .withRelations(ASEntityRelationTypes.AKA,"Email/john@mcdoe.com")
                .withProperties("Something",new LinkedHashMap<String,Object>(){{ put("Something","else"); }})
                .withProperties("Another","type","of",true,"property",1);

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"relations\":[{\"AKA\":{\"entity_ref\":\"Email/john@mcdoe.com\"}}],\"properties\":{\"Something\":{\"Something\":\"else\"},\"Another\":\"type\",\"of\":true,\"property\":1}}");

        customer = new ASEntity("Customer","0071")
                .withAspect(
                        trafficSources().addTrafficSource(
                        trafficSource()
                                .addCampaign("Some really kewl campaign")
                                .addMedium("Web")
                                .addReferrer("Faceboook")
                                .addSource("Faceboook")
                                .addTerm("some")));

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"traffic_sources\":[{\"campaign\":\"Some really kewl campaign\",\"medium\":\"Web\",\"referrer\":\"Faceboook\",\"source\":\"Faceboook\",\"term\":\"some\"}]}}");
    }

}
