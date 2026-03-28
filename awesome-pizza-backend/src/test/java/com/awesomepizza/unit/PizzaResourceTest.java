package com.awesomepizza.unit;

import com.awesomepizza.api.dto.response.PizzaTypeResponse;
import com.awesomepizza.api.rest.PizzaResource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PizzaResourceTest {

    private final PizzaResource pizzaResource = new PizzaResource();

    @Test
    void shouldExposePizzaMenuWithPrices() {
        List<PizzaTypeResponse> pizzas = pizzaResource.getAvailablePizzas();

        assertEquals(5, pizzas.size());

        PizzaTypeResponse margherita = pizzas.stream()
                .filter(pizza -> "MARGHERITA".equals(pizza.type()))
                .findFirst()
                .orElseThrow();

        assertEquals("Margherita", margherita.name());
        assertEquals(new BigDecimal("6.50"), margherita.unitPrice());
        assertTrue(margherita.ingredientsDescription().contains("mozzarella"));
    }
}
