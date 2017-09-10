package com.activitystream.model.config;

import org.joda.time.DateTimeZone;

import java.util.TimeZone;

/**
 * @author ivan
 */
public class ASConfig {

    private static String defaultCountryCode;
    private static String defaultCurrency;
    private static TimeZone defaultTimeZone;

    public static void setDefaults(String countryCode, String currency, TimeZone timeZone) {
        if (currency != null) {
            ASConfig.defaultCurrency = currency;
        }
        if (countryCode != null) {
            ASConfig.defaultCountryCode = countryCode;
        }
        if (timeZone != null) {
            JacksonMapper.getMapper().setTimeZone(timeZone);
            DateTimeZone.setDefault(DateTimeZone.forTimeZone(timeZone));
            ASConfig.defaultTimeZone = timeZone;
        }
    }

    public static String getDefaultCountryCode() {
        return defaultCountryCode;
    }

    public static String getDefaultCurrency() {
        return defaultCurrency;
    }

    public static TimeZone getDefaultTimeZone() {
        return defaultTimeZone;
    }
}
