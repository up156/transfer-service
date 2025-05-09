package com.transfer.demo.service.impl;


import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.dto.TransferResponseDto;
import com.transfer.demo.ex.UserNotFoundException;
import com.transfer.demo.ex.WrongFromAmountException;
import com.transfer.demo.ex.WrongTransferTargetException;
import com.transfer.demo.model.Account;
import com.transfer.demo.repository.AccountRepository;
import com.transfer.demo.request.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.transfer.demo.utils.TestData.getFirstAccount;
import static com.transfer.demo.utils.TestData.getInsufficientFundsRequest;
import static com.transfer.demo.utils.TestData.getJwtUser;
import static com.transfer.demo.utils.TestData.getSecondAccount;
import static com.transfer.demo.utils.TestData.getTransferRequest;
import static com.transfer.demo.utils.TestData.getWrongTransferRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Account fromAccount;
    private Account toAccount;
    private TransferRequest transferRequest;
    private TransferRequest wrongTransferRequest;
    private TransferRequest insufficientFundsRequest;
    private JwtUser jwtUser;


    @BeforeEach
    void init() {
        fromAccount = getFirstAccount();
        toAccount = getSecondAccount();
        transferRequest = getTransferRequest();
        wrongTransferRequest = getWrongTransferRequest();
        insufficientFundsRequest = getInsufficientFundsRequest();
        jwtUser = getJwtUser();
    }

    @Test
    void testSuccessfulTransfer() {

        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        TransferResponseDto response = transferService.transfer(jwtUser, transferRequest);

        assertEquals(new BigDecimal("100"), response.getNewBalance());
        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
        verify((accountRepository), times(2)).findByUserIdForUpdate(anyLong());
    }

    @Test
    void testTransferToSelf_throwsException() {

        assertThrows(WrongTransferTargetException.class, () -> transferService.transfer(jwtUser, wrongTransferRequest));
        verify((accountRepository), times(0)).findByUserIdForUpdate(anyLong());
        verify((accountRepository), times(0)).save(any(Account.class));
    }

    @Test
    void testSenderNotFound_throwsException() {

        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> transferService.transfer(jwtUser, transferRequest));

        verify((accountRepository), times(1)).findByUserIdForUpdate(anyLong());
        verify((accountRepository), times(0)).save(any(Account.class));
    }

    @Test
    void testInsufficientFunds_throwsException() {

        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(WrongFromAmountException.class, () -> transferService.transfer(jwtUser, insufficientFundsRequest));

        verify((accountRepository), times(2)).findByUserIdForUpdate(anyLong());
        verify((accountRepository), times(0)).save(any(Account.class));
    }
}