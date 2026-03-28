package com.awesomepizza.unit;

import com.awesomepizza.application.service.OrderService;
import com.awesomepizza.domain.factory.OrderFactory;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderItem;
import com.awesomepizza.domain.model.OrderStatus;
import com.awesomepizza.domain.model.PizzaType;
import com.awesomepizza.domain.factory.OrderItemFactory;
import com.awesomepizza.domain.validation.OrderItemValidator;
import com.awesomepizza.domain.validation.OrderValidator;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest {

    @Test
    void shouldCreatePlacedOrderWithTrackingCode() {
        OrderItemFactory orderItemFactory = new OrderItemFactory(new OrderItemValidator());
        OrderService service = new OrderService(
                new OrderFactory(new OrderValidator()),
                new InMemoryOrderRepository(),
                Clock.fixed(Instant.parse("2026-03-28T10:00:00Z"), ZoneOffset.UTC)
        );

        Order order = service.createOrder(
                "Mario Rossi",
                List.of(orderItemFactory.create(null, PizzaType.QUATTRO_FORMAGGI, 2))
        );

        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertEquals("Mario Rossi", order.getCustomerName());
        assertNotNull(order.getId());
        assertTrue(order.getCode().startsWith("ORD-"));
    }
}
