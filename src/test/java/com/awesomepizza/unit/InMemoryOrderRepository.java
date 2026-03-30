package com.awesomepizza.unit;

import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderStatus;
import com.awesomepizza.domain.repository.OrderRepository;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> storage = new LinkedHashMap<>();

    @Override
    public Order save(Order order) {
        storage.put(order.getCode(), order);
        return order;
    }

    @Override
    public Optional<Order> findByCode(String code) {
        return Optional.ofNullable(storage.get(code));
    }

    @Override
    public Optional<Order> findCurrentInPreparation() {
        return storage.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.IN_PREPARATION)
                .findFirst();
    }

    @Override
    public List<Order> findPlacedOrdersOrderedByCreation() {
        return storage.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.PLACED)
                .sorted(Comparator.comparing(Order::getCreatedAt))
                .toList();
    }

    @Override
    public List<Order> findReadyOrdersOrderedByUpdate() {
        return storage.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.READY)
                .sorted(Comparator.comparing(Order::getUpdatedAt))
                .toList();
    }
}
