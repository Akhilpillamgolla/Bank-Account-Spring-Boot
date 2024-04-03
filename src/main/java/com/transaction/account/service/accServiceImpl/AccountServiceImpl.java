package com.transaction.account.service.accServiceImpl;

import com.transaction.account.entity.Account;
import com.transaction.account.payload.DepositRequest;
import com.transaction.account.payload.Response;
import com.transaction.account.payload.UpdateRequest;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account create(Account account) {
        account.setLastDebited(new Date());
        return accountRepository.save(account);
    }

    @Override
    public Account deposit(Long id,DepositRequest depositRequest) {
        Account account=accountRepository.findById(id).get();
        Double availableamount=account.getAmount();
        Double sum=availableamount+depositRequest.getAmount();
        account.setAmount(sum);
        account.setLastDebited(new Date());
        accountRepository.save(account);
        return account;
    }
    @Override
    public Response withdraw(Long id, DepositRequest depositRequest) {
        Account account=accountRepository.findById(id).get();
        Double availAmount=account.getAmount();
        Response res=new Response();
        if(availAmount>depositRequest.getAmount()){
            account.setAmount(availAmount-depositRequest.getAmount());
            account.setLastDebited(new Date());
            accountRepository.save(account);
            res.setMessage("Amount withdrawl Successfull");
        }else{
            res.setMessage("Insufficient balance");
        }
        return res;
    }

    @Override
    public Double checkBalanceById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            return account.getAmount();
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    @Override
    public Account updateAccount(Long id, UpdateRequest updateRequest) {
        Account account=accountRepository.findById(id).get();
        account.setEmail(updateRequest.getEmail());
        account.setFirstName(updateRequest.getFirstName());
        account.setLastName(updateRequest.getLastName());
        return accountRepository.save(account);
    }



    @Override
    public Response deleteAcc(Long id) {
        Response res=new Response();
        accountRepository.deleteById(id);
        res.setMessage("Account Deleted SuccessFully");
        return res;
    }

    @Override
    public Account viewDetails(Long id) {
        return accountRepository.findById(id).get();
    }
    
  

    @Override
    public Account findAccountByMobileNumber(String mobileNumber) {
        return accountRepository.findByMobileNumber(mobileNumber).orElse(null);
    }

    @Override
    public Account findAccountByName(String firstName, String lastName) {
        return accountRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);
    }
    
    @Override
    public void markAccountAsDormant(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            Date lastTransactionDate = account.getLastDebited();
            Date currentDate = new Date();
            long daysSinceLastTransaction = TimeUnit.DAYS.convert(currentDate.getTime() - lastTransactionDate.getTime(), TimeUnit.MILLISECONDS);
            if (daysSinceLastTransaction > 365) {
                account.setStatus(false); // Mark account as dormant
                accountRepository.save(account);
            }
        }
    }
    
    @Override
    public void reactivateAccount(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            account.setStatus(true); // Reactivate the account
            accountRepository.save(account);
        }
    }
    @Override
    public double calculateInterest(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            if (account.isStatus() && isValidInterestCalculationPeriod(account)) {
                double interestRate = getInterestRate(account.getType());
                Date lastDebitedDate = account.getLastDebited();
                Date currentDate = new Date();
                long elapsedMonths = getElapsedMonths(lastDebitedDate, currentDate);
                return calculateInterestAmount(account.getAmount(), interestRate, elapsedMonths);
            }
        }
        return 0.0;
    }

    private boolean isValidInterestCalculationPeriod(Account account) {
        Calendar lastDebitedDate = Calendar.getInstance();
        lastDebitedDate.setTime(account.getLastDebited());

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        int lastDebitedYear = lastDebitedDate.get(Calendar.YEAR);
        int lastDebitedMonth = lastDebitedDate.get(Calendar.MONTH);

        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);

        return currentYear == lastDebitedYear && currentMonth == lastDebitedMonth;
    }

    private double getInterestRate(String accountType) {
        if (accountType.equalsIgnoreCase("savings")) {
            return 3.5;
        } else if (accountType.equalsIgnoreCase("current")) {
            return 2.0;
        }
        return 0.0;
    }

    private long getElapsedMonths(Date fromDate, Date toDate) {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromDate);

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toDate);

        long monthsBetween = 0;
        while (fromCal.before(toCal)) {
            fromCal.add(Calendar.MONTH, 1);
            monthsBetween++;
        }
        return monthsBetween;
    }

    private double calculateInterestAmount(double principal, double interestRate, long elapsedMonths) {
        double annualInterestRate = interestRate / 100;
        double monthlyInterestRate = annualInterestRate / 12;
        return principal * monthlyInterestRate * elapsedMonths;
    }



}
