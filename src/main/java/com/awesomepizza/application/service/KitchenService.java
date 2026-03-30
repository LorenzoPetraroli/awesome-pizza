package com.awesomepizza.application.service;

import com.awesomepizza.application.model.KitchenSummary;
import com.awesomepizza.application.validation.KitchenWorkflowValidator;
import com.awesomepizza.domain.exception.OrderNotFoundException;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;

@ApplicationScoped
public class KitchenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KitchenService.class);

    private final OrderRepository orderRepository;
    private final KitchenWorkflowValidator kitchenWorkflowValidator;
    private final Clock clock;

    @Inject
    public KitchenService(
            OrderRepository orderRepository,
            KitchenWorkflowValidator kitchenWorkflowValidator,
            Clock clock
    ) {
        this.orderRepository = orderRepository;
        this.kitchenWorkflowValidator = kitchenWorkflowValidator;
        this.clock = clock;
    }

    public KitchenSummary getSummary() {
        return new KitchenSummary(
                orderRepository.findCurrentInPreparation().orElse(null),
                orderRepository.findPlacedOrdersOrderedByCreation(),
                orderRepository.findReadyOrdersOrderedByUpdate()
        );
    }

    @Transactional
    public Order startOrderByCode(String code) {
        Order selectedOrder = orderRepository.findByCode(code)
                .orElseThrow(() -> new OrderNotFoundException(code));
        kitchenWorkflowValidator.validateCanStartSelectedOrder(
                orderRepository.findCurrentInPreparation().orElse(null),
                selectedOrder
        );
        return startOrder(selectedOrder);
    }

    private Order startOrder(Order order) {
        Instant now = Instant.now(clock);
        order.startPreparation(now);
        Order savedOrder = orderRepository.save(order);
        LOGGER.info("Order {} moved to {}", savedOrder.getCode(), savedOrder.getStatus());
        return savedOrder;
    }

    @Transactional
    public Order markOrderReady(String code) {
        Order order = orderRepository.findByCode(code)
                .orElseThrow(() -> new OrderNotFoundException(code));

        kitchenWorkflowValidator.validateCanMarkReady(order);
        order.markReady(Instant.now(clock));
        Order savedOrder = orderRepository.save(order);
        LOGGER.info("Order {} moved to {}", savedOrder.getCode(), savedOrder.getStatus());
        return savedOrder;
    }

    @Transactional
    public Order completeOrder(String code) {
        Order order = orderRepository.findByCode(code)
                .orElseThrow(() -> new OrderNotFoundException(code));

        kitchenWorkflowValidator.validateCanComplete(order);
        order.complete(Instant.now(clock));
        Order savedOrder = orderRepository.save(order);
        LOGGER.info("Order {} moved to {}", savedOrder.getCode(), savedOrder.getStatus());
        return savedOrder;
    }
}
