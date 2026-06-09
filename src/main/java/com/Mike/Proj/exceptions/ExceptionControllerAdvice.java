package com.Mike.Proj.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    
    @ExceptionHandler(value = CustomException.class)
    public final ResponseEntity<String> handleCustomException(CustomException exception){
        logger.error("Custom exception occurred", exception);
        // Return generic error message, log details securely
        return new ResponseEntity<>("An error occurred while processing your request", HttpStatus.BAD_REQUEST);     
    }
    
    @ExceptionHandler(value = AuthenticationFailException.class)
    public final ResponseEntity<String> handleFailedAuthentication(AuthenticationFailException exception){
        logger.warn("Authentication failed", exception);
        // Return generic error message, do not reveal whether email exists
        return new ResponseEntity<>("Authentication failed", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ProductNotExistsException.class)
    public final ResponseEntity<String> handleProductNotExistsException(ProductNotExistsException exception){
        logger.error("Product not found", exception);
        return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
    }

}
