# Activity Stream :: Key Concepts

Please familiarize yourself with the following before you start constructing your event-messages.

## AS Event
The [AS Event](as-event-messages) represents some activity, event or action reported to Activity Stream by a external systems via an [AS Event Message](as-event-messages). 
Each event becomes an item in the activity stream and is associated with all the entities involved in or by the event. 
All events represent a specific Action which is created dynamically on first use (just-in-time).

## AS Entity
Good examples of business entities are Users, Products, WebPages and Orders.
Entity types and entity instances are automatically created the first time they are referenced by an event or used in a [AS Entity Message](as-entity-messages). 
That way each event becomes a part of the entities history, its activity stream, that reflects the actions reported by any event that involves the entity.

## AS Event Relations (Role)
All [AS Entities](as-entity-messages) involved in an [AS Event](as-event-messages) have a [Role](as-event-relations) that declares what part/role they had in 
the event. 
The [ACTOR](as-event-relations#actor) is, for example, the entity responsible for triggering the event and 
the [AFFECTS](as-event-relations#affects) role is used for all entities directly affected by the event while 
the [REFERENCES](as-event-relations#references) role is used for entities involved in the event but have no real bearing on the event and are not affected by
 it.

Common (Predefined) [Roles](as-event-relations) are built into Activity Stream, but [Custom Roles]
(as-event-relations#extending-links-to-create-custom-link-types) and sub-roles 
can be defined on the fly.

## AS Entity Relations (Link)
Any entity can be linked to any other entity and the link between the entities can be typed, have properties and weight. 
That way a Car cab, for example, be [BELONGS TO](as-entity-relations#belongs-to) a Customer which is in turn can be linked 1:1 with his Mobile Phones.
[Common Link types](as-entity-relations) are built into Activity Stream but custom Link-Types can be defined on the fly.

## AS Aspects
[Aspects]() are message extensions which unlock rich support in Activity Stream, both in terms of processing and presentation.

## Stream Item
All things involved in an activity stream is collectively called a Stream Item. This includes entities, events and comments.

## Customer Entity types, Roles and Links 

## Relationship-only modelling

## Time series data and the Internet of things

## 