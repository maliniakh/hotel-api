package com.agoda.hotels.model;

/**
 * Room type.
 * JSON marshalling did not work properly for scala.Enumeration so I used java enum instead.
 */
public enum RoomType {
    Deluxe("Deluxe"),
    Superior("Superior"),
    SweetSuite("Sweet Suite");

    private String name;

    RoomType(String name) {
        this.name = name;
    }

    public static RoomType getByName(String name) {
        for (RoomType rt : values()) {
            if(rt.name.equals(name)) {
                return rt;
            }
        }

        return null;
    }
}
