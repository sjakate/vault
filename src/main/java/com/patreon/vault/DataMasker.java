package com.patreon.vault;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Used to mask sensitive data.
 */
public final class DataMasker {
    public static final List<String> defaultPropertiesToSanitize = Collections.unmodifiableList(Arrays.asList("ssn", "pan"));
    private static final String GENERIC_PAN_REGEX = "(\\d{4}).{8,11}(\\d{4})";

    /**
     * Mask sensitive fields in data. Fields are given by the <code>propertiesToBeSanitizedArray</code>
     */
    public static String maskSensitiveData(StringBuilder data, List<String> propertiesToSanitize) {
        Pattern genericPropertyPattern;

        for (String property : propertiesToSanitize) {
            genericPropertyPattern = Pattern.compile("\"" + property + "\"\\s*:\\s*\".*(.{4})\"");
            data.replace(0, data.length(), genericPropertyPattern.matcher(data).replaceAll("\"" + property + "\":\"**MASKED**$1\""));
        }

        return data.toString();
    }
}
