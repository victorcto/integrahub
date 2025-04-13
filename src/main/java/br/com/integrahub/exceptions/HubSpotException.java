package br.com.integrahub.exceptions;

import br.com.integrahub.dtos.HubspotErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class HubSpotException extends RuntimeException {
    private final transient HubspotErrorResponse errorResponse;
    private final HttpStatusCode statusCode;

    public HubSpotException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorResponse = null;
    }

    public HubSpotException(HubspotErrorResponse errorResponse, HttpStatusCode statusCode) {
        this.errorResponse = errorResponse;
        this.statusCode = statusCode;
    }
}
