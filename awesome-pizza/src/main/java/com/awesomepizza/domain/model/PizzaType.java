package com.awesomepizza.domain.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

// Pizza types supported by the menu, including display information for the customer experience.
@Schema(name = "PizzaType", description = "Pizza type supported by the Awesome Pizza menu")
public enum PizzaType {

    MARGHERITA("Margherita", "Tomato sauce, mozzarella, basil and extra virgin olive oil", new BigDecimal("6.50")),
    DIAVOLA("Diavola", "Tomato sauce, mozzarella and spicy salami", new BigDecimal("8.00")),
    CAPRICCIOSA("Capricciosa", "Tomato sauce, mozzarella, cooked ham, mushrooms and artichokes", new BigDecimal("9.50")),
    VEGETARIANA("Vegetariana", "Tomato sauce, mozzarella, grilled zucchini, eggplants and roasted peppers", new BigDecimal("9.00")),
    QUATTRO_FORMAGGI("Quattro Formaggi", "Mozzarella, gorgonzola, fontina and parmesan", new BigDecimal("8.50"));

    private final String displayName;
    private final String ingredientsDescription;
    private final BigDecimal unitPrice;

    PizzaType(String displayName, String ingredientsDescription, BigDecimal unitPrice) {
        this.displayName = displayName;
        this.ingredientsDescription = ingredientsDescription;
        this.unitPrice = unitPrice;
    }

    @Schema(description = "Human-readable pizza name", example = "Margherita")
    public String getDisplayName() {
        return displayName;
    }

    @Schema(
            description = "Short menu description of the pizza ingredients",
            example = "Tomato sauce, mozzarella, basil and extra virgin olive oil"
    )
    public String getIngredientsDescription() {
        return ingredientsDescription;
    }

    @Schema(description = "Unit price of the pizza in euro", example = "6.50")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
