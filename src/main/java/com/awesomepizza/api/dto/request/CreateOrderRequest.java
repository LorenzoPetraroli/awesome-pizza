package com.awesomepizza.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

// Request payload used to create a new customer order.
@Schema(name = "CreateOrderRequest", description = "Payload used to create a new customer order")
public record CreateOrderRequest(
        @NotBlank(message = "customerName is required")
        @Schema(description = "Customer name attached to the order", example = "Mario Rossi", required = true)
        String customerName,

        @Valid
        @NotEmpty(message = "at least one order item is required")
        @Schema(description = "List of pizzas requested by the customer", required = true)
        List<CreateOrderItemRequest> items
) {
}
