package com.activitystream.core.sdk.aspects;

import com.activitystream.core.model.aspects.SubscriptionAspect;
import com.activitystream.core.model.entities.EntityReference;
import com.activitystream.core.model.relations.ASEntityRelationTypes;
import com.activitystream.sdk.ASEntity;
import com.activitystream.sdk.Archetype;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class SubscriptionAspectTest {
    @Test
    public void testSubscriptionAspect() throws JsonProcessingException {
        ASEntity ticket = setUpTestTicket();

        String expected = "{\"archetype\":\"Product\",\"entity_ref\":\"Product/852456\",\"relations\":[{\"ASSOCIATED_WITH\":{\"entity_ref\":\"Event/852654\"}}],\"aspects\":{\"subscription\":{\"label\":\"2018/19 Full Season Subscription\",\"type\":\"Adult\",\"valid_from\":\"2018-07-14T10:00:00.000-04:00\",\"valid_until\":\"2018-10-14T10:00:00.000-04:00\",\"serving_count\":\"40\",\"remaining_servings\":\"10\"}}}";

        Assert.assertEquals(expected, ticket.toJSON());

        Integer servCount = 40;
        Integer remServInt = 10;

        Assert.assertEquals(ticket.getAspectManager().getSubscription().getRemainingServingsInt(), remServInt);
        Assert.assertEquals(ticket.getAspectManager().getSubscription().getServingCountInt(), servCount);

        Assert.assertEquals(ticket.getAspectManager().getSubscription().getRemainingServings(), "10");
        Assert.assertEquals(ticket.getAspectManager().getSubscription().getServingCount(), "40");

        DateTime expValidFrom = DateTime.parse("2018-07-14T10:00:00.000-04:00");
        DateTime actValidFrom = ticket.getAspectManager().getSubscription().getValidFrom();

        Assert.assertEquals(expValidFrom, actValidFrom);
    }

    private ASEntity setUpTestTicket() {
        ASEntity ticket = new ASEntity(Archetype.PRODUCT_TICKET, "852456");

        EntityReference event = new EntityReference("Event", "852654");

        ticket.withRelation(ASEntityRelationTypes.ASSOCIATED_WITH, event);

        SubscriptionAspect subscriptionAspect = new SubscriptionAspect()
                .withLabel("2018/19 Full Season Subscription")
                .withType("Adult")
                .withValidFrom("2018-07-14T10:00:00.000-04:00")
                .withValidUntil("2018-10-14T10:00:00.000-04:00")
                .withServingCount(40)
                .withRemainingServings(10);

        ticket.withAspect(subscriptionAspect);

        return ticket;
    }
}
