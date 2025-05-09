package com.transfer.demo.ex;

public class UserUnauthorizedException extends RuntimeException {

    public UserUnauthorizedException(String message){
        super(message);
    }
}
