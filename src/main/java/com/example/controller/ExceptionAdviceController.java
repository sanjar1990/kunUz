package com.example.controller;

import com.example.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(AppBadRequestException.class)
    public ResponseEntity<String> handler(AppBadRequestException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String>handler(ItemNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(ItemNotAvailable.class)
    public ResponseEntity<String>handler(ItemNotAvailable e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(ItemAlreadyExists.class)
    public ResponseEntity<String>handler(ItemAlreadyExists e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String>handler(UnAuthorizedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    @ExceptionHandler(AppMethodNotAllowedException.class)
    public ResponseEntity<String>handler(AppMethodNotAllowedException e){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
    }

}
