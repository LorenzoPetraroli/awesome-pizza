package com.awesomepizza.api.rest;

import com.awesomepizza.api.dto.response.ErrorResponse;
import com.awesomepizza.api.dto.response.KitchenSummaryResponse;
import com.awesomepizza.api.dto.response.OrderResponse;
import com.awesomepizza.api.mapper.OrderDtoMapper;
import com.awesomepizza.api.validation.OrderCodeValidator;
import com.awesomepizza.application.service.KitchenService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/kitchen")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Kitchen", description = "Operational APIs used by the kitchen to manage the order queue")
public class KitchenResource {

    private final KitchenService kitchenService;
    private final OrderDtoMapper orderDtoMapper;
    private final OrderCodeValidator orderCodeValidator;

    public KitchenResource(KitchenService kitchenService, OrderDtoMapper orderDtoMapper, OrderCodeValidator orderCodeValidator) {
        this.kitchenService = kitchenService;
        this.orderDtoMapper = orderDtoMapper;
        this.orderCodeValidator = orderCodeValidator;
    }

    @GET
    @Path("/summary")
    @Operation(
            summary = "Get kitchen summary",
            description = "Returns the active order, FIFO queue and ready orders used by frontend polling"
    )
    @APIResponse(
            responseCode = "200",
            description = "Kitchen summary retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KitchenSummaryResponse.class))
    )
    public KitchenSummaryResponse getSummary() {
        return orderDtoMapper.toKitchenSummaryResponse(kitchenService.getSummary());
    }

    @POST
    @Path("/orders/{code}/start")
    @Operation(
            summary = "Start a selected queued order",
            description = "Starts the selected order from the queue while enforcing the single active preparation rule"
    )
    @APIResponse(
            responseCode = "200",
            description = "Order started successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OrderResponse.class))
    )
    @APIResponse(
            responseCode = "409",
            description = "A new order cannot be started",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Order not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    public OrderResponse startOrder(@PathParam("code") String code) {
        orderCodeValidator.validate(code);
        return orderDtoMapper.toResponse(kitchenService.startOrderByCode(code));
    }

    @PATCH
    @Path("/orders/{code}/ready")
    @Operation(
            summary = "Mark an order as ready",
            description = "Moves an order from IN_PREPARATION to READY when the pizzas are finished"
    )
    @APIResponse(
            responseCode = "200",
            description = "Order updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OrderResponse.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Order not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "409",
            description = "Invalid order state transition",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    public OrderResponse markReady(@PathParam("code") String code) {
        orderCodeValidator.validate(code);
        return orderDtoMapper.toResponse(kitchenService.markOrderReady(code));
    }

    @PATCH
    @Path("/orders/{code}/complete")
    @Operation(
            summary = "Complete a ready order",
            description = "Closes the order lifecycle by moving the order from READY to COMPLETED"
    )
    @APIResponse(
            responseCode = "200",
            description = "Order completed successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OrderResponse.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Order not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "409",
            description = "Invalid order state transition",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    public OrderResponse complete(@PathParam("code") String code) {
        orderCodeValidator.validate(code);
        return orderDtoMapper.toResponse(kitchenService.completeOrder(code));
    }
}
