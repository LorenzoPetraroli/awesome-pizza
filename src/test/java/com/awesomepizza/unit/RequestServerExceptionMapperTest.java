package com.awesomepizza.unit;

import com.awesomepizza.api.dto.response.ErrorResponse;
import com.awesomepizza.api.exception.RequestServerExceptionMapper;
import com.awesomepizza.domain.model.PizzaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.ws.rs.BadRequestException;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestServerExceptionMapperTest {

    private final RequestServerExceptionMapper mapper = new RequestServerExceptionMapper();

    @Test
    void shouldReturnFriendlyMessageForWrappedInvalidEnumValue() {
        InvalidFormatException exception = InvalidFormatException.from(
                null,
                "Invalid pizza type",
                "MARGHERITAS",
                PizzaType.class
        );
        exception.prependPath(new Object(), "items");
        exception.prependPath(new Object(), 0);
        exception.prependPath(new Object(), "pizzaType");

        RestResponse<ErrorResponse> response = mapper.mapBadRequest(new BadRequestException(exception));
        ErrorResponse errorResponse = response.getEntity();

        assertTrue(errorResponse.message().contains("Pizza type"));
        assertTrue(errorResponse.message().contains("Allowed values"));
        assertTrue(errorResponse.message().contains("MARGHERITA"));
    }

    @Test
    void shouldReturnFriendlyMessageForInvalidEnumValue() {
        InvalidFormatException exception = InvalidFormatException.from(
                null,
                "Invalid pizza type",
                "MARGERITA",
                PizzaType.class
        );
        exception.prependPath(new Object(), "items");
        exception.prependPath(new Object(), 0);
        exception.prependPath(new Object(), "pizzaType");

        RestResponse<ErrorResponse> response = mapper.mapInvalidFormat(exception);
        ErrorResponse errorResponse = response.getEntity();

        assertTrue(errorResponse.message().contains("Pizza type"));
        assertTrue(errorResponse.message().contains("Allowed values"));
        assertTrue(errorResponse.message().contains("MARGHERITA"));
    }
}
