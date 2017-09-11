# Activity Stream SDK :: Getting Started 

## Introduction
This SDK currently contains only a Java version but other versions will be available soon with c#, python and go planned for the 3nd wave.
The examples in this documentation will be updated to include versions in all supported languages and the 
[as-sdk-examples](https://github.com/activitystream/as-sdk-examples) project will contain reusable examples in these languages as well.

This short getting started tutorial demonstrates how to construct messages ready to be sent to Activity Stream.  

## Initializing the SDK
You can set the default country_code, currency and timezone before using the SDK. This can be handy if your data, for example, contains only local time 
DateTimes and you want to make sure that the correct timezone information is retained in Activity Stream. 
```
ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+6:00"));
```

## Composing a AS Event message
Event messages are used to report any business activity that should be logged in Activity Stream.
```
ASEvent asEvent = new ASEvent()
        .addType("as.application.authentication.login")
        .addOrigin("your.web.application")
        .addOccurredAt("2017-01-01T00:00:00.000Z")
        .addImportance(ImportanceLevel.NOT_IMPORTANT)
        .addRelationIfValid(ASEventRelationTypes.ACTOR, "Customer","314");
```
This simple login message produces the following JSON structure which is a valid Activity Stream event messages.
```
asEvent.toJson() -> 
{
  "type":"as.application.authentication.login",
  "origin":"your.web.application",
  "occurred_at":"2017-01-01T00:00:00.000Z",
  "importance":2, 
  "involves": [
    {
      "ACTOR":{
        "entity_ref":"Customer/314"
      }
    }
  ]
}
``` 
Pleas read the [Introduction to AS Event messages](as-event-messages) for more information.

### Basic Event Relation Types (Roles)
Event relations are used to link all related entities to an event. There is no limit to how many business entities are involved in an event and their Role
 in the event may vary. Here is a list of the basic, extendable, Role types:
  
  - [ACTOR](as-event-relations#actor) (Required) 
  - [AFFECTS](as-event-relations#affects) 
  - [INVOLVES](as-event-relations#involves)
  - [REFERENCES](as-event-relations#references)
  - [OBSERVES](as-event-relations#observes)

## Composing a AS Entity Message
Entity messages are used to send information regarding business entities to Activity Stream but entity information can also be embedded in event messages. 
Entity messages can also be used to collect time series information for any business entity.  
```
asEntity = new ASEntity("Customer","0071")
        .addAspect(presentation()
                .addLabel("John McDoe")
                .addDescription("John is great")
                .addDetailsUrl("https://en.wikipedia.org/wiki/john.mcdoe")
        );
```
This simple entity message produces the following JSON structure which is a valid Activity Stream entity messages.
```
asEntity.toJson() ->
{
  "entity_ref":"Customer/0071",
  "aspects":{
    "presentation":{
      "label":"John McDoe",
      "description":"John is great",
      "details_url":"https://en.wikipedia.org/wiki/john.mcdoe"
    }
  }
}
``` 
Pleas read the [Introduction to AS Entity messages](as-entity-messages) for more information.

### Basic Entity Relation Types (Links) 
Entity relations are used to link together entities in a symbolic way. An entity can be linked to any other business entity in Activity Stream. Relationship 
types can be extended, times, wighted, distrusted or promoted.

The primary relationship types are:   

  - [IS](as-entity-relations#is) 
  - [CLOSE](as-entity-relations#close)
  - [PART_OF](as-entity-relations#part-of)
  - [KNOWS](as-entity-relations#knows) 
  - [KNOWS_OF](as-entity-relations#knows-of)


## Composing a Event message with embedded Entity information
Entity information can easily be embedded in entity meassages and there is no limit to the levels of nesting.
```
customer = new ASEntity("Customer","0071")
        .addAspect(presentation()
                .addLabel("John McDoe")
                .addDescription("John is great")
                .addDetailsUrl("https://en.wikipedia.org/wiki/john.mcdoe")
        );

ASEvent asEvent = new ASEvent()
        .addType("as.application.authentication.login")
        .addOrigin("your.web.application")
        .addOccurredAt("2017-01-01T00:00:00.000Z")
        .addImportance(ImportanceLevel.NOT_IMPORTANT)
        .addRelationIfValid(ASEventRelationTypes.ACTOR, customer);
```
This event message produces the following JSON structure:
```
{
  "type":"as.application.authentication.login",
  "origin":"your.web.application",
  "occurred_at":"2017-01-01T00:00:00.000Z",
  "importance":2, 
  "involves": [
    {
      "ACTOR":{
        {
          "entity_ref":"Customer/0071",
          "aspects":{
            "presentation":{
              "label":"John McDoe",
              "description":"John is great",
              "details_url":"https://en.wikipedia.org/wiki/john.mcdoe"
            }
          }
        }      
      }
    }
  ]
}

```
Entities can contain there own relations to other entities but only one event can be constructed and reported at once (using a single event message).
  
## What are Aspects 
Aspects offer feature rich and simple way to add commonly used information to both AS Events and AS Entities. Aspects can be seen as schema-bits/plug-ins 
that, when used, trigger extended enrichment and handling on the server side and have their own custom features for processing, presentation and storage.
  
### Event Aspects
A list of aspects that can be attached to any AS Event message 

  - [AB Testing](as-aspects#ab-testing)  
  - [Attachments](as-aspects#attachemnts)  
  - [CEI](as-aspects#cei)  
  - [Client IP](as-aspects#client-ip)  
  - [Client Device](as-aspects#client-device)  
  - [Dimensions](as-aspects#dimensions)    
  - [Geo Location](as-aspects#geo-location)    
  - [Items](as-aspects#items)
  - [Metrics](as-aspects#metrics)
  - [Tags](as-aspects#tags)
  - [Times](as-aspects#times)
  - [Traffic Source](as-aspects#traffic-sources)

Please refer type the [AS Entity relationship types]() for further information

### Entity Aspects
A list of aspects that can be attached to any AS Entity

  - [Attachments](as-aspects#attachemnts)  
  - [Address](as-aspects#address)  
  - [Classification](as-aspects#classification)    
  - [Demography](as-aspects#demography)    
  - [Dimensions](as-aspects#dimensions)    
  - [Geo Location](as-aspects#geo-location)    
  - [Metrics](as-aspects#metrics)
  - [Times](as-aspects#times)

## Sending messages to Activity Stream
This SDK does not cover sending messages to Activity Stream as that may vary. Please checkout the [SDK Examples](https://github
.com/activitystream/as-sdk-examples) to get various examples on how to do that view AMQP, HTTP etc.