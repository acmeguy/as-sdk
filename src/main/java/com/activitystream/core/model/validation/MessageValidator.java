package com.activitystream.core.model.validation;

import com.activitystream.core.model.entities.EntityReference;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MessageValidator {

    private static final Logger logger = LoggerFactory.getLogger(MessageValidator.class);

    private List<MessageProblem> exceptions;

    public MessageValidator() {
    }

    public int hasWarnings() {
        if (exceptions == null || exceptions.size() == 0) return 0;
        int cnt = 0;
        for (MessageProblem exception : exceptions) {
            if (exception instanceof MessageWarning) cnt++;
        }
        return cnt;
    }

    public int hasErrors() {
        if (exceptions == null || exceptions.size() == 0) return 0;
        int cnt = 0;
        for (MessageProblem exception : exceptions) {
            if (exception instanceof MessageError) cnt++;
        }
        return cnt;
    }

    public void addProblem(MessageProblem messageProblem) {
        if (exceptions == null)
            exceptions = new LinkedList<>();
        exceptions.add(messageProblem);
    }

    public List<MessageError> getErrors() {
        if (this.exceptions == null) return null;
        List<MessageError> errors = null;
        for (MessageProblem exception : exceptions) {
            if (exception instanceof MessageError) {
                if (errors == null) errors = new LinkedList<>();
                errors.add((MessageError) exception);
            }
        }
        return errors;
    }

    public List<MessageWarning> getWarnings() {
        if (this.exceptions == null) return null;
        List<MessageWarning> warnings = null;
        for (MessageProblem exception : exceptions) {
            if (exception instanceof MessageWarning) {
                if (warnings == null) warnings = new LinkedList<>();
                warnings.add((MessageWarning) exception);
            }
        }
        return warnings;
    }

    public List<MessageProblem> getAllMessageExceptions() {
        return exceptions;
    }

    public void markAsVerified() {
        if (exceptions == null)
            exceptions = new LinkedList<>();
    }

    public void reset() {
        exceptions = null;
    }

    public static String normalizeRef(String id) {

        if (id == null || id.isEmpty()) return null;

        String ref = id;
        ref = ref.replaceAll("/", "~").replaceAll(",", "").replaceAll(";", "").replaceAll("\\(", "[").replaceAll("\\)", "]").replaceAll("  ", " ")
                .replaceAll(" ", "_");
        return ref.toLowerCase();
    }

    public static String normalizeUppercaseRef(String id) {

        if (id == null || id.isEmpty()) return null;

        String ref = id;
        ref = ref.replaceAll("/", "~").replaceAll(",", "").replaceAll(";", "").replaceAll("\\(", "[").replaceAll("\\)", "]").replaceAll("  ", " ")
                .replaceAll(" ", "_");
        return ref.toUpperCase();
    }

    public Integer processInteger(String property, Object value, boolean required, Integer min, Integer max) {

        if (value == null && !required) return null;

        Integer iValue = null;
        if (value instanceof Number) {
            iValue = ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                iValue = Integer.parseInt(value.toString());
                addProblem(new AdjustedContentWarning("Converted to integer from " + value + " for: " + property));
            } catch (NumberFormatException e) {
                addProblem(new InvalidPropertyContentError("Could not convert Integer value from " + value + " for: " + property));
            }
        }

        if (iValue != null && min != null && iValue < min)
            addProblem(new InvalidPropertyContentError("Integer value of " + iValue + " is lower than the minumum of " + min + " for: " + property));
        if (iValue != null && max != null && iValue > max)
            addProblem(new InvalidPropertyContentError("Integer value of " + iValue + " is higher than the maximum of " + max + " for: " + property));
        if (iValue == null) addProblem(new InvalidPropertyContentError("Required Integer value could not found for: " + property));
        return iValue;
    }

    public Long processLong(String property, Object value, boolean required, Integer min, Integer max) {

        if (value == null && !required) return null;

        Long iValue = null;
        if (value instanceof Number) {
            iValue = ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                iValue = Long.parseLong(value.toString());
                addProblem(new AdjustedContentWarning("Converted to long from " + value + " for: " + property));
            } catch (NumberFormatException e) {
                addProblem(new InvalidPropertyContentError("Could not convert Long value from " + value + " for: " + property));
            }
        }

        if (iValue != null && min != null && iValue < min)
            addProblem(new InvalidPropertyContentError("Long value of " + iValue + " is lower than the minumum of " + min + " for: " + property));
        if (iValue != null && max != null && iValue > max)
            addProblem(new InvalidPropertyContentError("Long value of " + iValue + " is higher than the maximum of " + max + " for: " + property));
        if (iValue == null) addProblem(new InvalidPropertyContentError("Required Long value could not found for: " + property));
        return iValue;
    }

    public String processLatLong(String property, Object value, boolean required) {
        if (required && value == null) {
            addProblem(new InvalidPropertyContentError("Value is missing for " + property));
        }
        String valueString = value.toString().replaceAll(" ", "");
        if (!valueString.contains(",")) {
            addProblem(new InvalidPropertyContentError("LatLong string needs to contain two values seperated by a comma, for: " + property));
        }
        try {
            String[] values = valueString.split(",");
            double latDouble = Double.parseDouble(values[0]);
            if (latDouble < -90 || latDouble > 90) {
                addProblem(new InvalidPropertyContentError("Latitude value of " + latDouble + " is not valid, for: " + property));
            }

            double longDouble = Double.parseDouble(values[1]);
            if (longDouble < -180 || latDouble > 180) {
                addProblem(new InvalidPropertyContentError("Longitude value of " + longDouble + " is not valid, for: " + property));
            }
        } catch (NumberFormatException e) {
            addProblem(new InvalidPropertyContentError("LatLong value is not valid, for: " + property + " - " + e));
        }
        return valueString;
    }

    public String processUtlString(String property, Object value, boolean required) {
        return processString(property, value, required, null, null, true);
    }

    public String processString(String property, Object value, boolean required) {
        return processString(property, value, required, null, null, false);
    }

    public String processString(String property, Object value, boolean required, Integer minLength, Integer maxLength, boolean urlDecode) {

        if (value != null && value.toString().isEmpty()) value = null;

        if (required && value == null) {
            addProblem(new InvalidPropertyContentError("String value is missing for " + property));
        }
        if (value instanceof ArrayList) {
            addProblem(new AdjustedContentWarning("String value of '" + Arrays.asList(value) + "' is incorrect for " + property + " first value selected"));
            value = ((ArrayList) value).get(0);
        }
        String valueString = (String) value;

        if (valueString != null) {
            if (urlDecode) {
                try {
                    valueString = java.net.URLDecoder.decode(valueString, "UTF-8");
                } catch (Exception e) {
                    logger.error("Error", e);
                }
            }

            if (minLength != null && valueString.length() < minLength)
                addProblem(new AdjustedContentWarning("String value of '" + valueString + "' is to short for " + property));
            if (maxLength != null && valueString.length() > maxLength)
                addProblem(new AdjustedContentWarning("String value of '" + valueString + "' is to long for " + property));
            valueString = valueString.replaceAll("  ", " ").trim();
        }
        return valueString;
    }

    public Locale processLocale(String property, Object value, boolean required) {

        if (value != null && value.toString().isEmpty()) value = null;

        if (required && value == null) {
            addProblem(new InvalidPropertyContentError("Value is missing for " + property));
        }

        if (value instanceof Locale) return (Locale) value;
        else if (value instanceof String) return new Locale((String) value);
        else {
            addProblem(new InvalidPropertyContentError("Value type not supported for Locale '" + value + "' (" + property + ")"));
        }
        return null;
    }

    public TimeZone processTimezone(String property, Object value, boolean required) {

        if (value != null && value.toString().isEmpty()) value = null;

        if (required && value == null) {
            addProblem(new InvalidPropertyContentError("Value is missing for " + property));
        }

        if (value instanceof TimeZone) return (TimeZone) value;
        else if (value instanceof String) return TimeZone.getTimeZone((String) value);
        else {
            addProblem(new InvalidPropertyContentError("Value type not supported for Locale '" + value + "' (" + property + ")"));
        }
        return null;
    }

    public String processTypeString(String property, Object value, boolean required) {
           String typeString = processLowerCaseString(property, value, required);
           if (!typeString.matches("[a-z|\\.]*")) {
               addProblem(new InvalidPropertyContentError("\"" + value + "\" is not a valid signature: " + property + " (please use alphanumeric characters " +
                       "and seperate groups with a dot"));
           }
           return typeString;
    }

    public String processLowerCaseString(String property, Object value, boolean required) {

        if (value == null && required) {
            addProblem(new InvalidPropertyContentError("String value is missing for: " + property));
        } else if (value != null) {
            String sValue = value.toString();
            String lcalue = sValue.toLowerCase();
            if (!value.equals(lcalue)) {
                addProblem(new AdjustedContentWarning("Source information '" + sValue + "' was converted to lowercase"));
                value = lcalue;
            }
        }
        return (String) value;

    }

    public Boolean processBoolean(String property, Object value, boolean required) {

        if (value == null && required) return false;

        if (value != null) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).intValue() == 1;
            } else {
                addProblem(new InvalidPropertyContentError("Could not evaluate boolean: " + property));
            }
        }
        return null;
    }

    public String processIdString(String property, Object value, boolean required) {
        if (required && value == null) {
            addProblem(new InvalidPropertyContentError("String ID value is missing for " + property));
        }

        String idValue = value.toString().toLowerCase().replace(" ", "_");
        if (!value.toString().equals(idValue)) {
            addProblem(new AdjustedPropertyWarning("String ID was created from incompatible string: '" + value + "' for: " + property));
            value = idValue;
        }

        return value.toString();
    }

    public Double processDouble(String property, Object value, boolean required, Double min, Double max) {
        Double dValue = null;

        try {
            if (value instanceof Number) {
                dValue = ((Number) value).doubleValue();
            } else if (value instanceof String) {
                dValue = Double.parseDouble(value.toString());
            }

            if (dValue != null && min != null && dValue < min)
                addProblem(new InvalidPropertyContentError("Double value of " + dValue + " is lower than the minumum of " + min + " for: " + property));
            if (dValue != null && max != null && dValue > max)
                addProblem(new InvalidPropertyContentError("Double value of " + dValue + " is higher than the maximum of " + max + " for: " + property));
            if (dValue == null && required) addProblem(new InvalidPropertyContentError("Required Double value could not found for: " + property));
        } catch (NumberFormatException e) {
            //eat
        }
        return dValue;
    }

    public Float processFloat(String property, Object value, boolean required, Float min, Float max) {
        Float dValue = null;
        try {
            if (value instanceof Number) {
                dValue = ((Number) value).floatValue();
            } else if (value instanceof String) {
                dValue = Float.parseFloat(value.toString());
            }

            if (dValue != null && min != null && dValue < min)
                addProblem(new InvalidPropertyContentError("Float value of " + dValue + " is lower than the minumum of " + min + " for: " + property));
            if (dValue != null && max != null && dValue > max)
                addProblem(new InvalidPropertyContentError("Float value of " + dValue + " is higher than the maximum of " + max + " for: " + property));
            if (dValue == null && required) addProblem(new InvalidPropertyContentError("Required Float value could not found for: " + property));
        } catch (NumberFormatException e) {
            //eat
        }
        return dValue;
    }

    public DateTime processIsoDateTime(String property, Object value, boolean required) {

        final DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        final DateTimeFormatter dtfAP = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss aa");

        if (value == null && required) {
            addProblem(new InvalidPropertyContentError("Required DateTime is missing for: " + property));
            return null;
        }

        try {
            if (value instanceof String) {
                String theValue = (String) value;
                if (theValue.contains("/")) {
                    if (theValue.contains("AM") || theValue.contains("PM")) {
                        value = DateTime.parse(theValue, dtfAP);
                    } else {
                        value = DateTime.parse(theValue, dtf);
                    }
                } else if (theValue.contains(" ")) {
                    value = DateTime.parse(theValue.replaceAll(" ", "T"));
                } else {
                    value = DateTime.parse(theValue);
                }
            } else if (value instanceof Number) {
                value = new DateTime(value);
            } else if (value instanceof Date) {
                value = new DateTime(value);
            } else if (!(value instanceof DateTime)) {
                value = new DateTime(value);
            }
        } catch (Exception e) {
            logger.warn("could not parse date " + value + " for property: " + property);
            addProblem(new InvalidPropertyContentError("DateTime could not be parsed from: '" + value + "' for: '" + property + "'"));
            return null;
        }

        return (DateTime) value;
    }

    public Date processIsoDate(String property, Object value, boolean required) {

        final DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");

        if (value == null && required) {
            addProblem(new InvalidPropertyContentError("Required Date is missing for: " + property));
            return null;
        }

        try {
            if (value instanceof String) {
                String theValue = (String) value;
                if (theValue.contains("/")) {
                    value = DateTime.parse(theValue, dtf);
                } else if (theValue.contains(" ")) {
                    value = DateTime.parse(theValue.replaceAll(" ", "T"));
                } else {
                    value = DateTime.parse(theValue);
                }
            } else if (value instanceof Number) {
                value = new DateTime(value);
            } else if (!(value instanceof DateTime)) {
                value = new DateTime(value);
            }
            value = ((DateTime) value).toDate();
        } catch (Exception e) {
            logger.warn("could not parse date " + value + " for property: " + property);
            addProblem(new InvalidPropertyContentError("Date could not be parsed from: '" + value + "' for: '" + property + "'"));
            return null;
        }

        return (Date) value;
    }

    public String processUrl(String property, Object value, boolean required) {
        return (String) value;
    }

    public List<String> processSimpleValueList(String property, Object value, boolean required) {
        return processSimpleValueList(property, value, required, null);
    }

    public Set<String> processSimpleSetList(String property, Object value, boolean required) {
        return processSimpleSetList(property, value, required, null);
    }

    public Set<String> processSimpleSetList(String property, Object value, boolean required, String splitValue) {
        return new HashSet<>(processSimpleValueList(property, value, required, splitValue));
    }

    public List<String> processSimpleValueList(String property, Object value, boolean required, String splitValue) {
        if (value == null) {
            if (required) addProblem(new InvalidPropertyContentError(property + " can not be null!"));
            return null;
        }

        LinkedHashSet<String> valueList = new LinkedHashSet<>();
        if (value instanceof String) valueList.add((String) value);
        else if (value instanceof String[]) Collections.addAll(valueList, (String[]) value);
        else valueList.addAll((Collection<String>) value);

        List<String> verifiedList = new ArrayList<>();
        for (String valueString : valueList) {
            valueString = valueString.trim();
            if (splitValue != null && valueString.contains(splitValue)) {
                for (String part : valueString.split(splitValue)) verifiedList.add(part.trim());
                addProblem(new AdjustedContentWarning(
                        property + " list value can not include '" + splitValue + "' and it will be used as a delimiter to create multiple entries!"));
            } else {
                verifiedList.add(valueString);
            }
        }

        return verifiedList;
    }

    public Period processPeriod(String property, Object value, boolean required) {
        if (value == null && required) {
            addProblem(new InvalidPropertyContentError(property + " can not be null!"));
        } else if (!(value instanceof String)) {
            addProblem(new InvalidPropertyContentError(property + " can only be set with a. Not a " + value.getClass()));
        } else {
            return Period.parse((String) value);
        }
        return null;
    }

    public Map processUrlBasedMap(String property, Object value, boolean required) {
        return processMap(property, value, required, true);
    }

    public Map processMap(String property, Object value, boolean required) {
        return processMap(property, value, required, false);
    }

    public Map processMap(String property, Object value, boolean required, boolean urlDecode) {
        if (value == null && required) {
            addProblem(new InvalidPropertyContentError(property + " can not be null!"));
        } else if (value == null) {
            return null;
        } else if (!(value instanceof Map)) {
            addProblem(
                    new InvalidPropertyContentError("Field value for '" + property + "' must be a map. Not a " + value.getClass() + ", value: " + value));
        } else {
            if (urlDecode) {
                Map valueMap = (Map) value;
                for (String key : (Set<String>) valueMap.keySet()) {
                    if (valueMap.get(key) != null && valueMap.get(key) instanceof String) {
                        try {
                            valueMap.put(key, java.net.URLDecoder.decode(((String) valueMap.get(key)), "UTF-8"));
                        } catch (Exception e) {
                            logger.error("Error", e);
                        }
                    }
                }
                value = valueMap;
            }
            return (Map) value;
        }
        return null;
    }

    public Byte[] processBase64(String property, Object value, boolean required) {
        return (Byte[]) value;
    }

    public EntityReference processEntityReference(String property, Object value) {
        EntityReference entityRef = null;
        if (value instanceof EntityReference)
            entityRef = (EntityReference) value;
        else if (value instanceof String)
            entityRef = new EntityReference((String) value);
        else if (value instanceof UUID)
            entityRef = new EntityReference((UUID) value);
        else if (value != null) {
            addProblem(new InvalidPropertyContentError("Unable to parse entity reference from: '" + value + "' of type " + value.getClass() +
                    " for: '" + property + "'"));
        } else {
            addProblem(new InvalidPropertyContentError("Null entity reference for: '" + property + "'"));
        }

        if (entityRef != null) {
            if (entityRef.getEntityTypeReference() != null && !entityRef.getEntityTypeReference().isValidType()) {
                addProblem(new InvalidPropertyContentError("Invalid entity type in reference: '" + value + "' for: '" + property + "'"));
            } else if (!entityRef.isComplete()) {
                addProblem(new InvalidPropertyContentError("EntityReference could not be parsed from: '" + value + "' for: '" + property + "'"));
            }
        }

        return entityRef;
    }
}
