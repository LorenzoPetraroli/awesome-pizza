package com.awesomepizza.unit;

import com.awesomepizza.domain.factory.OrderFactory;
import com.awesomepizza.domain.factory.OrderItemFactory;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderStatus;
import com.awesomepizza.domain.model.PizzaType;
import com.awesomepizza.domain.validation.OrderItemValidator;
import com.awesomepizza.domain.validation.OrderValidator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private final OrderItemFactory orderItemFactory = new OrderItemFactory(new OrderItemValidator());
    private final OrderFactory orderFactory = new OrderFactory(new OrderValidator());

    @Test
    void shouldMoveOrderThroughExpectedStates() {
        Order order = orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-TEST001",
                "Mario Rossi",
                List.of(
                        orderItemFactory.create(UUID.randomUUID(), PizzaType.MARGHERITA, 2),
                        orderItemFactory.create(UUID.randomUUID(), PizzaType.CAPRICCIOSA, 1)
                ),
                Instant.parse("2026-03-28T10:00:00Z")
        );

        Instant startedAt = Instant.parse("2026-03-28T10:05:00Z");
        order.startPreparation(startedAt);
        order.markReady(Instant.parse("2026-03-28T10:05:20Z"));
        order.complete(Instant.parse("2026-03-28T10:05:25Z"));

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertEquals(Instant.parse("2026-03-28T10:05:25Z"), order.getCompletedAt());
    }

    @Test
    void shouldRejectInvalidTransition() {
        Order order = orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-TEST002",
                "Mario Rossi",
                List.of(orderItemFactory.create(UUID.randomUUID(), PizzaType.DIAVOLA, 1)),
                Instant.parse("2026-03-28T10:00:00Z")
        );

        assertThrows(IllegalStateException.class, () -> order.complete(Instant.parse("2026-03-28T10:15:00Z")));
    }
}
