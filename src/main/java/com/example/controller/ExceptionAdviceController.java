package com.example.controller;

import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotAvailable;
import com.example.exception.ItemNotFoundException;
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
}
