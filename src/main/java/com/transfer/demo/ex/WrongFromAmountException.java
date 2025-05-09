package com.transfer.demo.ex;

public class WrongFromAmountException extends RuntimeException {

    public WrongFromAmountException(String message){
        super(message);
    }
}
