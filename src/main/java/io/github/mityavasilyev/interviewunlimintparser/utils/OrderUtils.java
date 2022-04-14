package io.github.mityavasilyev.interviewunlimintparser.utils;

import java.util.List;

public class OrderUtils {

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
