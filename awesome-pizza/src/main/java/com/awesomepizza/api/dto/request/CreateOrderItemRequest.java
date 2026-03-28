package com.awesomepizza.api.dto.request;

import com.awesomepizza.domain.model.PizzaType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

// Request payload for a single pizza line inside a customer order.
@Schema(name = "CreateOrderItemRequest", description = "Single pizza line requested when a customer creates an order")
public record CreateOrderItemRequest(
        @Schema(description = "Pizza type requested by the customer", example = "MARGHERITA", required = true)
        @NotNull(message = "pizzaType is required")
        PizzaType pizzaType,

        @Schema(description = "Requested quantity for the selected pizza type", example = "2", minimum = "1", required = true)
        @Min(value = 1, message = "quantity must be greater than zero")
        int quantity
) {
}
