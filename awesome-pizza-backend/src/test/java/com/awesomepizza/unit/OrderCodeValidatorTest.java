package com.awesomepizza.unit;

import com.awesomepizza.api.validation.OrderCodeValidator;
import com.awesomepizza.domain.exception.InvalidOrderCodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderCodeValidatorTest {

    @Test
    void shouldAcceptValidOrderCode() {
        OrderCodeValidator validator = new OrderCodeValidator();

        assertDoesNotThrow(() -> validator.validate("ORD-12AB34CD"));
    }

    @Test
    void shouldRejectInvalidOrderCode() {
        OrderCodeValidator validator = new OrderCodeValidator();

        assertThrows(InvalidOrderCodeException.class, () -> validator.validate("ORDER-123"));
    }
}
