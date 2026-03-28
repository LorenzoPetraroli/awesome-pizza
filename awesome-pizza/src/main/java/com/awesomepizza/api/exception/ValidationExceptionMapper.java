package com.awesomepizza.api.exception;

import com.awesomepizza.api.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .map(this::formatViolation)
                .findFirst()
                .orElse("Validation error");

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(Instant.now(), 400, "Bad Request", message))
                .build();
    }

    private String formatViolation(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : "";
        return propertyPath.isBlank()
                ? violation.getMessage()
                : propertyPath + ": " + violation.getMessage();
    }
}
