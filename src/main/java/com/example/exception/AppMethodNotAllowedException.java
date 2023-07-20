package com.example.exception;

public class AppMethodNotAllowedException extends RuntimeException{
    public AppMethodNotAllowedException(String message){
        super(message);
    }
}
