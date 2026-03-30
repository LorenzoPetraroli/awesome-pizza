package com.awesomepizza.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;
    private String code;
    private String customerName;
    private OrderStatus status;
    private List<OrderItem> items = List.of();
    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;

    public Order() {}

    public Order(UUID id,
                 String code,
                 String customerName,
                 OrderStatus status,
                 List<OrderItem> items,
                 Instant createdAt,
                 Instant updatedAt,
                 Instant completedAt) {
        this.id = id;
        this.code = code;
        this.customerName = customerName;
        this.status = status;
        this.items = items != null ? List.copyOf(items) : List.of();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items != null ? List.copyOf(items) : List.of();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public void startPreparation(Instant startedAt) {
        if (status != OrderStatus.PLACED) {
            throw new IllegalStateException("Only placed orders can enter preparation");
        }
        this.status = OrderStatus.IN_PREPARATION;
        this.updatedAt = startedAt;
    }

    public void markReady(Instant readyAt) {
        if (status != OrderStatus.IN_PREPARATION) {
            throw new IllegalStateException("Only orders in preparation can be marked ready");
        }
        this.status = OrderStatus.READY;
        this.updatedAt = readyAt;
    }

    public void complete(Instant completedAt) {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Only ready orders can be completed");
        }
        this.status = OrderStatus.COMPLETED;
        this.completedAt = completedAt;
        this.updatedAt = completedAt;
    }
}
