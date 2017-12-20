package com.activitystream.core.model.utils;

import net.logstash.logback.encoder.org.apache.commons.lang.WordUtils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slugify {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern NON_WORD_CHARACTER = Pattern.compile("[\\W]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String asEntityType(String input) {
        StringBuilder entityType = new StringBuilder();
        for (String part : input.split(":")) {
            part = WordUtils.capitalize(part);
            String normalized = Normalizer.normalize(WHITESPACE.matcher(part).replaceAll(""), Form.NFD);
            if (entityType.length() > 0) entityType.append(":");
            entityType.append(NON_WORD_CHARACTER.matcher(normalized).replaceAll(""));
        }
        return entityType.toString();
    }

    public static String asSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        while (nowhitespace.contains("--")) {
            nowhitespace = nowhitespace.replaceAll("--","-");
        }
        String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        while (slug.contains("--")) {
            slug = slug.replaceAll("--","-");
        }
        return slug.toLowerCase(Locale.ENGLISH);
    }
}