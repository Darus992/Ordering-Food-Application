package pl.dariuszgilewicz.infrastructure.model.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.dariuszgilewicz.api.dto.ErrorResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                String.join(", ", errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid argument",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "Entity not found",
                ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "Page not found",
                ex.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistAuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityAlreadyExistAuthenticationException(EntityAlreadyExistAuthenticationException ex) {
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "Email already exists",
                ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException ex) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                ex.getMessage());
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoContentException(NoContentException ex) {
        return createErrorResponse(
                HttpStatus.NO_CONTENT,
                "No content available",
                ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred",
                ex.getMessage());
    }

    public static ResponseEntity<ErrorResponseDTO> createErrorResponse(HttpStatus status, String error, String message) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                status.value(),
                error,
                message
        );
        return new ResponseEntity<>(errorResponseDTO, status);
    }
}
