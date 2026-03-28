package com.awesomepizza.api.rest;

import com.awesomepizza.api.dto.response.PizzaTypeResponse;
import com.awesomepizza.domain.model.PizzaType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

@Path("/api/pizzas")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Pizzas", description = "APIs that expose the pizza types available for ordering")
public class PizzaResource {

    @GET
    @Operation(
            summary = "Get available pizza types",
            description = "Returns the pizza types that a customer can choose when creating an order"
    )
    @APIResponse(
            responseCode = "200",
            description = "Pizza types retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PizzaTypeResponse.class))
    )
    public List<PizzaTypeResponse> getAvailablePizzas() {
        return Arrays.stream(PizzaType.values())
                .map(pizzaType -> new PizzaTypeResponse(
                        pizzaType.name(),
                        pizzaType.getDisplayName(),
                        pizzaType.getIngredientsDescription()
                ))
                .toList();
    }
}
