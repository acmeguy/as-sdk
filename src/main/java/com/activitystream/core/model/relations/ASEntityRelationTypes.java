package com.activitystream.core.model.relations;

/**
 * Entity Relations Types are used to create relations between any to Entities regardless of their type
 *
 * All these types can be subclassed in the graph
 * A subtype is create add adding it after a Base type
 * Like: MEMBER_OF:PREMIUM_MEMBER would create a relationship type "PREMIUM_MEMBER"
 * but It could never be created as a subclass of any other parent type than "MEMBER_OF"
 */
public class ASEntityRelationTypes {

    /**
     * An alias is a strict 1:1 relation and indicates a direct and exclusive 1:1 relations
     * Abstract/Base Type: IS
     */
    public static final String ALIAS = "ALIAS";

    /**
     * Also Known As relations are used to incate very close 1:1 relations (Alternative IDs)
     * Abstract/Base Type: IS
     */
    public static final String AKA = "AKA";

    /**
     * An Exclusive 1:1 relations pointing to a known Social ID for the entity
     */
    public static final String SOCIAL = "SOCIAL_ID";

    /**
     * Exclusive 1:n relations (Sub entity, Branch, subsidiary)
     * Abstract/Base Type: CLOSE
     */
    public static final String PART_OF = "PART_OF";

    /**
     * exclusive 1:n relations (Membership, Employment etc.)
     * Abstract/Base Type: CLOSE
     */
    public static final String MEMBER_OF = "MEMBER_OF";

    /**
     * exclusive 1:n relations (A variant of the target Entity)
     * Abstract/Base Type: CLOSE
     */
    public static final String VERSION_OF = "VERSION_OF";


    //Special purpose relations


    /**
     * non-exclusive 1:n relations (Physical Joins, Strong Pairing)
     * Abstract/Base Type: CLOSE
     */
    public static final String PAIRED_WITH = "PAIRED_WITH";

    /**
     * non-exclusive 1:n relations (Physical Joins, Strong Pairing)
     * Abstract/Base Type: CLOSE
     */
    public static final String USED_BY = "USED_BY";

    /**
     * non-exclusive 1:n relations (This Entity prvides measurements for the target entity
     * Any changes to the Metrics or Dimensions of this Entity are directly reflected on the target entity
     * Abstract/Base Type: CLOSE
     */
    public static final String MEASURES = "MEASURES";

    /**
     * non-exclusive 1:n relations (Price calculations) (CLOSE)
     * This entity provides price calculation information for the target entity in the case of it being a Transaction line
     * Abstract/Base Type: CLOSE
     */
    public static final String RATED_BY = "RATED_BY";

    /**
     * non-exclusive 1:n relations (Supplier->Consumer relationship)
     * This Entity provides the target Entity with something (non-specific)
     * Abstract/Base Type: CLOSE
     */
    public static final String POWERED_BY = "POWERED_BY";

    /**
     * non-exclusive 1:n relations (Supplier->Consumer relationship)
     * This Entity provides the target with electricity, gas, water or other measurable units of power/fuel
     * Abstract/Base Type: CLOSE
     */
    public static final String SUPPLIED_BY = "SUPPLIED_BY";

    /**
     * non-exclusive 1:n relations (describes ownership/possession)
     * Describes a ownership relationship between entities
     * Abstract/Base Type: CLOSE
     */
    public static final String BELONGS_TO = "BELONGS_TO";

    /**
     * non-exclusive 1:n relations (describes ownership/possession)
     * A temporary trusted relationship where this entity performs something on behalf of another entity
     */
    public static final String ON_BEHALF_OF = "ON_BEHALF_OF";

    //Proxy relations
    /**
     * exclusive 1:1 relations from a Business Entity To a Extension (Like a WebSession)
     */
    public static final String PROXY_FOR = "PROXY_FOR";
    /**
     * vague n:1 relations (employee on behalf of a customer... for example)
     */
    public static final String RELAYED_BY = "RELAYED_BY";

    //Location relations
    /**
     * Is Temporarily located at (KNOWS)
     */
    public static final String LOCATED_AT = "LOCATED_AT";
    /**
     * Is permanently located in (CLOSE)
     */
    public static final String LOCATED_IN = "LOCATED_IN";

    //Weaker relations
    /**
     * Has social relations to (less important than family)
     */
    public static final String ASSOCIATED_WITH = "ASSOCIATED_WITH";
    /**
     * family relations
     */
    public static final String RELATED_TO = "RELATED_TO";
    /**
     * Has other relations to (less important than family)
     */
    public static final String HAS_RELATIONS_TO = "HAS_RELATIONS_TO";
    /**
     * The entity is of a particulate type (eg. Make, Model, Year)
     */
    public static final String OF_TYPE = "OF_TYPE";
    /**
     * Has social relations to (less important than family)
     */
    public static final String HOSTED_AT = "HOSTED_AT";



    //Can be used for events, shows, movies or in any other context it makes sense
    /**
     * A Entity featured in a show
     */
    public static final String FEATURING = "FEATURING";
    /**
     * Visual major role in some context
     */
    public static final String STARS_IN = "STARS_IN";
    /**
     * Visual minor role in some context
     */
    public static final String APPEARS_IN = "APPEARS_IN";
    /**
     * Non-Visual major role in some context
     */
    public static final String INSTRUMENTAL_IN = "INSTRUMENTAL_IN";
    /**
     * Non-Visual minor role in some context
     */
    public static final String ASSISTS_IN = "ASSISTS_IN";


}
