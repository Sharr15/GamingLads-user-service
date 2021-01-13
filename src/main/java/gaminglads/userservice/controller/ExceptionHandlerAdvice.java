package gaminglads.userservice.controller;

import gaminglads.userservice.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e){
        log.error("Credentials were invalid", e);
        return new ResponseEntity<>("Credentials were invalid", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTokenNotCreatedException(TokenNotCreatedException e){
        log.error("Token not created", e);
        return new ResponseEntity<>("Token not created", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e){
        log.error("User not found", e);
        return new ResponseEntity<>("User not found", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotSavedException(UserNotSavedException e){
        log.error("User could not be saved", e);
        return new ResponseEntity<>("User could not be saved", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleProfileNotCreatedException(ProfileNotCreatedException e){
        log.error("Profile could not be created", e);
        return new ResponseEntity<>("Profile could not be created", HttpStatus.CONFLICT);
    }
}
