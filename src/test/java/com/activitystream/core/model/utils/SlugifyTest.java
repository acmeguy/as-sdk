package com.activitystream.core.model.utils;

import org.junit.Assert;
import org.junit.Test;

public class SlugifyTest {

    @Test
    public void asEntityType_inputWithDash() {
        String entityType = Slugify.asEntityType("Colorado Youth Ballet Nutcracker - Box Office Cash");

        Assert.assertEquals("ColoradoYouthBalletNutcrackerBoxOfficeCash", entityType);
    }

    @Test
    public void asSlug_inputWithDash() {
        String entityType = Slugify.asSlug("Colorado Youth Ballet Nutcracker - Box Office Cash");

        Assert.assertEquals("colorado-youth-ballet-nutcracker-box-office-cash", entityType);
    }

}