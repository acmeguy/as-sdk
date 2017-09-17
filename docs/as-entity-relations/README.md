# Activity Stream :: AS Entity Relations (Links) 

## Introduction

![Entity Relations - Links](ASEntityRelations.png)

## Common and custom link properties

## Extending Links to create custom Link types
You can subclass any relationship type by adding your own type name to the end of the the relationship type you would like to subclass/extend: 
“[RELATED_TO]():MOTHER”. 
Once the MOTHER subclass is created MOTHER becomes a reserved keyword for this relations type and it will always and only extend [RELATED_TO]().

**Please note:** All relationship-types are unique and can only be used once and that relationship-types are always specified in all-caps.

## IS
Exclusive 1:1 relations
### AKA
Exclusive 1:1 relations (Also Know As)
### ALIAS
### SOCIAL_ID

## CLOSE
Almost the same
### INTEGRAL_TO
### PART_OF
Non-exclusive relationship (Membership, employment)
### PAIRED_WITH
### BELONGS_TO
Non-exclusive relationship (describes ownership/possession)
### POWERED_BY
### MEASURES
### LOCATED_IN
### STARS_IN
### INSTRUMENTAL_IN

## KNOWS
Definitive relationship
### REATED_BY
### MEMBER_OF
### RELATED_TO
Family relations
### SUPPLIED_BY
non-exclusive relationship (describes responsibility)
### HOSTED_AT
### MANUFACTURED_BY
### LOCATED_AT
### APPEARS_IN
### ASSISTS_IN


## KNOWS_OF
Should know of it’s existence
### ON_BEHALF_OF
Non-exclusive relations (Employee on behalf of a customer)
### RELAYED_BY
### ASSOCIATED_WITH
Has social relations to

