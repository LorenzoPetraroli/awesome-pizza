package com.awesomepizza.api.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.util.Arrays;
import java.util.stream.Collectors;

// Builds user-friendly error messages for malformed JSON requests.
final class RequestParsingErrorMessageFactory {

    private RequestParsingErrorMessageFactory() {
    }

    static String buildMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof InvalidFormatException invalidFormatException
                    && invalidFormatException.getTargetType() != null
                    && invalidFormatException.getTargetType().isEnum()) {
                return buildEnumValueMessage(invalidFormatException);
            }
            if (current instanceof MismatchedInputException mismatchedInputException) {
                return buildMismatchedInputMessage(mismatchedInputException);
            }
            current = current.getCause();
        }

        return "Malformed JSON request body";
    }

    private static String buildEnumValueMessage(InvalidFormatException exception) {
        String fieldPath = buildFieldPath(exception);
        String fieldLabel = toUserFieldLabel(fieldPath);
        String invalidValue = String.valueOf(exception.getValue());
        String allowedValues = Arrays.stream(exception.getTargetType().getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return "%s has invalid value '%s'. Allowed values: %s".formatted(fieldLabel, invalidValue, allowedValues);
    }

    private static String buildMismatchedInputMessage(MismatchedInputException exception) {
        String fieldPath = buildFieldPath(exception);
        String fieldLabel = toUserFieldLabel(fieldPath);
        return fieldPath.isBlank()
                ? "Request body contains missing or invalid fields"
                : fieldLabel + " is missing or has an invalid value";
    }

    private static String buildFieldPath(JsonMappingException exception) {
        return exception.getPath().stream()
                .map(reference -> reference.getFieldName() != null
                        ? reference.getFieldName()
                        : "[" + reference.getIndex() + "]")
                .collect(Collectors.joining("."))
                .replace(".[", "[");
    }

    private static String toUserFieldLabel(String fieldPath) {
        if (fieldPath == null || fieldPath.isBlank()) {
            return "Field";
        }

        if (fieldPath.contains("pizzaType")) {
            return "Pizza type";
        }
        if (fieldPath.contains("customerName")) {
            return "Customer name";
        }
        if (fieldPath.contains("quantity")) {
            return "Quantity";
        }
        if (fieldPath.contains("items")) {
            return "Order items";
        }

        String normalizedField = fieldPath.replaceAll(".*\\.", "").replaceAll("\\[\\d+]", "");

        return switch (normalizedField) {
            case "pizzaType" -> "Pizza type";
            case "customerName" -> "Customer name";
            case "items" -> "Order items";
            case "quantity" -> "Quantity";
            default -> normalizedField.substring(0, 1).toUpperCase() + normalizedField.substring(1);
        };
    }
}
