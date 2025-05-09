package com.transfer.demo.ex;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String message){
        super(message);
    }
}
