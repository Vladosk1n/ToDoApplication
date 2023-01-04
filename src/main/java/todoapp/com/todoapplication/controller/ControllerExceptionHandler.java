package todoapp.com.todoapplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import todoapp.com.todoapplication.exceptions.*;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RequestInputValidationException.class)
    public ResponseEntity<ErrorMessage> resourceInvalidException(RequestInputValidationException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> resourceAlreadyExistsException(TaskAlreadyExistsException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                ex.getMessage());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorMessage> resourceNotAuthenticatedException(UserNotAuthenticatedException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(TaskNotFoundException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MappingProblemException.class)
    public ResponseEntity<ErrorMessage> resourceNotMappedException(MappingProblemException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}