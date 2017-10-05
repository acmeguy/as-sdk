# Activity Stream :: AS Aspects 

## Introduction and index
Aspects offer feature rich and simple way to add commonly used information to both AS Events and AS Entities. Aspects can be seen as schema-bits/plug-ins 
that, when used, trigger extended enrichment and handling on the server side and have their own custom features for processing, presentation and storage.

Some aspects are used for both AS Events and AS Entities but some are dedicated to either one.

| AS Event Aspects | AS Entity Aspects |
| ------------- | ------------- |
| [AB Testing](#ab-testing) `tracked*` | [Address](#address)  |
| [Attachments](#attachments) | [Attachments](#attachments)  |
| [Classification](#classification) | [Classification](#classification)  |
| [Client Device](#clientdevice) | [Demography](#demography)  |
| [Client IP](#clientip) | [Dimensions](#dimensions) |
| [Customer Experience Index](as-aspects#cei) `tracked` | [Geo location](#geo-location) `tracked` |
| [Dimensions](#dimensions) `tracked` | [Metrics](#metrics) `tracked` |
| [Geo location](#geo-location) `tracked` | Identifiable |
| Identifiable  | [Inventory](#inventory) `tracked` |
| [Items](#items)  | [Presentation](#presentation) |
| [Metrics](#metrics) | [Properties](#properties) |
| [Presentation](#presentation) | [Tags](#tags) |
| [Properties](#properties) | [Timed](#timed) |
| [Tags](#tags) |  |
| [Timed](#timed) |  |
| [Traffic Source](#traffic-source) |  |

\* Some aspects are tracked. This means that their value, and any changes to them, are reflected in time-series. That way, for example, the Geo 
Location Aspect can be used to store the location of a vehicle over time and the Metrics Aspect can be used to store its speed, fuel_level, temperature and 
other relevant metrics over time.

  

### AB Testing
The ab_test aspect is used to store AB Test results. Analytics for AB Tests are immediately available as well as real-time dashboards tailored for AB testing

### Address
Sets the address of a entity.
```
ASEntity venue = new ASEntity("Venue", "983983");
venue.withAspect(address()
        .withAddress("Kensington Gore")
        .withCity("Kensington")
        .withState("Greater London")
        .withPostCode("SW7 2AP")
        .withCountryCode("UK")
        .withCountry("United Kingdom"));
```
Produces this AS Entity message in JSON:
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
venue.withAspect(classification()
        .withType("Theater")
        .withVariant("Concert Hall")
        .withCategories("Classical","Pop","Variety","Shows"));
```
Produces this AS Entity message in JSON:
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

```
ASEntity secretAgent = new ASEntity("Agent", "007");
secretAgent.withAspect(demography()
        .withGender("male")
        .withBirthDate("1968-04-13")
        .withEmployment("Governmental Employee")
        .withEthnicity("Caucasian")
        .withMaritalStatus("Very Single")
        .withHousing("Lives alone")
        .withMosaicGroup("Wealthy World Traveller")
        .withEducation("Like to have a degree")
        .withIncome("400k$ - 800k$"));
```
Produces this AS Entity message in JSON:
```
{
  "entity_ref":"Agent/007",
  "aspects":{
    "demography":{
      "gender":"male",
      "gender_guessed":false,
      "birth_year":1968,
      "birth_month":4,
      "birth_day":13,
      "employment":"Governmental Employee",
      "ethnicity":"Caucasian",
      "marital_status":"Very Single",
      "income":"400k$ - 800k$",
      "mosaic_group":"Wealthy World Traveller",
      "education":"Like to have a degree"
    }
  }
}
```

### Dimensions
Generic store for ad-hoc dimensions.
All values in dimensions are added to all time-series that are created for the AS Event or the AS Entity. In that way AB test results, page-views, purchases or 
any other aspect that automatically generated time-series dynamically get extra dimensions for slicing and dicing.
```
ASEntity venue = new ASEntity("Venue", "983983");
venue.withDimension("house_color","white");
venue.withDimensions("door_faces","north", "door_color","brown");
```
Produces this AS Entity message in JSON:
```
{
  "entity_ref":"Venue/983983",
  "aspects":{
    "dimensions":{
      "house_color":"white",
      "door_faces":"north",
      "door_color":"brown"
    }
  }
}
```
### Geo Location
If an event specifies a (geo) location then Activity Stream resolves it and provides additional information for it.
After enrichment* the aspect returns location specific information.

### Identifiable
Used to assign external IDs to AS Events and AS Entities. This allows an external system to look up individual stream items (Events or Entities) 

### Items
Used to represent line-items or transaction items for commerce.
```
        ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));

        ASEvent purchaseEvent = new ASEvent(ASEvent.POP_TYPES.AS_COMMERCE_TRANSACTION_COMPLETED, "www.web");
        purchaseEvent.withOccurredAt("2017-01-01T12:00:00")
                .withRelationIfValid(ASConstants.REL_BUYER,"Customer/983938")
                .withAspect(items()
                        .withLine(lineItem()
                                .withProduct(ASLineItem.LINE_TYPES.PURCHASED, new ASEntity("Event/398928"))
                                .withItemCount(1)
                                .withItemPrice(32.5) //this is turned into 0 when marked as complimentary
                                .withPriceCategory("Section A")
                                .withPriceType("Seniors")
                                .withVariant("VIP")
                                .markAsComplimentary() //Sets as complimentary and changes the price to 0
                        )
                );
```
Produces this AS Event message in JSON:
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
        "involves": [
          {
            "PURCHASED": {
              "entity_ref": "Event/398928"
            }
          }
        ],
        "item_count": 1.0,
        "item_price": 0.0,
        "price_category": "Section A",
        "price_type": "Seniors",
        "variant": "VIP",
        "complimentary": true,
        "currency": "USD"
      }
    ]
  }
}
```

### Metrics
Generic store for ad-hoc metrics. Metrics are tracked.
```
    ASEntity venue = new ASEntity("Venue", "983983");
    venue.withMetric("built",1941, "capacity", 5272);
```
Produces this AS Entity message in JSON:
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
        .withMetric("built",1941, "capacity", 5272)
        .withOccurredAt("2017-10-31T12:00:00-01:00");
```
Produces this AS Entity message in JSON and forces a data-point to be created with a specific time. 
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
    .withAspect(presentation()
        .withLabel("Royal Albert Hall")
        .withDescription("The Royal Albert Hall is a concert hall on the northern edge of South Kensington, London, which holds the Proms concerts annually each summer since 1941. It has a capacity of up to 5,272 seats.")
        .withDetailsUrl("https://www.royalalberthall.com/")
        .withThumbnail("https://cdn.royalalberthall.com/file/1396975600/32830992449")
        .withIcon("ol-venue"));
```
Produces this AS Entity message in JSON:
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
### Properties
Used to store any custom properties for AS Events or AS Entities. Properties are stored in the JSON format received. The contents of properties is not 
analysed or put into analytics store but they can be used in presenting the Entity or the event in the activity stream and are valid inputs for any 
templating of events.  

```
ASConfig.setDefaults("US", "USD", TimeZone.getTimeZone("GMT+0:00"));
ASEntity venue = new ASEntity("Venue", "983983")
        .withProperties("some_boolean",true)
        .withProperties("date", DateTime.parse("2017-01-01T00:00:00.000Z"))
        .withProperties("a_map", Collections.singletonMap("map_value",Collections.singletonMap("nested",true)))
        .withProperties("a_sttring", "Yes, I'm a String")
        .withProperties("one_of_many", true, "two_of_many",true, "four_of_many",false, "item_index",4, "position_name","five");
```
Produces this AS Entity message in JSON:
```
{
  "entity_ref":"Venue/983983",
  "properties":{
    "some_boolean":true,
    "date":"2017-01-01T00:00:00.000Z",
    "a_map":{
      "map_value":{
        "nested":true
      }
    },
    "a_sttring":"Yes, I'm a String",
    "one_of_many":true,
    "two_of_many":true,
    "four_of_many":false,
    "item_index":4,
    "position_name":"five"
  }
}
```
*please note:* Properties are now stored as a part of the "aspect" structure.

### Tags
An array of strings used to further classify events in the activity stream. You can use any tag you like but keep in mind that a small set (low cardinality) of tags is commonly more useful than a large set of tags.
```
ASEntity venue = new ASEntity("Venue", "983983");
venue.withAspect(tags().withTags("National"));
```
Produces this AS Entity message in JSON:
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
venue.withAspect(timed()
        .withPeriod("construction","1867","1871")
        .withPeriod("inaugurated","1871-03-29")
        .withPeriod("renovated","1996","2004")
);
```
Produces this AS Entity message in JSON: 
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
