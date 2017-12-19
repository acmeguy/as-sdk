package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.ItemsManager;
import com.activitystream.core.model.relations.ASEventRelationTypes;
import com.activitystream.sdk.ASConstants;
import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.ASEvent;
import com.activitystream.sdk.ASLineItem;
import com.activitystream.sdk.ASService;
import com.activitystream.core.model.stream.ImportanceLevel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.util.TimeZone;

import static com.activitystream.sdk.ASLineItem.lineItem;
import static com.activitystream.core.model.aspects.ItemsManager.items;

public class ItemsAspectTests {

    private static final Logger logger = LoggerFactory.getLogger(ItemsAspectTests.class);

    @BeforeMethod(alwaysRun = true)

    @Test
    public void simplePurchaseMessage() throws Exception {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent purchaseEvent = new ASEvent(ASEvent.PRE.AS_COMMERCE_PURCHASE_COMPLETED, "www.web");
        purchaseEvent.withOccurredAt("2017-01-01T12:00:00")
                .withRelationIfValid(ASConstants.REL_BUYER,"Customer/983938");
        purchaseEvent
                .withAspect(items(purchaseEvent)
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

        Assert.assertEquals(purchaseEvent.toJSON().equals("{\"occurred_at\":\"2017-01-01T12:00:00.000Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"www.web\",\"involves\":[{\"BUYER\":{\"entity_ref\":\"Customer/983938\"}}],\"aspects\":{\"items\":[{\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/398928\"}}],\"item_count\":1.0,\"item_price\":0.0,\"price_category\":\"Section A\",\"price_type\":\"Seniors\",\"variant\":\"VIP\",\"complimentary\":true}]}}"),true);
        ASEvent parsedEvent = ASEvent.fromJSON(purchaseEvent.toJSON());

        //Round-trip test
        Assert.assertEquals(purchaseEvent.toJSON().equals(parsedEvent.toJSON()),true);
        Assert.assertEquals(purchaseEvent.getStreamId().equals(parsedEvent.getStreamId()),true);

        //Stream IDs are always calculated the same way so they are deterministic.
        Assert.assertEquals(purchaseEvent.getStreamId().toString().equals("6dedc893-fc58-3b22-92ab-4f843d4b5c0e"),true);

    }

    @Test
    public void testSimpleItemsMessage() throws IOException {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent event = new ASEvent(
                "2017-09-01T10:10:10.010-0000",
                ASEvent.PRE.AS_COMMERCE_PURCHASE_COMPLETED,
                "test",
                null,
                ImportanceLevel.IMPORTANT,
                "Customer/3"
        );
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3}"),true);

        ASLineItem itemLine = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED,new ASEntity("Event","3873"),1,30.2);

        event.addLineItem(itemLine);
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"item_count\":1.0,\"item_price\":30.2,\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/3873\"}}]}]}}"),true);

        //add it again to see item count go up
        event.mergeLineItem(itemLine);
        logger.warn("event.toJSON() " + event.toJSON());
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"item_count\":2.0,\"item_price\":30.2,\"involves\":[{\"PURCHASED\":{\"entity_ref\":\"Event/3873\"}}]}]}}"),true);
    }

    @Test
    public void testSimpleItemsReturnedMessage() throws IOException {

        ASService.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent event = new ASEvent(
                "2017-09-01T10:10:10.010-0000",
                ASEvent.PRE.AS_COMMERCE_PURCHASE_COMPLETED,
                "test",
                null,
                ImportanceLevel.IMPORTANT,
                "Customer/3"
        );
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3}"),true);

        event.addLineItem(
                lineItem()
                        .withProduct(ASLineItem.LINE_TYPES.RETURNED, new ASEntity("Event","3873"))
                        .withItemCount(1)
                        .withItemPrice(30.2)
        );
        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"involves\":[{\"RETURNED\":{\"entity_ref\":\"Event/3873\"}}],\"item_count\":1.0,\"item_price\":30.2}]}}"),true);

        //add the same product again item count go up
        event.mergeLineItem(
                lineItem()
                        .withProduct(ASLineItem.LINE_TYPES.RETURNED, new ASEntity("Event","3873"))
                        .withItemCount(1)
                        .withItemPrice(30.2)
        );

        org.junit.Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-09-01T10:10:10.010Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"test\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Customer/3\"}}],\"importance\":3,\"aspects\":{\"items\":[{\"involves\":[{\"RETURNED\":{\"entity_ref\":\"Event/3873\"}}],\"item_count\":2.0,\"item_price\":30.2}]}}"),true);
    }

    @Test
    public void testItemsMultipleLinedIds() {
        ASEntity show = new ASEntity("Show", "1");

        ASLineItem lineItem1 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50").withLineId("1");
        ASLineItem lineItem2 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50").withLineId("2");
        ASLineItem lineItem3 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "550").withLineId("3");

        ItemsManager items = new ItemsManager();
        items.mergeItemLine(lineItem1);
        items.mergeItemLine(lineItem2);
        items.mergeItemLine(lineItem3);

        org.junit.Assert.assertEquals(items.toString().equals("[{involves=[Relation{entity_ref=Show/1, link=PURCHASED}], item_count=2.0, item_price=50.0, line_ids=[1, 2]}, {involves=[Relation{entity_ref=Show/1, link=PURCHASED}], item_count=1.0, item_price=550.0, line_ids=[3]}]"),true);
    }

    @Test
    public void testItemsMultipleTickets() {
        ASEntity show = new ASEntity("Show", "1");

        ASLineItem lineItem1 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50").withRelationIfValid(ASEventRelationTypes.AFFECTS + ":CREATES", "Ticket", "123");
        ASLineItem lineItem2 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50").withRelationIfValid(ASEventRelationTypes.AFFECTS + ":CREATES", "Ticket", "456");
        ASLineItem lineItem3 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "550").withRelationIfValid(ASEventRelationTypes.AFFECTS + ":CREATES", "Ticket", "789");

        ItemsManager items = new ItemsManager();
        items.mergeItemLine(lineItem1);
        items.mergeItemLine(lineItem2);
        items.mergeItemLine(lineItem3);

        org.junit.Assert.assertEquals("[{involves=[Relation{entity_ref=Show/1, link=PURCHASED}, Relation{entity_ref=Ticket/123, link=AFFECTS:CREATES}, Relation{entity_ref=Ticket/456, link=AFFECTS:CREATES}], item_count=2.0, item_price=50.0}, {involves=[Relation{entity_ref=Show/1, link=PURCHASED}, Relation{entity_ref=Ticket/789, link=AFFECTS:CREATES}], item_count=1.0, item_price=550.0}]", items.toString());
    }

    @Test
    public void testItemsDuplicateRelation() {
        ASEntity show = new ASEntity("Show", "1");

        ASLineItem lineItem1 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50").withRelationIfValid(ASEventRelationTypes.AFFECTS, "Barcode", "123");
        ASLineItem lineItem2 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50").withRelationIfValid(ASEventRelationTypes.AFFECTS, "Barcode", "123");
        ASLineItem lineItem3 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50").withRelationIfValid(ASEventRelationTypes.AFFECTS, "Barcode", "456");
        ASLineItem lineItem4 = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "550").withRelationIfValid(ASEventRelationTypes.AFFECTS, "Barcode", "789");

        ItemsManager items = new ItemsManager();
        items.mergeItemLine(lineItem1);
        items.mergeItemLine(lineItem2);
        items.mergeItemLine(lineItem3);
        items.mergeItemLine(lineItem4);

        org.junit.Assert.assertEquals("[{involves=[Relation{entity_ref=Show/1, link=PURCHASED}, Relation{entity_ref=Barcode/123, link=AFFECTS}, Relation{entity_ref=Barcode/456, link=AFFECTS}], item_count=3.0, item_price=50.0}, {involves=[Relation{entity_ref=Show/1, link=PURCHASED}, Relation{entity_ref=Barcode/789, link=AFFECTS}], item_count=1.0, item_price=550.0}]", items.toString());
    }

    @Test
    public void testMergeLineItems() {

        ASEntity show = new ASEntity("Show", "1");
        ItemsManager items = new ItemsManager();

        for (int i = 0; i<10; i++) {
            ASLineItem lineItem = new ASLineItem(ASLineItem.LINE_TYPES.PURCHASED, show, "1", "50")
                    .withPriceType("Children").withPriceCategory("General Admission").withComplimentary(true).withFixedCommission("1.0")
                    .withDescription("Desc").withTax(0.1).withValidFrom(DateTime.parse("2017-06-30T01:20+00:00"))
                    .withVariant("Var").withDimensions("Section", "Section1")
                    .withValidUntil(DateTime.parse("2018-06-30T01:20+00:00"));

            items.mergeItemLine(lineItem);
        }

        org.junit.Assert.assertEquals("[{involves=[Relation{entity_ref=Show/1, link=PURCHASED}], item_count=10.0, item_price=0.0, price_type=Children, price_category=General Admission, complimentary=true, commission_fixed=1.0, description=Desc, tax_percentage=0.1, valid_from=2017-06-30T01:20:00.000Z, variant=Var, aspects={dimensions={section=Section1}}, valid_until=2018-06-30T01:20:00.000Z}]", items.toString());
    }
}
