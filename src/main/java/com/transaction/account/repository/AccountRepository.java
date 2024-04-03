package com.transaction.account.repository;

import com.transaction.account.entity.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
	
	Optional<Account> findByMobileNumber(String mobileNumber);
	Optional<Account> findByFirstNameAndLastName(String firstName, String lastName);

}
