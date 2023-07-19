package com.example.exception;

public class MethodNotAllowedException extends RuntimeException{
    public MethodNotAllowedException(String message){
        super(message);
    }
}
