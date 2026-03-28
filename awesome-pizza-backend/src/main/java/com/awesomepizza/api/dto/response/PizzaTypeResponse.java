package com.awesomepizza.api.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

// Response payload describing a pizza type available in the menu.
@Schema(name = "PizzaTypeResponse", description = "Pizza type available for ordering")
public record PizzaTypeResponse(
        @Schema(description = "Pizza type identifier used by the API", example = "MARGHERITA")
        String type,

        @Schema(description = "Human-readable pizza name", example = "Margherita")
        String name,

        @Schema(
                description = "Short menu description of the pizza ingredients",
                example = "Tomato sauce, mozzarella, basil and extra virgin olive oil"
        )
        String ingredientsDescription,

        @Schema(description = "Unit price of the pizza in euro", example = "6.50")
        BigDecimal unitPrice
) {
}
