package com.transaction.account.service;

import com.transaction.account.entity.Account;
import com.transaction.account.payload.DepositRequest;
import com.transaction.account.payload.Response;
import com.transaction.account.payload.UpdateRequest;
import org.hibernate.sql.Update;

public interface AccountService {
	Account create(Account account);

	Account deposit(Long id, DepositRequest depositRequest);

	Object withdraw(Long id, DepositRequest depositRequest);


	Account updateAccount(Long id, UpdateRequest updateRequest);

	Response deleteAcc(Long id);

	Account viewDetails(Long id);

	Account findAccountByMobileNumber(String mobileNumber);

	Account findAccountByName(String firstName, String lastName);
	
	void markAccountAsDormant(Long id);
	
	void reactivateAccount(Long id);
	
	double calculateInterest(Long id);
	 
	Double checkBalanceById(Long id);



}
