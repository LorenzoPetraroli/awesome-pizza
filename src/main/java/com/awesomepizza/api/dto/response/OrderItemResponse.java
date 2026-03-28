package com.awesomepizza.api.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

// Response payload describing one pizza line inside an order.
@Schema(name = "OrderItemResponse", description = "Single order line returned by the API")
public record OrderItemResponse(
        @Schema(description = "Pizza enum code", example = "DIAVOLA")
        String pizzaType,

        @Schema(description = "Human-readable pizza name", example = "Diavola")
        String displayName,

        @Schema(description = "Ordered quantity for the pizza", example = "1")
        int quantity,

        @Schema(description = "Unit price of the selected pizza in euro", example = "8.00")
        BigDecimal unitPrice,

        @Schema(description = "Total price for the order line in euro", example = "16.00")
        BigDecimal lineTotal
) {
}
