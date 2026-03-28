package com.awesomepizza.api.mapper;

import com.awesomepizza.api.dto.request.CreateOrderItemRequest;
import com.awesomepizza.api.dto.response.KitchenSummaryResponse;
import com.awesomepizza.api.dto.response.OrderItemResponse;
import com.awesomepizza.api.dto.response.OrderResponse;
import com.awesomepizza.domain.factory.OrderItemFactory;
import com.awesomepizza.application.model.KitchenSummary;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderItem;
import com.awesomepizza.domain.model.OrderStatus;
import com.awesomepizza.domain.model.PizzaType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "cdi")
public abstract class OrderDtoMapper {

    @Inject
    protected OrderItemFactory orderItemFactory;

    public abstract List<OrderItem> toDomainItems(List<CreateOrderItemRequest> items);

    protected OrderItem toDomainItem(CreateOrderItemRequest request) {
        return orderItemFactory.create(generateItemId(), request.pizzaType(), request.quantity());
    }

    @Mapping(target = "status", source = "status", qualifiedByName = "orderStatusToString")
    @Mapping(target = "totalPrice", source = "items", qualifiedByName = "itemsToTotalPrice")
    public abstract OrderResponse toResponse(Order order);

    @Mapping(target = "pizzaType", source = "pizzaType", qualifiedByName = "pizzaTypeToString")
    @Mapping(target = "displayName", source = "pizzaType", qualifiedByName = "pizzaTypeToDisplayName")
    @Mapping(target = "unitPrice", source = "pizzaType", qualifiedByName = "pizzaTypeToUnitPrice")
    @Mapping(target = "lineTotal", source = ".", qualifiedByName = "orderItemToLineTotal")
    protected abstract OrderItemResponse toItemResponse(OrderItem item);

    @Mapping(target = "currentOrder", source = "currentOrder")
    @Mapping(target = "queuedOrders", source = "queuedOrders")
    @Mapping(target = "readyOrders", source = "readyOrders")
    public abstract KitchenSummaryResponse toKitchenSummaryResponse(KitchenSummary summary);

    protected UUID generateItemId() {
        return UUID.randomUUID();
    }

    @Named("orderStatusToString")
    protected String orderStatusToString(OrderStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("pizzaTypeToString")
    protected String pizzaTypeToString(PizzaType pizzaType) {
        return pizzaType != null ? pizzaType.name() : null;
    }

    @Named("pizzaTypeToDisplayName")
    protected String pizzaTypeToDisplayName(PizzaType pizzaType) {
        return pizzaType != null ? pizzaType.getDisplayName() : null;
    }

    @Named("pizzaTypeToUnitPrice")
    protected BigDecimal pizzaTypeToUnitPrice(PizzaType pizzaType) {
        return pizzaType != null ? pizzaType.getUnitPrice() : null;
    }

    @Named("orderItemToLineTotal")
    protected BigDecimal orderItemToLineTotal(OrderItem item) {
        if (item == null || item.pizzaType() == null) {
            return null;
        }
        return item.pizzaType().getUnitPrice().multiply(BigDecimal.valueOf(item.quantity()));
    }

    @Named("itemsToTotalPrice")
    protected BigDecimal itemsToTotalPrice(List<OrderItem> items) {
        return items == null
                ? BigDecimal.ZERO
                : items.stream()
                        .map(this::orderItemToLineTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
