package com.awesomepizza.api.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

// Standard error payload returned by the API.
@Schema(name = "ErrorResponse", description = "Error payload returned by the API")
public record ErrorResponse(
        @Schema(description = "Timestamp when the error payload was generated", example = "2026-03-27T20:45:12Z")
        Instant timestamp,

        @Schema(description = "HTTP status code", example = "409")
        int status,

        @Schema(description = "Short HTTP error name", example = "Conflict")
        String error,

        @Schema(description = "Detailed error message", example = "Another order is already in preparation")
        String message
) {
}
