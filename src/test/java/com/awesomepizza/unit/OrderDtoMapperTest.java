package com.awesomepizza.unit;

import com.awesomepizza.api.dto.response.OrderResponse;
import com.awesomepizza.api.mapper.OrderDtoMapperImpl;
import com.awesomepizza.domain.factory.OrderFactory;
import com.awesomepizza.domain.factory.OrderItemFactory;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.PizzaType;
import com.awesomepizza.domain.validation.OrderItemValidator;
import com.awesomepizza.domain.validation.OrderValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDtoMapperTest {

    private final OrderItemFactory orderItemFactory = new OrderItemFactory(new OrderItemValidator());
    private final OrderFactory orderFactory = new OrderFactory(new OrderValidator());
    private final OrderDtoMapperImpl mapper = new OrderDtoMapperImpl();

    @Test
    void shouldExposeUnitPricesLineTotalsAndOrderTotal() {
        Order order = orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-PRICE01",
                "Mario Rossi",
                List.of(
                        orderItemFactory.create(UUID.randomUUID(), PizzaType.MARGHERITA, 2),
                        orderItemFactory.create(UUID.randomUUID(), PizzaType.DIAVOLA, 1)
                ),
                Instant.parse("2026-03-28T10:00:00Z")
        );

        OrderResponse response = mapper.toResponse(order);

        assertEquals(new BigDecimal("21.00"), response.totalPrice());
        assertEquals(new BigDecimal("6.50"), response.items().get(0).unitPrice());
        assertEquals(new BigDecimal("13.00"), response.items().get(0).lineTotal());
        assertEquals(new BigDecimal("8.00"), response.items().get(1).unitPrice());
        assertEquals(new BigDecimal("8.00"), response.items().get(1).lineTotal());
    }
}
