package com.awesomepizza.api.validation;

import com.awesomepizza.domain.exception.InvalidOrderCodeException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.regex.Pattern;

// Validates the public order code format exposed by the API.
@ApplicationScoped
public class OrderCodeValidator {

    private static final Pattern ORDER_CODE_PATTERN = Pattern.compile("^ORD-[A-Z0-9]{8}$");

    public void validate(String code) {
        if (code == null || !ORDER_CODE_PATTERN.matcher(code).matches()) {
            throw new InvalidOrderCodeException(code);
        }
    }
}
