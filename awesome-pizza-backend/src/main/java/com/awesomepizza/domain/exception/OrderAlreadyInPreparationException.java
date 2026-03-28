package com.awesomepizza.domain.exception;

public class OrderAlreadyInPreparationException extends RuntimeException {

    public OrderAlreadyInPreparationException() {
        super("Another order is already in preparation");
    }
}
