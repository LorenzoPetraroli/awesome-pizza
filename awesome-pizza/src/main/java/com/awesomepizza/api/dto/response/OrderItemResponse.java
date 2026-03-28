package com.awesomepizza.api.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

// Response payload describing one pizza line inside an order.
@Schema(name = "OrderItemResponse", description = "Single order line returned by the API")
public record OrderItemResponse(
        @Schema(description = "Pizza enum code", example = "DIAVOLA")
        String pizzaType,

        @Schema(description = "Human-readable pizza name", example = "Diavola")
        String displayName,

        @Schema(description = "Ordered quantity for the pizza", example = "1")
        int quantity
) {
}
