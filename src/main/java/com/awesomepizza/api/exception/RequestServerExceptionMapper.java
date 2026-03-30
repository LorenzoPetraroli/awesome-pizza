package com.awesomepizza.api.exception;

import com.awesomepizza.api.dto.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.Instant;

// Handles request parsing errors in Quarkus REST Reactive before the generic JAX-RS mappers run.
@ApplicationScoped
public class RequestServerExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapBadRequest(BadRequestException exception) {
        return buildBadRequestResponse(exception);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapJsonProcessing(JsonProcessingException exception) {
        return buildBadRequestResponse(exception);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapInvalidFormat(InvalidFormatException exception) {
        return buildBadRequestResponse(exception);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapMismatchedInput(MismatchedInputException exception) {
        return buildBadRequestResponse(exception);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapJsonMapping(JsonMappingException exception) {
        return buildBadRequestResponse(exception);
    }

    private RestResponse<ErrorResponse> buildBadRequestResponse(Throwable throwable) {
        return RestResponse.status(
                RestResponse.Status.BAD_REQUEST,
                new ErrorResponse(
                        Instant.now(),
                        400,
                        "Bad Request",
                        RequestParsingErrorMessageFactory.buildMessage(throwable)
                )
        );
    }
}
