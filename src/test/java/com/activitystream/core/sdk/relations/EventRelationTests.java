package com.activitystream.core.sdk.relations;

import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.relations.Relation;
import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.ASEvent;
import com.activitystream.sdk.ASLineItem;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;

public class EventRelationTests {

    private static final Logger logger = LoggerFactory.getLogger(EventRelationTests.class);

    @Test
    public void basicEntityRelationTest() throws IOException {

        ASEntity entity = new ASEntity("Car","IS-ST-393");

        entity.withRelation(new Relation("HAS","Driver/9833"));
        entity.withRelation("HAS","Driver/9834");
        entity.withRelationIfValid("HAS","Driver", "9835");
        entity.withRelationIfValid("HAS", new EntityReference("Driver", "9836"));
        entity.withRelationIfValid("HAS","Driver", "9837", "main",true);
        entity.withRelationIfValid("HAS","Driver", "9838", new LinkedHashMap<String,Object>(){{ put("main",false);}});

        Assert.assertEquals(entity.toJSON().equals("{\"entity_ref\":\"Car/IS-ST-393\",\"relations\":[{\"HAS\":{\"entity_ref\":\"Driver/9833\"}},{\"HAS\":{\"entity_ref\":\"Driver/9834\"}},{\"HAS\":{\"entity_ref\":\"Driver/9835\"}},{\"HAS\":{\"entity_ref\":\"Driver/9836\"}},{\"HAS\":{\"entity_ref\":\"Driver/9837\"},\"properties\":{\"main\":true}},{\"HAS\":{\"entity_ref\":\"Driver/9838\"},\"properties\":{\"main\":false}}]}"),true);
    }

    @Test
    public void basicEventRelationTest() throws IOException {

        ASEvent event = new ASEvent(ASEvent.PRE.AS_COMMERCE_PURCHASE_COMPLETED, "some.com");
        event.withOccurredAt("2017-12-10T09:19:06.285Z");

        event.withRelation(new Relation("ACTOR","Driver/9833"));
        event.withRelation("AFFECTS","CAR/IS-ST-393");
        event.withRelationIfValid("REFERENCES","Driver/9835");
        event.withRelationIfValid("REFERENCES", new EntityReference("Driver", "9836"));
        event.withRelationIfValid("REFERENCES","Driver", "9838", new LinkedHashMap<String,Object>(){{ put("main",false);}});

        Assert.assertEquals(event.toJSON().equals("{\"occurred_at\":\"2017-12-10T09:19:06.285Z\",\"type\":\"as.commerce.purchase.completed\",\"origin\":\"some.com\",\"involves\":[{\"ACTOR\":{\"entity_ref\":\"Driver/9833\"}},{\"AFFECTS\":{\"entity_ref\":\"CAR/IS-ST-393\"}},{\"REFERENCES\":{\"entity_ref\":\"Driver/9835\"}},{\"REFERENCES\":{\"entity_ref\":\"Driver/9836\"}},{\"REFERENCES\":{\"entity_ref\":\"Driver/9838\"},\"properties\":{\"main\":false}}]}"),true);
    }

    @Test
    public void basicASLineItemRelationTest() throws IOException {

        ASEntity entity = new ASEntity("Car","IS-ST-393");

        ASLineItem itemLine = new ASLineItem(ASLineItem.LINE_TYPES.REPURCHASED, entity, 1, 92829);

        itemLine.withRelation(new Relation("ACTOR","Driver/9833"));
        itemLine.withRelationIfValid("AFFECTS","CAR/IS-ST-393");
        itemLine.withRelationIfValid("REFERENCES","Driver/9835");
        itemLine.withRelationIfValid("REFERENCES", new EntityReference("Driver", "9836"));
        itemLine.withRelationIfValid("REFERENCES","Driver", "9838", new LinkedHashMap<String,Object>(){{ put("main",false);}});

        Assert.assertEquals(itemLine.toString().equals("{item_count=1.0, item_price=92829.0, involves=[Relation{entity_ref=Car/IS-ST-393, link=REPURCHASED}, Relation{entity_ref=Driver/9833, link=ACTOR}, Relation{entity_ref=CAR/IS-ST-393, link=AFFECTS}, Relation{entity_ref=Driver/9835, link=REFERENCES}, Relation{entity_ref=Driver/9836, link=REFERENCES}, Relation{entity_ref=Driver/9838, link=REFERENCES, properties={main=false}}]}"),true);
    }

}
