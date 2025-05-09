package com.transfer.demo.service.impl;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.dto.TransferResponseDto;
import com.transfer.demo.ex.UserNotFoundException;
import com.transfer.demo.ex.WrongFromAmountException;
import com.transfer.demo.ex.WrongTransferTargetException;
import com.transfer.demo.model.Account;
import com.transfer.demo.repository.AccountRepository;
import com.transfer.demo.request.TransferRequest;
import com.transfer.demo.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public TransferResponseDto transfer(JwtUser jwtUser, TransferRequest request) {

        log.info("Transfer service started transfer for current jwtUser: {} with request: {}", jwtUser, request);
        Long fromUserId = jwtUser.getId();
        Long toUserId = request.toUserId();
        BigDecimal amount = request.amount();

        if (fromUserId.equals(toUserId)) {
            throw new WrongTransferTargetException("Wrong transfer target user id");
        }

        Account fromAccount = accountRepository.findByUserIdForUpdate(fromUserId)
                .orElseThrow(() -> new UserNotFoundException("Sender user not found"));
        Account toAccount = accountRepository.findByUserIdForUpdate(toUserId)
                .orElseThrow(() -> new UserNotFoundException("Addressee user not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new WrongFromAmountException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new TransferResponseDto(fromAccount.getBalance());
    }

    @Scheduled(fixedRate = 30000)
    @ConditionalOnProperty()
    @Transactional
    public void increaseBalancesScheduled() {

        log.info("Transfer service started increase balances scheduled");
        List<Account> accounts = accountRepository.findAll();

        accounts.forEach(
                account -> {

                    BigDecimal maxBalance = account.getInitialDeposit().multiply(BigDecimal.valueOf(2.07));
                    BigDecimal currentBalance = account.getBalance();

                    if (currentBalance.compareTo(maxBalance) < 0) {
                        BigDecimal increasedBalance = currentBalance.multiply(BigDecimal.valueOf(1.10));
                        if (increasedBalance.compareTo(maxBalance) > 0) {
                            increasedBalance = maxBalance;
                        }
                        account.setBalance(increasedBalance);
                    }
                }
        );

        accountRepository.saveAll(accounts);
    }
}