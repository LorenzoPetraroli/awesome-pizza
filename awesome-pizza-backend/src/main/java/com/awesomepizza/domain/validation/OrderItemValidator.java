package com.awesomepizza.domain.validation;

import com.awesomepizza.domain.model.PizzaType;
import jakarta.enterprise.context.ApplicationScoped;

// Validates data required to build a domain order item.
@ApplicationScoped
public class OrderItemValidator {

    public void validate(PizzaType pizzaType, int quantity) {
        if (pizzaType == null) {
            throw new IllegalArgumentException("Pizza type is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}
