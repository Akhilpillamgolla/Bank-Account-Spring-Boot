package com.transaction.account.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest {
    private Double amount;
}
