# Activity Stream SDK :: Getting Started 

## Aspects
Aspects offer feature rich and simple way to add commonly used information to both AS Events and AS Entities. Aspects can be seen as schema-bits/plug-ins 
that, when used, trigger extended enrichment and handling on the server side and have their own custom features for processing, presentation and storage.

##Tracked aspects

Some of the following aspects are tracked. This means that their value, and any changes, are reflected in time-series. That way, for example, the Geo 
Location Aspect can be used to store the location of a vehicle over time and the Metrics Aspect can be used to store its speed, fuel_level, temperature and 
other relevant metrics over time.
   

### AB Testing
The ab_test aspect is used to store AB Test results. Analytics for AB Tests are immediately available as well as real-time dashboards tailored for AB testing

### Address
Sets the address of a entity.
```
ASEntity venue = new ASEntity("Venue", "983983");
venue.addAspect(address()
        .addAddress("Kensington Gore")
        .addCity("Kensington")
        .addState("Greater London")
        .addPostCode("SW7 2AP")
        .addCountryCode("UK")
        .addCountry("United Kingdom"));
```
Produces this Entity message in JSON:
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "address":{
      "address":"Kensington Gore",
      "city":"Kensington",
      "state":"Greater London",
      "zip_code":"SW7 2AP",
      "country_code":"UK",
      "country":"United Kingdom"
    }
  }
}
```

### Attachments
List of attachments associated with the event.

### Classification
Used to classify entities (e.g. product, customers, suppliers etc.).

It provides a 3 level structure beneath the Entity Type (EntityType -> Classification
.type -> Classification.variant -> Classification.categories).  Properties should be filled out in this order: type, variant, categories.
```
ASEntity venue = new ASEntity("Venue", "983983");
venue.addAspect(classification()
        .addType("Theater")
        .addVariant("Concert Hall")
        .addCategories("Classical","Pop","Variety","Shows"));
```
Produces this Entity message in JSON:
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "classification":{
      "type":"Theater",
      "variant":"Concert Hall",
      "categories":["Classical","Pop","Variety","Shows"]
    }
  }
}
```
### Client Device
Based on user_agent string and is only used for web/browser originated events.

### Client IP
If the event is originated over public Internet and the IP address is provided then Activity Stream supplies the specifics that can be resolved from the IP address.

### Customer Experience Index
Each event can have synthesized/imagined effect on the entities affected by it. When applicable it can, for example, be used to estimate the current state of a customer based on the things in his activity stream.
The defaults can be stored for each Event-Type but they can also be explicitly defined for individual events using this aspect.

### Demography
Used to store demography information for an entity.

### Dimensions *(tracked)*
Generic store for ad-hoc dimensions.

### Geo Location *(tracked)*
If an event specifies a (geo) location then Activity Stream resolves it and provides additional information for it.
After enrichment* the aspect returns location specific information.

### Items
Used to represent line-items or transaction items for commerce.
```
        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent purchaseEvent = new ASEvent(ASEvent.POP_TYPES.AS_COMMERCE_TRANSACTION_COMPLETED, "www.web");
        purchaseEvent.addOccurredAt("2017-01-01T12:00:00")
                .addRelationIfValid(ASConstants.REL_BUYER,"Customer/983938")
                .addAspect(items()
                        .addLine(lineItem()
                                .addTransactionType(ASLineItem.LINE_TYPES.PURCHASED)
                                .addItemCount(1)
                                .addItemPrice(32.5)
                                .addProduct(ASLineItem.LINE_TYPES.PURCHASED, new ASEntity("Event/398928"))
                                .addPriceCategory("Section A")
                                .addPriceType("Seniors")
                                .addVariant("VIP")
                                .markAsComplimentary()
                        )
                );
```
Produces this Event message in JSON:
```
{
  "occurred_at": "2017-01-01T12:00:00.000Z",
  "type": "as.commerce.transaction.completed",
  "origin": "www.web",
  "involves": [
    {
      "BUYER": {
        "entity_ref": "Customer/983938"
      }
    }
  ],
  "importance": 3,
  "aspects": {
    "items": [
      {
        "currency": "USD",
        "item_count": 1.0,
        "item_price": 0.0,
        "involves": [
          {
            "PURCHASED": {
              "entity_ref": "Event/398928"
            }
          }
        ],
        "price_category": "Section A",
        "price_type": "Seniors",
        "variant": "VIP",
        "complementary": true
      }
    ]
  }
}
```

### Metrics *(tracked)*
Generic store for ad-hoc metrics.
```
    ASEntity venue = new ASEntity("Venue", "983983");
    venue.addMetric("built",1941, "capacity", 5272);
```
Produces this Entity message in JSON:
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "metrics":{
      "built":1941.0,
      "capacity":5272.0
    }
  }
}
```
Metric values are tracked. It means that any changes in value is stored and that it can then be queried as any other time-series in our real-time analytics. 
store.
If a timestamp is provided then the time-series datapoint will be stored using that time but it defaults to current time. 
```
ASEntity venue = new ASEntity("Venue", "983983")
        .addMetric("built",1941, "capacity", 5272)
        .addOccurredAt("2017-10-31T12:00:00-01:00");
```
Produces this Entity message in JSON and forces a data-point to be created with a specific time. 
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "metrics":{
      "built":1941.0,
      "capacity":5272.0
    },
  "status":{
    "update_occurred_at":"2017-10-31T12:00:00.000-01:00"}
  }
}
```
Metrics are created on demand and there is no practical limit to the number of metric/value pairs that can be stored.

### Presentation
Commonly used fields to display human-readable entity information but applies to events as well.
```
ASEntity venue = new ASEntity("Venue", "983983")
    .addAspect(presentation()
        .addLabel("Royal Albert Hall")
        .addDescription("The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.")
        .addDetailsUrl("https://www.royalalberthall.com/")
        .addThumbnail("https://cdn.royalalberthall.com/file/1396975600/32830992449")
        .addIcon("ol-venue"));
```
Produces this Entity messages in JSON:
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "presentation":{
      "label":"Royal Albert Hall",
      "description":"The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.",
      "details_url":"https://www.royalalberthall.com/",
      "thumbnail":"https://cdn.royalalberthall.com/file/1396975600/32830992449",
      "icon":"ol-venue"
    }
  }
}
```

### Tags
An array of strings used to further classify events in the activity stream. You can use any tag you like but keep in mind that a small set (low cardinality) of tags is commonly more useful than a large set of tags.
```
ASEntity venue = new ASEntity("Venue", "983983");
venue.addAspect(tags().addTags("National"));
```
Produces this Entity messages in JSON:
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "tags":["National"]
  }
}
```

### Timed
Used to store timestamps or periods associated with an entity or an event.

**Please note**: The Timed aspect is not design to store rough periods even though it can. (All dates are parsed and converted to timestamps)

Reserved period types (lower-case "slugs"):
- begins (used as the main period of an event)   
- on_sale (example timed-type for ticketing)   
- doors_open (example timed-type for ticketing)   
```
ASEntity venue = new ASEntity("Venue", "983983");
venue.addAspect(timed()
        .addPeriod("construction","1867","1871")
        .addPeriod("inaugurated","1871-03-29")
        .addPeriod("renovated","1996","2004")
);
```
Produces this Entity messages in JSON: 
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "timed":{
      "construction":{
        "begins":"1867-01-01T00:00:00.000Z",
        "ends":"1871-01-01T00:00:00.000Z",
        "duration":126230400000
      },
      "inaugurated":{
        "begins":"1871-03-29T00:00:00.000Z"
      },
      "renovated":{
        "begins":"1996-01-01T00:00:00.000Z",
        "ends":"2004-01-01T00:00:00.000Z",
        "duration":252460800000
      }
    }
  }
}
```

### Traffic Source
Used to track campaign and origin information. It's mostly used for web traffic but can be used for any event.
