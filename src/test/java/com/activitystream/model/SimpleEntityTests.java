package com.activitystream.model;

import com.activitystream.model.entities.EntityReference;
import com.activitystream.model.relations.ASEntityRelationTypes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;
import java.util.LinkedHashMap;

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

        customer = new ASEntity("Customer","0071","John McDoe");
        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\"}}}");

        customer = new ASEntity("Customer","0071","John McDoe", "John is great");
        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\",\"description\":\"John is great\"}}}");

        ASEntity sameCustomer = new ASEntity(new EntityReference("Customer","0071"),"John McDoe", "John is great");
        Assert.assertEquals(sameCustomer.toJSON().equals(customer.toJSON()), true);
        Assert.assertEquals(sameCustomer.getStreamId().toString().equals("08c80573-6440-3c74-8fbe-e1d7738d75b3"), true);

        ASEntity customerWithRelations = new ASEntity("Customer","0071");
        customerWithRelations.addRelation(ASEntityRelationTypes.AKA,"Email/stebax@gmail.com"); //Relations :: Simple example
        customerWithRelations.addRelation(ASEntityRelationTypes.AKA,new ASEntity("Phone","+1-555011011","Johns Mobile Phone")); //Relations :: Example Using An embedded entity (They can be as complex as you like)

        //Test the JSON output
        Assert.assertEquals(customerWithRelations.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"relations\":[{\"AKA\":{\"entity_ref\":\"Email/stebax@gmail" +
                ".com\"}},{\"AKA\":{\"entity_ref\":\"Phone/+1-555011011\",\"aspects\":{\"presentation\":{\"label\":\"Johns Mobile Phone\"}}}}]}");

        //Test the Stream ID
        Assert.assertEquals(customerWithRelations.getStreamId().toString().equals("08c80573-6440-3c74-8fbe-e1d7738d75b3"), true);

    }

    @Test
    public void createEntitiesWithAspects() throws IOException {

        ASEntity customer = new ASEntity("Customer","0071","John McDoe")
                .addAspect(demography()
                        .addGender("female")
                        .addIncome("100-200")
                        .addMosaicGroup("B")
                        .addBirthYear(1999)
                        .addBirthMonth(12)
                        .addMaritalStatus("Married")
                        .addEducation("Some"));

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\"},\"demography\":{\"gender\":\"female\",\"gender_guessed\":false,\"income\":\"100-200\",\"mosaic_group\":\"B\",\"birth_year\":1999,\"birth_month\":12,\"marital_status\":\"Married\",\"education\":\"Some\"}}}");

        customer = new ASEntity("Customer","0071","John McDoe")
                .addDimension("big","one")
                .addDimension("bigger","one")
                .addMetric("bigger",1);

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\"},\"dimensions\":{\"big\":\"one\",\"bigger\":\"one\"},\"metrics\":{\"bigger\":1.0}}}");

        customer = new ASEntity("Customer","0071")
                .addAspect(presentation()
                        .addLabel("John McDoe")
                        .addIcon("some_icon.png")
                        .addDescription("John is great")
                        .addDetailsUrl("https://en.wikipedia.org/wiki/john.mcdoe")
                );

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"aspects\":{\"presentation\":{\"label\":\"John McDoe\",\"icon\":\"some_icon.png\",\"description\":\"John is great\",\"details_url\":\"https://en.wikipedia.org/wiki/john.mcdoe\"}}}");

        customer = new ASEntity("Customer","0071")
                .addRelations(ASEntityRelationTypes.AKA,"Email/john@mcdoe.com")
                .addProperties("Something",new LinkedHashMap<String,Object>(){{ put("Something","else"); }})
                .addProperties("Another","type","of",true,"property",1);

        Assert.assertEquals(customer.toJSON(),"{\"entity_ref\":\"Customer/0071\",\"relations\":[{\"AKA\":{\"entity_ref\":\"Email/john@mcdoe.com\"}}],\"properties\":{\"Something\":{\"Something\":\"else\"},\"Another\":\"type\",\"of\":true,\"property\":1}}");

        customer = new ASEntity("Customer","0071")
                .addAspect(
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
