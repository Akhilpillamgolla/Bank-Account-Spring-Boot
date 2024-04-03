package com.transaction.account.controller;

import com.transaction.account.entity.Account;
import com.transaction.account.payload.DepositRequest;
import com.transaction.account.payload.Response;
import com.transaction.account.payload.UpdateRequest;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> addAccount(@RequestBody Account account){
        return new ResponseEntity<>(accountService.create(account), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable("id") Long id, @RequestBody DepositRequest depositRequest){
        return new ResponseEntity<>(accountService.deposit(id,depositRequest),HttpStatus.CREATED);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable("id") Long id,@RequestBody DepositRequest depositRequest){
        return new ResponseEntity<>(accountService.withdraw(id,depositRequest),HttpStatus.OK);
    }

    @GetMapping("/{id}/calculate-interest")
    public ResponseEntity<Double> calculateInterest(@PathVariable("id") Long id) {
        double interestAmount = accountService.calculateInterest(id);
        return new ResponseEntity<>(interestAmount, HttpStatus.OK);
    }

    @GetMapping("/{id}/checkbalance")
    public ResponseEntity<Double> checkBalanceById(@PathVariable("id") Long id) {
        Double balance = accountService.checkBalanceById(id);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Account> updateAcount(@PathVariable("id") Long id, @RequestBody UpdateRequest updateRequest){
        return new ResponseEntity<>(accountService.updateAccount(id,updateRequest),HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteAccount(@PathVariable("id") Long id){
        return new ResponseEntity<>(accountService.deleteAcc(id),HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/view/{id}")
    public ResponseEntity<Account> viewAccountDetails(@PathVariable("id") Long id) {
        Account account = accountService.viewDetails(id);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/viewByMobile/{mobileNumber}")
    public ResponseEntity<Account> viewAccountDetailsByMobileNumber(@PathVariable("mobileNumber") String mobileNumber) {
        Account account = accountService.findAccountByMobileNumber(mobileNumber);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/viewByName/{firstName}/{lastName}")
    public ResponseEntity<Account> viewAccountDetailsByName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        Account account = accountService.findAccountByName(firstName, lastName);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/mark-dormant")
    public ResponseEntity<Response> markAccountAsDormant(@PathVariable("id") Long id) {
        accountService.markAccountAsDormant(id);
        Response response = new Response("Account marked as dormant");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Response> reactivateAccount(@PathVariable("id") Long id) {
        accountService.reactivateAccount(id);
        Response response = new Response("Account reactivated");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}
