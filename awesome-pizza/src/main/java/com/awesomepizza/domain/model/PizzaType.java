package com.awesomepizza.domain.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

// Pizza types supported by the menu, including display information for the customer experience.
@Schema(name = "PizzaType", description = "Pizza type supported by the Awesome Pizza menu")
public enum PizzaType {

    MARGHERITA("Margherita", "Tomato sauce, mozzarella, basil and extra virgin olive oil"),
    DIAVOLA("Diavola", "Tomato sauce, mozzarella and spicy salami"),
    CAPRICCIOSA("Capricciosa", "Tomato sauce, mozzarella, cooked ham, mushrooms and artichokes"),
    VEGETARIANA("Vegetariana", "Tomato sauce, mozzarella, grilled zucchini, eggplants and roasted peppers"),
    QUATTRO_FORMAGGI("Quattro Formaggi", "Mozzarella, gorgonzola, fontina and parmesan");

    private final String displayName;
    private final String ingredientsDescription;

    PizzaType(String displayName, String ingredientsDescription) {
        this.displayName = displayName;
        this.ingredientsDescription = ingredientsDescription;
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
}
