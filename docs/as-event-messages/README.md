# Activity Stream :: AS Event Messages

## Introduction to AS Events
The activity stream is based on events that are sent from various business or operations systems every time something of operational or business relevance happens. 
These events are sent as event-messages directly to the AS REST API or via a messaging queue. 
Each remote-event is represented by one or more event-messages. 
The event-message is the basic structure for reporting these events and they rely on semi-structured format that demands certain compliance but also leaves plenty of room for custom information.


## AS Event message examples

### Purchasing Event

### Web Event (Generic)
```
ASEvent webVisitStarts = new ASEvent()
        .withType(ASEvent.PAST.AS_CRM_VISIT_STARTED)
        .withOrigin("wwww.mysite.domain")
        .withOccurredAt("2017-01-01T00:00:00.000Z")
        .withRelationIfValid(ASEventRelationTypes.ACTOR,"Customer","007")
        .withAspect(classification()
                .withType("virtual")
                .withVariant("web"))
        .withAspect(clientIP("127.0.0.1"))
        .withAspect(clientDevice("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"));
```
Produces this AS Event message in JSON:
```
{
  "type":"as.crm.visit.started",
  "origin":"wwww.mysite.domain",
  "occurred_at":"2017-01-01T00:00:00.000Z",
  "involves":[
    {"ACTOR":{"entity_ref":"Customer/007"}}
  ],
  "aspects":{
    "classification":{
      "type":"virtual",
      "variant":"web"
    },
    "client_ip":{
      "ip":"127.0.0.1"
    },
    "client_device":{
      "user_agent":"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
    }
  }
}

```
### Embedding Entity Information in event messages
Event messages can contain as much entity information as needed to create missing entities or to update them.
```
ASEntity customer = new ASEntity("Customer","30893928")
        .withAspect(presentation()
                .withLabel("John Doe"))
        .withRelationIfValid(ASEntityRelationTypes.AKA,"Email", "john.doe@gmail.com")
        .withRelationIfValid(ASEntityRelationTypes.AKA,"Phone", "+150012348765");

webVisitStarts = new ASEvent()
        .withType(ASEvent.PAST.AS_CRM_VISIT_STARTED)
        .withOrigin("wwww.mysite.domain")
        .withOccurredAt("2017-01-01T00:00:00.000Z")
        .withRelation(ASEventRelationTypes.ACTOR,customer)
        .withAspect(classification()
                .withType("virtual")
                .withVariant("web"))
        .withAspect(clientIP("127.0.0.1"))
        .withAspect(clientDevice("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"));
```
Produces this AS Event + AS Entity message in JSON:
```
{
  "type":"as.crm.visit.started",
  "origin":"wwww.mysite.domain",
  "occurred_at":"2017-01-01T00:00:00.000Z",
  "involves":[
    {"ACTOR":
      {
        "entity_ref":"Customer/30893928",
        "aspects":{
          "presentation":{
            "label":"John Doe"
          }
        },
        "relations":[
          {"AKA":{"entity_ref":"Email/john.doe@gmail.com"}},
          {"AKA":{"entity_ref":"Phone/+150012348765"}}
        ]
      }
    }
  ],
  "aspects":{
    "classification":{
      "type":"virtual",
        "variant":"web"
    },
    "client_ip":{
      "ip":"127.0.0.1"
    },
    "client_device":{
      "user_agent":"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
    }
  }
}
```