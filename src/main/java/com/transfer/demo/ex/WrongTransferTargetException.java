package com.transfer.demo.ex;

public class WrongTransferTargetException extends RuntimeException {

    public WrongTransferTargetException(String message){
        super(message);
    }
}
