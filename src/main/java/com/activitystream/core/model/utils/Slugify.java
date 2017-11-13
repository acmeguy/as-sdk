package com.activitystream.core.model.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slugify {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String asEntityType(String input) {
        StringBuilder entityType = new StringBuilder();
        for (String part : input.split(":")) {
            String normalized = Normalizer.normalize(WHITESPACE.matcher(part).replaceAll(""), Form.NFD);
            if (entityType.length() > 0) entityType.append(":");
            entityType.append(NONLATIN.matcher(normalized).replaceAll(""));
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
        return slug.toLowerCase(Locale.ENGLISH);
    }
}