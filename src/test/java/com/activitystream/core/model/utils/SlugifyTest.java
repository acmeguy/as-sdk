package com.activitystream.core.model.utils;

import org.junit.Assert;
import org.junit.Test;

public class SlugifyTest {

    @Test
    public void asEntityType_inputWithDash() {
        String entityType = Slugify.asEntityType("Colorado Youth Ballet Nutcracker - Box office cash");

        Assert.assertEquals("ColoradoYouthBalletNutcrackerBoxOfficeCash", entityType);
    }

    @Test
    public void asSlug_inputWithDash() {
        String entityType = Slugify.asSlug("Colorado Youth Ballet Nutcracker - Box office cash");

        Assert.assertEquals("colorado-youth-ballet-nutcracker-box-office-cash", entityType);
    }

    @Test
    public void asEntityType_inputWithColon() {
        String entityType = Slugify.asEntityType("Fee : Internal fee");

        Assert.assertEquals("Fee:InternalFee", entityType);
    }

    @Test
    public void asSlug_inputWithColon() {
        String entityType = Slugify.asSlug("Fee : Internal fee");

        Assert.assertEquals("fee-internal-fee", entityType);
    }


    @Test
    public void asEntityType_valid() {
        String entityType = Slugify.asEntityType("SeatingArrangement");

        Assert.assertEquals("SeatingArrangement", entityType);
    }

}