package com.awesomepizza.domain.factory;

import com.awesomepizza.domain.model.OrderItem;
import com.awesomepizza.domain.model.PizzaType;
import com.awesomepizza.domain.validation.OrderItemValidator;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

// Factory responsible for creating valid order item value objects.
@ApplicationScoped
public class OrderItemFactory {

    private final OrderItemValidator orderItemValidator;

    public OrderItemFactory(OrderItemValidator orderItemValidator) {
        this.orderItemValidator = orderItemValidator;
    }

    public OrderItem create(UUID id, PizzaType pizzaType, int quantity) {
        orderItemValidator.validate(pizzaType, quantity);
        return new OrderItem(id, pizzaType, quantity);
    }
}
