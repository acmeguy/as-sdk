package com.activitystream.model.aspects;

import com.activitystream.model.ASConstants;
import com.activitystream.model.ASEntity;
import com.activitystream.model.ASEvent;
import com.activitystream.model.ASLineItem;
import com.activitystream.model.config.ASConfig;
import com.activitystream.model.stream.ImportanceLevel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.util.TimeZone;

import static com.activitystream.model.ASLineItem.lineItem;
import static com.activitystream.model.aspects.ItemsManager.items;

public class ItemsAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ItemsAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simplePurchaseMessage() throws Exception {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent purchaseEvent = new ASEvent(ASEvent.PRE.AS_COMMERCE_TRANSACTION_COMPLETED, "www.web");
        purchaseEvent.withOccurredAt("2017-01-01T12:00:00")
                .withRelationIfValid(ASConstants.REL_BUYER,"Customer/983938")
                .withAspect(items()
                        .addLine(lineItem()
                                .withProduct(ASLineItem.LINE_TYPES.PURCHASED, new ASEntity("Event/398928"))
                                .withItemCount(1)
                                .withItemPrice(32.5)
                                .withPriceCategory("Section A")
                                .withPriceType("Seniors")
                                .withVariant("VIP")
                                .withAsComplimentary()
                        )
                );

        Assert.assertEquals(purchaseEvent.toJSON().equals("{\"occurred_at\":\"2017-01-01T12:00:00.000Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"www.web\",\"involves\":[{\"BUYER\":{\"entity_ref\":\"Customer/983938\"}}],\"aspects\":{\"items\":[{\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/398928\"}}],\"item_count\":1.0,\"item_price\":0.0,\"price_category\":\"Section A\",\"price_type\":\"Seniors\",\"variant\":\"VIP\",\"complimentary\":true}]}}"),true);

        ASEvent parsedEvent = ASEvent.fromJSON(purchaseEvent.toJSON());
        //Round-trip test
        logger.warn("purchaseEvent.toJSON(): " + purchaseEvent.toJSON());
        logger.warn("parsedEvent.toJSON()  : " + parsedEvent.toJSON());
        Assert.assertEquals(purchaseEvent.toJSON().equals(parsedEvent.toJSON()),true);

        logger.warn("purchaseEvent.getStreamId() " + purchaseEvent.getStreamId());
        logger.warn("parsedEvent.getStreamId() " + parsedEvent.getStreamId());

        //todo - Pétur, please help me figure out why this test fails
        //Assert.assertEquals(purchaseEvent.getStreamId().equals(parsedEvent.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(purchaseEvent.getStreamId().toString().equals("1854e4a6-e4af-3696-bb93-dbbacdd19cb3"),true);

    }

    @Test
    public void testSimpleItemsMessage() throws IOException {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent event = new ASEvent(
                "2017-09-01T10:10:10.010-0000",
                ASEvent.PRE.AS_COMMERCE_TRANSACTION_COMPLETED,
                "test",
                null,
                ImportanceLevel.IMPORTANT,
                "Customer/3"
        );
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3}"),true);

        ASLineItem itemLine = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED,new ASEntity("Event","3873"),1,30.2);

        event.addLineItem(itemLine);
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"item_count\":1.0,\"item_price\":30.2,\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/3873\"}}]}]}}"),true);

        //add it again to see item count go up
        event.mergeLineItem(itemLine);
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"item_count\":2.0,\"item_price\":30.2,\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/3873\"}}]}]}}"),true);
    }

    @Test
    public void testSimpleItemsReturnedMessage() throws IOException {

        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent event = new ASEvent(
                "2017-09-01T10:10:10.010-0000",
                ASEvent.PRE.AS_COMMERCE_TRANSACTION_COMPLETED,
                "test",
                null,
                ImportanceLevel.IMPORTANT,
                "Customer/3"
        );
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3}"),true);

        event.addLineItem(
                lineItem()
                        .withProduct(ASLineItem.LINE_TYPES.RETURNED, new ASEntity("Event","3873"))
                        .withItemCount(1)
                        .withItemPrice(30.2)
        );
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"involves\":[{\"RETURNED\":{\"entity_ref\":\"Event/3873\"}}],\"item_count\":1.0,\"item_price\":30.2}]}}"),true);

        //add the same product again item count go up
        event.mergeLineItem(
                lineItem()
                        .withProduct(ASLineItem.LINE_TYPES.RETURNED, new ASEntity("Event","3873"))
                        .withItemCount(1)
                        .withItemPrice(30.2)
        );

        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.transaction.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"involves\":[{\"RETURNED\":{\"entity_ref\":\"Event/3873\"}}],\"item_count\":2.0,\"item_price\":30.2}]}}"),true);
    }


}
