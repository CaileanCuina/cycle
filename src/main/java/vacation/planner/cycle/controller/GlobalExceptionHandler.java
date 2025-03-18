package vacation.planner.cycle.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.exceptions.BasicCycleException;
import vacation.planner.cycle.exceptions.CycleSequenceException;
import vacation.planner.cycle.exceptions.InvalidDateException;
import vacation.planner.cycle.exceptions.NoUserFoundException;
import vacation.planner.cycle.exceptions.UserNameNotUniqueException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP = "timestamp";

    @ExceptionHandler(Exception.class)
    public ErrorResponse createGeneralErrorResponse(Exception ex) {
        UUID errorId = UUID.randomUUID();
        log.error("Error id: {}: {}", errorId, ex.getStackTrace());
        return ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occurred. Forward Error with id:" + errorId)
                .property(TIMESTAMP, Instant.now())
                .build();
    }

    @ExceptionHandler({UserNameNotUniqueException.class, InvalidDateException.class, CycleSequenceException.class })
    public ErrorResponse createBadRequestException(BasicCycleException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .property("code", String.valueOf(ex.getErrorCode()))
                .property(TIMESTAMP, Instant.now())
                .build();
    }

    @ExceptionHandler(NoUserFoundException.class)
    public ErrorResponse createNotFoundException(BasicCycleException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .property("code", String.valueOf(ex.getErrorCode()))
                .property(TIMESTAMP, Instant.now())
                .build();
    }
}
