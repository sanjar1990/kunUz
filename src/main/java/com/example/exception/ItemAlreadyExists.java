package com.example.exception;

public class ItemAlreadyExists extends RuntimeException{
    public ItemAlreadyExists(String message){
        super(message);
    }
}
