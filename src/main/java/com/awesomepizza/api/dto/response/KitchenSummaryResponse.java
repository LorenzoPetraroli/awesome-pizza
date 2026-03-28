package com.awesomepizza.api.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

// Response payload exposing the current kitchen overview for polling clients.
@Schema(name = "KitchenSummaryResponse", description = "Kitchen snapshot returned to support frontend polling")
public record KitchenSummaryResponse(
        @Schema(description = "Order currently in preparation, if any")
        OrderResponse currentOrder,

        @Schema(description = "FIFO queue of orders still waiting to be started")
        List<OrderResponse> queuedOrders,

        @Schema(description = "Orders already marked ready and waiting for completion")
        List<OrderResponse> readyOrders
) {
}
