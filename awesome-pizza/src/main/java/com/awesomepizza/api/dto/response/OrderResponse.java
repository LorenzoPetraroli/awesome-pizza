package com.awesomepizza.api.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

// Response payload representing the full state of an order.
@Schema(name = "OrderResponse", description = "Full order representation returned by the API")
public record OrderResponse(
        @Schema(description = "Public order code used by the customer to track the order", example = "ORD-A1B2C3D4")
        String code,

        @Schema(description = "Customer name associated with the order", example = "Mario Rossi")
        String customerName,

        @Schema(description = "Current order status", example = "PLACED")
        String status,

        @Schema(description = "Ordered pizzas")
        List<OrderItemResponse> items,

        @Schema(description = "Timestamp when the order was created", example = "2026-03-27T20:15:30Z")
        Instant createdAt,

        @Schema(description = "Timestamp of the latest order update", example = "2026-03-27T20:25:30Z")
        Instant updatedAt,

        @Schema(description = "Timestamp when the order was completed", example = "2026-03-27T20:40:00Z")
        Instant completedAt
) {
}
