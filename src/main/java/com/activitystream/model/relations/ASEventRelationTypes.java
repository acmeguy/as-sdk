package com.activitystream.model.relations;

public class ASEventRelationTypes {

    /**
     * The Entity responsible for causing the event
     */
    public static final String ACTOR = "ACTOR";

    /**
     * Event affects the referenced Entity
     */
    public static final String AFFECTS = "AFFECTS";

    /**
     * Entity is involved in event (not directly affected)
     */
    public static final String INVOLVES = "INVOLVES";

    /**
     * Entity is witnesses/observes the event (not directly affected)
     */
    public static final String OBSERVES = "OBSERVES";

    /**
     * Entity is referenced in the event (not involved or directly affected)
     */
    public static final String REFERENCES = "REFERENCES";

    /**
     * Event creates the referenced Entity
     * Should be changed into a relations property or multi inheritance class
     */
    @Deprecated
    public static final String CREATES = "CREATES";

    /**
     * Event updates the referenced Entity
     * Should be changed into a relations property or multi inheritance class
     */
    public static final String UPDATES = "UPDATES";

    /**
     * Event removes/deletes/destroys the referenced Entity
     * Should be changed into a relations property or multi inheritance class
     */
    public static final String REMOVES = "REMOVES";

}
