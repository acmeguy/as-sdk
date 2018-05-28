package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.EmailAspect;
import org.junit.Test;
import org.testng.Assert;

public class EmailAspectTest {
    @Test
    public void testEmailAspect() {
        EmailAspect emailAspect1 = new EmailAspect();
        emailAspect1.withEmail("bugs.bunny@email.com");
        emailAspect1.withEmailAnonymized();

        String expected = "{address=bugs.bunny@email.com, is_anonymized=true}";

        Assert.assertEquals(emailAspect1.toString(), expected);

        Assert.assertTrue(emailAspect1.isAnonymized());

        EmailAspect emailAspect2 = new EmailAspect("peter.parker@email.com");

        Assert.assertEquals(emailAspect2.getEmail(), "peter.parker@email.com");
        Assert.assertFalse(emailAspect2.isAnonymized());
    }
}
