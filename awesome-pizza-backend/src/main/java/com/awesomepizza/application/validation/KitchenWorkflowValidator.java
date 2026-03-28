package com.awesomepizza.application.validation;

import com.awesomepizza.domain.exception.InvalidOrderStateException;
import com.awesomepizza.domain.exception.OrderAlreadyInPreparationException;
import com.awesomepizza.domain.model.Order;
import com.awesomepizza.domain.model.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;

// Validates application-level preconditions for kitchen workflow operations.
@ApplicationScoped
public class KitchenWorkflowValidator {

    public void validateCanMarkReady(Order order) {
        if (order.getStatus() != OrderStatus.IN_PREPARATION) {
            throw new InvalidOrderStateException("Only orders in preparation can be marked ready");
        }
    }

    public void validateCanStartSelectedOrder(Order currentInPreparation, Order selectedOrder) {
        if (currentInPreparation != null) {
            throw new OrderAlreadyInPreparationException();
        }
        if (selectedOrder.getStatus() != OrderStatus.PLACED) {
            throw new InvalidOrderStateException("Only orders in PLACED status can be started");
        }
    }

    public void validateCanComplete(Order order) {
        if (order.getStatus() != OrderStatus.READY) {
            throw new InvalidOrderStateException("Only ready orders can be completed");
        }
    }
}
