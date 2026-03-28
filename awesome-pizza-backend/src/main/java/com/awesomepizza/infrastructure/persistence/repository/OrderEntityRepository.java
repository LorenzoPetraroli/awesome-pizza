package com.awesomepizza.infrastructure.persistence.repository;

import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderStatus;
import com.awesomepizza.domain.repository.OrderRepository;
import com.awesomepizza.infrastructure.persistence.entity.OrderEntity;
import com.awesomepizza.infrastructure.persistence.mapper.OrderPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderEntityRepository implements PanacheRepository<OrderEntity>, OrderRepository {

    private final OrderPersistenceMapper mapper;

    public OrderEntityRepository(OrderPersistenceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity managedEntity = getEntityManager().merge(entity);
        getEntityManager().flush();
        return mapper.toDomain(managedEntity);
    }

    @Override
    public Optional<Order> findByCode(String code) {
        return getEntityManager().createQuery(
                        "select distinct o from OrderEntity o left join fetch o.items where o.code = :code",
                        OrderEntity.class
                )
                .setParameter("code", code)
                .getResultStream()
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findCurrentInPreparation() {
        return findCurrentInPreparationEntity();
    }

    @Override
    public List<Order> findPlacedOrdersOrderedByCreation() {
        return findByStatusOrdered(OrderStatus.PLACED, "createdAt");
    }

    @Override
    public List<Order> findReadyOrdersOrderedByUpdate() {
        return findByStatusOrdered(OrderStatus.READY, "updatedAt");
    }

    private Optional<Order> findCurrentInPreparationEntity() {
        return getEntityManager().createQuery(
                        "select distinct o from OrderEntity o left join fetch o.items where o.status = :status order by o.createdAt asc",
                        OrderEntity.class
                )
                .setParameter("status", OrderStatus.IN_PREPARATION)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .map(mapper::toDomain);
    }

    private List<Order> findByStatusOrdered(OrderStatus status, String orderByField) {
        return getEntityManager().createQuery(
                        "select distinct o from OrderEntity o left join fetch o.items where o.status = :status order by o." + orderByField + " asc",
                        OrderEntity.class
                )
                .setParameter("status", status)
                .getResultStream()
                .map(mapper::toDomain)
                .toList();
    }
}
