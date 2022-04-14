package io.github.mityavasilyev.interviewunlimintparser.utils;

import java.util.List;

public class OrderUtils {

    /**
     * Builds standard status out of provided errors.
     * Returns OK string if provided list of errors is empty.
     *
     * @param errors List of errors that occurred during parsing
     * @return String that represents status of parsing
     */
    public static String buildParseResult(List<String> errors) {

        if (errors == null) throw new IllegalArgumentException();

        if (errors.isEmpty()) return "OK";

        StringBuilder parseResult = new StringBuilder();
        for (String error : errors) {
            if (parseResult.length() != 0) parseResult.append(", ");
            parseResult.append(error);
        }
        return parseResult.toString();
    }
}
