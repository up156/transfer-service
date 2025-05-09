package com.transfer.demo.ex;

public class AlreadyExistException extends RuntimeException {

    public AlreadyExistException(String message){
        super(message);
    }
}
