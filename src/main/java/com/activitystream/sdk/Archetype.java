package com.activitystream.sdk;

public enum Archetype {
    CAMPAIGN("Campaign"),
    CAMPAIGN_MAILING("Campaign", "Mailing"),
    CAMPAIGN_PROMOTION("Campaign", "Promotion"),
    CUSTOMER("Customer"),
    EMAIL("Email"),
    IP_ADDRESS("IPAddress"),
    LOCATION("Location"),
    LOCATION_VENUE("Location", "Venue"),
    MANUFACTURER("Manufacturer"),
    MANUFACTURER_PERFORMER("Manufacturer", "Performer"),
    MANUFACTURER_GROUP("ManufacturerGroup"),
    MANUFACTURER_DEVICE("MeasurementDevice"),
    ORDER("Order"),
    PHONE("Phone"),
    PRODUCT("Product"),
    PRODUCT_EVENT("Product", "Event"),
    PRODUCT_TICKET("Product"),
    PRODUCT_GROUP("ProductGroup"),
    RETAILER("Retailer"),
    RETAILER_GROUP("RetailerGroup"),
    SESSION("Session"),
    SESSION_WEB("Session", "Web"),
    SERVICE("Service"),
    SERVICE_CHARGE("Service", "Charge"),
    SOCIAL("Social"),
    SOCIAL_YOUTUBE_USER("Social", "YoutubeUser"),
    SOCIAL_VEVO_USER("Social", "VevoUser"),
    SOCIAL_FACEBOOK_USER("Social", "FacebookUser"),
    SOCIAL_TWITTER_USER("Social", "TwitterUser"),
    SOCIAL_INSTAGRAM_USER("Social", "InstagramUser"),
    SOCIAL_MUSICBRAINZ_ID ("Social", "MusicbrainzID"),
    SUB_LOCATION ("SubLocation"),
    SUB_LOCATION_HALL ("SubLocation", "Hall"),
    SUB_LOCATION_SEAT ("SubLocation", "Seat"),
    SUPPLIER ("Supplier"),
    SUPPLIER_ORGANIZER ("Supplier", "Organizer"),
    SUPPLIER_GROUP ("SupplierGroup"),
    TRANSACTION ("Transaction");

    private final String name;
    private final String variant;


    Archetype(String name, String variant) {
        this.name = name;
        this.variant = variant;
    }

    Archetype(String name) {
        this(name, null);
    }

    public String getName() {
        return name;
    }

    public String getVariant() {
        return variant;
    }
}
