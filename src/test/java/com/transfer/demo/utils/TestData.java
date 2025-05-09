package com.transfer.demo.utils;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.model.Account;
import com.transfer.demo.model.User;
import com.transfer.demo.request.TransferRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TestData {

    public static Account getFirstAccount() {
        Account account = new Account();
        account.setUser(new User(1L, null, null, null, Set.of(), Set.of(), account));
        account.setBalance(new BigDecimal("200"));
        return account;
    }

    public static Account getSecondAccount() {
        Account account = new Account();
        account.setUser(new User(2L, null, null, null, Set.of(), Set.of(), account));
        account.setBalance(new BigDecimal("50"));
        return account;
    }

    public static TransferRequest getTransferRequest() {
        return new TransferRequest(2L, new BigDecimal("100"));
    }

    public static TransferRequest getWrongTransferRequest() {
        return new TransferRequest(1L, new BigDecimal("100"));
    }

    public static TransferRequest getInsufficientFundsRequest() {
        return new TransferRequest(2L, new BigDecimal("500"));
    }

    public static JwtUser getJwtUser() {
        return new JwtUser(1L, null, null, List.of());
    }
}
