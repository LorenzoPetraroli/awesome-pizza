package com.awesomepizza.domain.validation;

import com.awesomepizza.domain.model.OrderItem;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// Validates the data required to build a valid order aggregate.
@ApplicationScoped
public class OrderValidator {

    public void validateForCreation(UUID id, String code, String customerName, List<OrderItem> items, Instant now) {
        validateRequiredFields(id, code, customerName, items, now, now);
    }

    private void validateRequiredFields(
            UUID id,
            String code,
            String customerName,
            List<OrderItem> items,
            Instant createdAt,
            Instant updatedAt
    ) {
        if (id == null) {
            throw new IllegalArgumentException("Order id is required");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Order code is required");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Creation timestamp is required");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Update timestamp is required");
        }
    }
}
