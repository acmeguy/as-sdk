package com.activitystream.model.config;

import com.activitystream.model.ASConstants;
import com.activitystream.model.ASEvent;
import com.activitystream.model.ASLineItem;
import com.activitystream.model.aspects.ItemsManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import java.util.TimeZone;

/**
 * @author ivan
 */
public class ASConfigTest {

    @Test
    public void setDefaultTimeZone() throws JsonProcessingException {
        ASConfig.setDefaults(null, null, TimeZone.getTimeZone("GMT+6:00"));

        ASEvent event = new ASEvent();
        event.withOccurredAt("2017-08-29T05:46:53+03:00");
        Assert.assertTrue(event.toJSON().contains("2017-08-29T05:46:53.000+03:00"));
    }

    @Test
    public void setDefaultTimeZoneWithoutZone() throws JsonProcessingException {
        ASConfig.setDefaults(null, null, TimeZone.getTimeZone("GMT+6:00"));

        ASEvent event = new ASEvent();
        event.withOccurredAt("2017-08-29T05:46:53");
        Assert.assertTrue(event.toJSON().contains("2017-08-29T05:46:53.000+06:00"));
    }

    @Test
    public void setDefaultCurrency() throws JsonProcessingException {
        ASConfig.setDefaults(null, "USD", null);

        ASEvent event = new ASEvent();
        ItemsManager itemsManager = new ItemsManager();
        ASLineItem lineItem = new ASLineItem();
        lineItem.withRelationIfValid(ASConstants.REL_TRADE, "Show", "123");
        itemsManager.mergeItemLine(lineItem);
        event.withAspect(itemsManager);

        Assert.assertTrue(event.toJSON().contains("USD"));
        Assert.assertFalse(event.toJSON().contains("EUR"));
    }

    @Test
    public void setCurrencyExplicitly() throws JsonProcessingException {
        ASConfig.setDefaults(null, "USD", null);

        ASEvent event = new ASEvent();
        ItemsManager itemsManager = new ItemsManager();
        ASLineItem lineItem = new ASLineItem();
        lineItem.setCurrency("EUR");
        lineItem.withRelationIfValid(ASConstants.REL_TRADE, "Show", "123");
        itemsManager.mergeItemLine(lineItem);
        event.withAspect(itemsManager);

        Assert.assertTrue(event.toJSON().contains("EUR"));
        Assert.assertFalse(event.toJSON().contains("USD"));
    }

}