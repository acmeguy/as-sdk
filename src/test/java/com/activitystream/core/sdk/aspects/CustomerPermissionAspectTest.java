package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.CustomerPermissionAspect;
import com.activitystream.sdk.ASEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

public class CustomerPermissionAspectTest {
    @Test
    public void testCreateCustomerPermissionAspect() throws JsonProcessingException {
        ASEntity customer = new ASEntity().withAspect(new CustomerPermissionAspect(true, false, false));

        Assert.assertEquals("{\"aspects\":{\"customer_permission\":{\"marketing_permission\":true,\"information_permission\":false,\"processing_permission\":false}}}", customer.toJSON());
        Assert.assertTrue(customer.getAspectManager().getCustomerPermission().getMarketingPermission());
        Assert.assertFalse(customer.getAspectManager().getCustomerPermission().getInformationPermission());

        customer.getAspectManager().getCustomerPermission().withProcessingPermission(true);
        Assert.assertTrue(customer.getAspectManager().getCustomerPermission().getProcessingPermission());
    }
}
