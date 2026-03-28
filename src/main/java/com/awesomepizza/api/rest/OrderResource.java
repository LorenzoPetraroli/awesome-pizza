package com.awesomepizza.api.rest;

import com.awesomepizza.api.dto.request.CreateOrderRequest;
import com.awesomepizza.api.dto.response.ErrorResponse;
import com.awesomepizza.api.dto.response.OrderResponse;
import com.awesomepizza.api.mapper.OrderDtoMapper;
import com.awesomepizza.api.validation.OrderCodeValidator;
import com.awesomepizza.application.service.OrderService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Orders", description = "Public APIs for customer order creation and status tracking")
public class OrderResource {

    private final OrderService orderService;
    private final OrderDtoMapper orderDtoMapper;
    private final OrderCodeValidator orderCodeValidator;

    public OrderResource(OrderService orderService, OrderDtoMapper orderDtoMapper, OrderCodeValidator orderCodeValidator) {
        this.orderService = orderService;
        this.orderDtoMapper = orderDtoMapper;
        this.orderCodeValidator = orderCodeValidator;
    }

    @POST
    @Operation(
            summary = "Create a new order",
            description = "Creates a new customer order in PLACED status and returns the public tracking code"
    )
    @APIResponse(
            responseCode = "201",
            description = "Order created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OrderResponse.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response createOrder(@Valid CreateOrderRequest request) {
        OrderResponse response = orderDtoMapper.toResponse(
                orderService.createOrder(request.customerName(), orderDtoMapper.toDomainItems(request.items()))
        );
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @GET
    @Path("/{code}")
    @Operation(
            summary = "Get an order by code",
            description = "Returns the current status and full details of the order associated with the public code"
    )
    @APIResponse(
            responseCode = "200",
            description = "Order found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OrderResponse.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Order not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid order code format",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
    )
    public OrderResponse getOrder(@PathParam("code") String code) {
        orderCodeValidator.validate(code);
        return orderDtoMapper.toResponse(orderService.getOrderByCode(code));
    }
}
