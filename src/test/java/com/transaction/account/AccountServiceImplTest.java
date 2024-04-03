package com.transaction.account;

import com.transaction.account.entity.Account;

import com.transaction.account.payload.DepositRequest;

import com.transaction.account.payload.Response;

import com.transaction.account.payload.UpdateRequest;

import com.transaction.account.repository.AccountRepository;

import com.transaction.account.service.AccountService;

import com.transaction.account.service.accServiceImpl.AccountServiceImpl;


import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.MockitoAnnotations;


import java.util.Calendar;

import java.util.Date;

import java.util.Optional;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


class AccountServiceImplTest {


    @Mock

    private AccountRepository accountRepository;


    @InjectMocks

    private AccountServiceImpl accountService;


    @BeforeEach

    void setUp() {

        MockitoAnnotations.openMocks(this);

    }




    @Test

    void deposit_ShouldUpdateAccountWithDepositAmount() {

        Long accountId = 1L;

        Double availableAmount = 1000.0;

        Double depositAmount = 500.0;

        Double expectedAmount = 1500.0;


        Account account = new Account();

        account.setId(accountId);

        account.setAmount(availableAmount);


        DepositRequest depositRequest = new DepositRequest();

        depositRequest.setAmount(depositAmount);


        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class))).thenReturn(account);


        Account result = accountService.deposit(accountId, depositRequest);


        assertNotNull(result);

        assertEquals(expectedAmount, result.getAmount());

        verify(accountRepository, times(1)).findById(accountId);

        verify(accountRepository, times(1)).save(account);

    }




    @Test

    void withdraw_ShouldNotUpdateAccount_WhenBalanceIsInsufficient() {

        Long accountId = 1L;

        Double availableAmount = 100.0;

        Double withdrawAmount = 500.0;


        Account account = new Account();

        account.setId(accountId);

        account.setAmount(availableAmount);


        DepositRequest withdrawRequest = new DepositRequest();

        withdrawRequest.setAmount(withdrawAmount);


        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));


        Response response = accountService.withdraw(accountId, withdrawRequest);


        assertNotNull(response);

        assertEquals("Insufficient balance", response.getMessage());

        assertEquals(availableAmount, account.getAmount());

        verify(accountRepository, times(1)).findById(accountId);

        verify(accountRepository, never()).save(any(Account.class));

    }



    @Test

    void reactivateAccount_ShouldSetAccountStatusToTrue() {

        Long accountId = 1L;

        boolean initialStatus = false;


        Account account = new Account();

        account.setId(accountId);

        account.setStatus(initialStatus);


        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class))).thenReturn(account);


        accountService.reactivateAccount(accountId);


        assertTrue(account.isStatus());

        verify(accountRepository, times(1)).findById(accountId);

        verify(accountRepository, times(1)).save(account);

    }


}