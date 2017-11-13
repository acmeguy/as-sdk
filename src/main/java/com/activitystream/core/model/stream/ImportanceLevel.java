package com.activitystream.core.model.stream;

public enum ImportanceLevel {

    INSIGNIFICANT,
    NOTICEABLE,
    NOT_IMPORTANT,
    IMPORTANT,
    VERY_IMPORTANT,
    CRITICAL;

    public static final ImportanceLevel[] importanceLevels = new ImportanceLevel[]{INSIGNIFICANT, NOTICEABLE, NOT_IMPORTANT, IMPORTANT, VERY_IMPORTANT,
            CRITICAL};

    ImportanceLevel() {

    }

}
