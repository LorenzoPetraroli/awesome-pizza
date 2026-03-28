package com.awesomepizza.domain.exception;

public class InvalidOrderCodeException extends RuntimeException {

    public InvalidOrderCodeException(String code) {
        super("Order code '%s' has an invalid format. Expected format: ORD-XXXXXXXX".formatted(code));
    }
}
