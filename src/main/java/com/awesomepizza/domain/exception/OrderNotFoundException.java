package com.awesomepizza.domain.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String code) {
        super("Order not found for code: " + code);
    }
}
