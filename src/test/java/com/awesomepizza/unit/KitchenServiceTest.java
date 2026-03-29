package com.awesomepizza.unit;

import com.awesomepizza.application.service.KitchenService;
import com.awesomepizza.application.validation.KitchenWorkflowValidator;
import com.awesomepizza.domain.exception.InvalidOrderStateException;
import com.awesomepizza.domain.exception.OrderAlreadyInPreparationException;
import com.awesomepizza.domain.factory.OrderFactory;
import com.awesomepizza.domain.factory.OrderItemFactory;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderStatus;
import com.awesomepizza.domain.model.PizzaType;
import com.awesomepizza.domain.validation.OrderItemValidator;
import com.awesomepizza.domain.validation.OrderValidator;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KitchenServiceTest {

    private final OrderItemFactory orderItemFactory = new OrderItemFactory(new OrderItemValidator());
    private final OrderFactory orderFactory = new OrderFactory(new OrderValidator());

    @Test
    void shouldStartSelectedQueuedOrder() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        KitchenService service = new KitchenService(
                repository,
                new KitchenWorkflowValidator(),
                Clock.fixed(Instant.parse("2026-03-28T10:10:00Z"), ZoneOffset.UTC)
        );

        repository.save(orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-FIRST01",
                "Mario Rossi",
                List.of(orderItemFactory.create(UUID.randomUUID(), PizzaType.MARGHERITA, 1)),
                Instant.parse("2026-03-28T10:00:00Z")
        ));
        repository.save(orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-SECOND1",
                "Luigi Verdi",
                List.of(orderItemFactory.create(UUID.randomUUID(), PizzaType.DIAVOLA, 1)),
                Instant.parse("2026-03-28T10:05:00Z")
        ));

        Order startedOrder = service.startOrderByCode("ORD-SECOND1");

        assertEquals("ORD-SECOND1", startedOrder.getCode());
        assertEquals(OrderStatus.IN_PREPARATION, startedOrder.getStatus());
        assertEquals("ORD-FIRST01", service.getSummary().queuedOrders().get(0).getCode());
    }

    @Test
    void shouldRejectSecondOrderInPreparation() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        KitchenService service = new KitchenService(
                repository,
                new KitchenWorkflowValidator(),
                Clock.fixed(Instant.parse("2026-03-28T10:10:00Z"), ZoneOffset.UTC)
        );

        Order current = orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-ACTIVE1",
                "Mario Rossi",
                List.of(orderItemFactory.create(UUID.randomUUID(), PizzaType.MARGHERITA, 1)),
                Instant.parse("2026-03-28T10:00:00Z")
        );
        Instant startedAt = Instant.parse("2026-03-28T10:01:00Z");
        current.startPreparation(startedAt);
        repository.save(current);

        assertThrows(InvalidOrderStateException.class, () -> service.startOrderByCode("ORD-ACTIVE1"));
    }

    @Test
    void shouldRejectStartingAnotherOrderWhenOneIsAlreadyInPreparation() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        KitchenService service = new KitchenService(
                repository,
                new KitchenWorkflowValidator(),
                Clock.fixed(Instant.parse("2026-03-28T10:10:00Z"), ZoneOffset.UTC)
        );

        Order current = orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-ACTIVE1",
                "Mario Rossi",
                List.of(orderItemFactory.create(UUID.randomUUID(), PizzaType.MARGHERITA, 1)),
                Instant.parse("2026-03-28T10:00:00Z")
        );
        current.startPreparation(Instant.parse("2026-03-28T10:01:00Z"));
        repository.save(current);

        repository.save(orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-NEXT001",
                "Luigi Verdi",
                List.of(orderItemFactory.create(UUID.randomUUID(), PizzaType.DIAVOLA, 1)),
                Instant.parse("2026-03-28T10:05:00Z")
        ));

        assertThrows(OrderAlreadyInPreparationException.class, () -> service.startOrderByCode("ORD-NEXT001"));
    }

    @Test
    void shouldRejectStartingOrderThatIsNotPlaced() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        KitchenService service = new KitchenService(
                repository,
                new KitchenWorkflowValidator(),
                Clock.fixed(Instant.parse("2026-03-28T10:10:00Z"), ZoneOffset.UTC)
        );

        Order readyOrder = orderFactory.createPlacedOrder(
                UUID.randomUUID(),
                "ORD-READY01",
                "Mario Rossi",
                List.of(orderItemFactory.create(UUID.randomUUID(), PizzaType.MARGHERITA, 1)),
                Instant.parse("2026-03-28T10:00:00Z")
        );
        readyOrder.startPreparation(Instant.parse("2026-03-28T10:01:00Z"));
        readyOrder.markReady(Instant.parse("2026-03-28T10:05:00Z"));
        repository.save(readyOrder);

        assertThrows(InvalidOrderStateException.class, () -> service.startOrderByCode("ORD-READY01"));
    }
}
