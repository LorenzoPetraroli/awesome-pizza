package com.awesomepizza.domain.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

// Immutable domain item representing a pizza line inside an order.
@Schema(name = "OrderItem", description = "Domain value object representing a single pizza line inside an order")
public record OrderItem(
        @Schema(description = "Technical identifier of the order item", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Pizza type assigned to the order item", implementation = PizzaType.class)
        PizzaType pizzaType,

        @Schema(description = "Quantity requested for the pizza type", example = "2")
        int quantity
) {

}
