#Activity Stream SDK :: Entity Messages

##Introduction to AS Entities
Entities (Business objects) represent any object that is referenced by events in the activity stream. 
An insurance company would typically, and at least, have Customer, Vehicle, Policy and Claim as entity-types and numerous entities of each entity-type.

Entities, as well as entity-types, are created automatically the first time they are referenced by an event but they can also be created explicitly using REST or by sending entity information in a dedicated entity-message to the message queue.

Additional entity information
Any JSON information can be stored as the properties of a business entity and when the aspects are used to store them then the entity can be represented by Activity Stream and meaningful information from the entity can be used while processing associated business events.

Unlimited entity relations
Entities are stored in a graph where even the most complicated relations between entities can be stored in a simple way. 
By using the same kind of approach as social-networking sites use to store different types of friendships, and who is following who, we are able to link any entity with any other entity where the relationship types can be customized on the fly.

See Entity Relations for details on how to link and unlink entities

Full referential integrity
Entities can not be deleted from the Entity Graph but access to them can be controlled using an Access Control List (see the ACL documentation for details).

Event Sourcing - The ultimate time-machine
Entities are only updated with event/entity messages and each alteration is stored as an event in the activity stream. This allows us to know the state of the entity at any point in time.