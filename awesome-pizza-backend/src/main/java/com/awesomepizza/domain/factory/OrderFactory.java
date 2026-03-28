package com.awesomepizza.domain.factory;

import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderItem;
import com.awesomepizza.domain.model.OrderStatus;
import com.awesomepizza.domain.validation.OrderValidator;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// Factory responsible for creating valid order aggregates.
@ApplicationScoped
public class OrderFactory {

    private final OrderValidator orderValidator;

    public OrderFactory(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public Order createPlacedOrder(UUID id, String code, String customerName, List<OrderItem> items, Instant now) {
        orderValidator.validateForCreation(id, code, customerName, items, now);
        return new Order(
                id,
                code.trim(),
                customerName.trim(),
                OrderStatus.PLACED,
                items,
                now,
                now,
                null
        );
    }
}
