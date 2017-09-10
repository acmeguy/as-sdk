package com.activitystream.model;

import com.activitystream.model.stream.ImportanceLevel;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SimpleLineItemTests {

    private static final Logger logger = LoggerFactory.getLogger(SimpleLineItemTests.class);

    @Test
    public void testSimpleItemsMessage() throws IOException {

        ASEvent event = new ASEvent(
                "2017-09-01T10:10:10.010-0000",
                ASEvent.POP_TYPES.AS_COMMERCE_TRANSACTION_COMPLETED,
                "test",
                null,
                ImportanceLevel.IMPORTANT,
                "Customer/3"
                );
        Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3}"),true);

        ASLineItem itemLine = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED,new ASEntity("Event","3873"),1,30.2);

        event.addLineItem(itemLine);
        Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"item_count\":1.0,\"item_price\":30.2,\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/3873\"}}]}]}}"),true);

        //add it again to see item count go up
        event.mergeLineItem(itemLine);
        Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"item_count\":2.0,\"item_price\":30.2,\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/3873\"}}]}]}}"),true);

    }


}
