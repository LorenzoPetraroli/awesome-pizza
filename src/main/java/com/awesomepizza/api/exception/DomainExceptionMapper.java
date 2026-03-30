package com.awesomepizza.api.exception;

import com.awesomepizza.api.dto.response.ErrorResponse;
import com.awesomepizza.domain.exception.InvalidOrderStateException;
import com.awesomepizza.domain.exception.InvalidOrderCodeException;
import com.awesomepizza.domain.exception.OrderAlreadyInPreparationException;
import com.awesomepizza.domain.exception.OrderNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        return switch (exception) {
            case OrderNotFoundException orderNotFoundException ->
                    build(Response.Status.NOT_FOUND, orderNotFoundException.getMessage());
            case OrderAlreadyInPreparationException orderAlreadyInPreparationException ->
                    build(Response.Status.CONFLICT, orderAlreadyInPreparationException.getMessage());
            case InvalidOrderStateException invalidOrderStateException ->
                    build(Response.Status.CONFLICT, invalidOrderStateException.getMessage());
            case InvalidOrderCodeException invalidOrderCodeException ->
                    build(Response.Status.BAD_REQUEST, invalidOrderCodeException.getMessage());
            case IllegalArgumentException illegalArgumentException ->
                    build(Response.Status.BAD_REQUEST, illegalArgumentException.getMessage());
            default ->
                    build(Response.Status.INTERNAL_SERVER_ERROR, "Unexpected server error");
        };
    }

    private Response build(Response.Status status, String message) {
        return Response.status(status)
                .entity(new ErrorResponse(Instant.now(), status.getStatusCode(), status.getReasonPhrase(), message))
                .build();
    }
}
