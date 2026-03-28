package com.awesomepizza.application.model;

import com.awesomepizza.domain.model.Order;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name = "KitchenSummary", description = "Aggregated view of the current kitchen state")
public record KitchenSummary(
        @Schema(description = "Order currently in preparation, if present")
        Order currentOrder,

        @Schema(description = "Orders still queued in PLACED status, sorted with FIFO priority")
        List<Order> queuedOrders,

        @Schema(description = "Orders already marked as ready and waiting for completion")
        List<Order> readyOrders
) {

    public KitchenSummary {
        queuedOrders = List.copyOf(queuedOrders);
        readyOrders = List.copyOf(readyOrders);
    }
}
