package com.example.exception;

public class ItemNotAvailable extends RuntimeException{
    public ItemNotAvailable(String message){
        super(message);
    }
}
