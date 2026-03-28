package com.awesomepizza.application.service;

import com.awesomepizza.domain.factory.OrderFactory;
import com.awesomepizza.domain.exception.OrderNotFoundException;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderItem;
import com.awesomepizza.domain.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderFactory orderFactory;
    private final OrderRepository orderRepository;
    private final Clock clock;

    @Inject
    public OrderService(OrderFactory orderFactory, OrderRepository orderRepository, Clock clock) {
        this.orderFactory = orderFactory;
        this.orderRepository = orderRepository;
        this.clock = clock;
    }

    @Transactional
    public Order createOrder(String customerName, List<OrderItem> items) {
        Instant now = Instant.now(clock);
        Order order = orderFactory.createPlacedOrder(UUID.randomUUID(), OrderCodeGenerator.nextCode(), customerName, items, now);
        Order savedOrder = orderRepository.save(order);
        LOGGER.info("Created order {} for customer {}", savedOrder.getCode(), savedOrder.getCustomerName());
        return savedOrder;
    }

    public Order getOrderByCode(String code) {
        return orderRepository.findByCode(code)
                .orElseThrow(() -> new OrderNotFoundException(code));
    }
}
