package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.PhoneAspect;
import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.Archetype;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.testng.Assert;

public class PhoneAspectTest {
    @Test
    public void testPhoneAspect() throws JsonProcessingException {
        PhoneAspect phoneAspect = new PhoneAspect();
        phoneAspect.withAreaCode("1");
        phoneAspect.withCountryCode("212");
        phoneAspect.withPhoneNumber("963-2121");
        phoneAspect.withPhoneNumberType("Home");

        ASEntity customer = new ASEntity(Archetype.PHONE, "some_hashed_id");

        /***
         * PhoneAspect should be only used if entity archetype is 'Phone'
         * ***/
        if ("Phone".equals(customer.get("archetype"))) { // Do we need to implement getArchetype method?!?
            customer.withAspect(phoneAspect);
        }

        String expected1 = "{\"archetype\":\"Phone\",\"entity_ref\":\"Phone/some_hashed_id\",\"aspects\":{\"phone\":{\"area_code\":false,\"country_code\":false,\"number\":false,\"number_type\":false}}}";
        Assert.assertEquals(customer.toJSON(), expected1);

        Assert.assertFalse(customer.getAspectManager().getPhone().isAnonymized());

        customer.getAspectManager().getPhone().withPhoneAnonymized();

        Assert.assertTrue(customer.getAspectManager().getPhone().isAnonymized());

        String expected2 = "{\"archetype\":\"Phone\",\"entity_ref\":\"Phone/some_hashed_id\",\"aspects\":{\"phone\":{\"area_code\":false,\"country_code\":false,\"number\":false,\"number_type\":false,\"is_anonymized\":true}}}";

        Assert.assertEquals(customer.toJSON(), expected2);
    }
}
