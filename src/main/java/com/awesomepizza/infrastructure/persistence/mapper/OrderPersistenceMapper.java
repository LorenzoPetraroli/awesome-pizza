package com.awesomepizza.infrastructure.persistence.mapper;

import com.awesomepizza.domain.factory.OrderItemFactory;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderItem;
import com.awesomepizza.infrastructure.persistence.entity.OrderEntity;
import com.awesomepizza.infrastructure.persistence.entity.OrderItemEntity;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "cdi")
public abstract class OrderPersistenceMapper {

    @Inject
    protected OrderItemFactory orderItemFactory;

    public abstract Order toDomain(OrderEntity entity);

    @Mapping(target = "items", ignore = true)
    public abstract OrderEntity toEntity(Order order);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", expression = "java(resolveItemId(item))")
    protected abstract OrderItemEntity toEntityItem(OrderItem item);

    protected abstract List<OrderItemEntity> toEntityItems(List<OrderItem> items);

    protected OrderItem toDomainItem(OrderItemEntity entity) {
        return orderItemFactory.create(entity.getId(), entity.getPizzaType(), entity.getQuantity());
    }

    protected UUID resolveItemId(OrderItem item) {
        return item.id() != null ? item.id() : UUID.randomUUID();
    }

    @AfterMapping
    protected void attachItems(Order source, @MappingTarget OrderEntity target) {
        target.replaceItems(toEntityItems(source.getItems()));
    }
}
