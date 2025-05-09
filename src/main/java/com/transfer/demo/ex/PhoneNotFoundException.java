package com.transfer.demo.ex;

public class PhoneNotFoundException extends RuntimeException {

    public PhoneNotFoundException(String message){
        super(message);
    }
}
