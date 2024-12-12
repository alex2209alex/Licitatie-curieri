package ro.fmi.unibuc.licitatie_curieri.common;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.fmi.unibuc.licitatie_curieri.common.exception.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<GenericApplicationError> handleInternalServerErrorException(Exception exception) {
        return getGenerocApplicationErrorResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericApplicationError> handleNotFoundException(Exception exception) {
        return getGenerocApplicationErrorResponseEntity(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<GenericApplicationError> handleForbiddenErrorException(Exception exception) {
        return getGenerocApplicationErrorResponseEntity(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            BadRequestException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<GenericApplicationError> handleBadRequestException(Exception exception) {
        return getGenerocApplicationErrorResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<GenericApplicationError> getGenerocApplicationErrorResponseEntity(Exception exception, HttpStatus status) {
        log.error(exception.getMessage());
        val error = new GenericApplicationError(status.value(), exception.getMessage());
        return ResponseEntity.status(status).body(error);
    }
}
