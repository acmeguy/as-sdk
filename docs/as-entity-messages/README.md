# Activity Stream :: AS Entity Messages

## Introduction to AS Entities
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


## AS Entity message example
```
ASEntity secretAgent = new ASEntity("Agent","007")
        .withAspect(presentation()
                .withLabel("James Bond")
                .withThumbnail("http://www.007.com/wp-content/uploads/2014/01/Roger-Moore-james-bond-BW.jpg")
                .withIcon("007-icon")
                .withDescription("Dude working for a Queen killing people, mostly")
                .withDetailsUrl("http://www.007.com/"))
        .withAspect(demography()
                .withGender("male")
                .withBirthDate("1968-04-13")
                .withEmployment("Governmental Employee")
                .withEthnicity("Caucasian")
                .withMaritalStatus("Very Single")
                .withHousing("Lives alone")
                .withMosaicGroup("Wealthy World Traveller")
                .withEducation("Like to have a degree")
                .withIncome("400k$ - 800k$"))
        .withAspect(address()
                .withAddress("61 Horseferry Road")
                .withPostCode("SW1")
                .withCity("Westminster")
                .withRegion("London")
                .withState("Greater London")
                .withCountry("United Kingdom")
                .withCountryCode("UK")
        ).withAspect(classification()
                .withType("individual")
                .withVariant("employee")
                .withCategories("agent","secret")
                .withTags("kills bad guys","travels","mostly obeys","womanizer","loyal","utility")
        )
        .withRelationIfValid(ASEntityRelationTypes.AKA,"Email","007@mi5.gov.uk")
        .withRelationIfValid(ASEntityRelationTypes.AKA,"Phone","+441007007007", "secure_line",true)
        .withPartition("mi5_only");
```
Produces this AS Entity message in JSON:
```
{
  "entity_ref":"Agent/007",
  "aspects":{
    "presentation":{
      "label":"James Bond",
      "thumbnail":"http://www.007.com/wp-content/uploads/2014/01/Roger-Moore-james-bond-BW.jpg",
      "icon":"007-icon",
      "description":"Dude working for a Queen killing people, mostly",
      "details_url":"http://www.007.com/"
    },
    "demography":{
      "gender":"male",
      "gender_guessed":false,
      "birth_day":13,
      "birth_year":1968,
      "birth_month":4,
      "employment":"Governmental Employee",
      "ethnicity":"Caucasian",
      "marital_status":"Very Single",
      "income":"400k$ - 800k$",
      "mosaic_group":"Wealthy World Traveller",
      "education":"Like to have a degree"
    },
    "address":{
      "address":"61 Horseferry Road",
      "zip_code":"SW1",
      "city":"Westminster",
      "region":"London",
      "state":"Greater London",
      "country":"United Kingdom",
      "country_code":"UK"
    },
    "classification":{
      "type":"individual",
      "variant":"employee",
      "categories":["agent","secret"],
      "tags":["kills bad guys","travels","mostly obeys","womanizer","loyal","utility"]
    }
  },
  "relations":[
    {"AKA":{"entity_ref":"Email/007@mi5.gov.uk"}},
    {"AKA":{"entity_ref":"Phone/+441007007007"},"properties":{"secure_line":true}}
  ],
  "partition":"mi5_only"
}
```