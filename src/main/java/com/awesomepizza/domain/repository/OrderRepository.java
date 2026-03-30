package com.awesomepizza.domain.repository;

import com.awesomepizza.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByCode(String code);

    Optional<Order> findCurrentInPreparation();

    List<Order> findPlacedOrdersOrderedByCreation();

    List<Order> findReadyOrdersOrderedByUpdate();
}
