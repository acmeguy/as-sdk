package com.activitystream.model.relations;

public enum Direction {
    OUT,
    IN,
    BOTH;

    public static final Direction[] proper = new Direction[]{OUT, IN};

    private Direction() {
    }

    public Direction opposite() {
        if (this.equals(OUT)) {
            return IN;
        } else {
            return this.equals(IN) ? OUT : BOTH;
        }
    }
}
