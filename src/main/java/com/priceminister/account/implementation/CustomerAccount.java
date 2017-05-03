package com.priceminister.account.implementation;

import com.priceminister.account.Account;
import com.priceminister.account.AccountRule;
import com.priceminister.account.exception.AccountErrors;
import com.priceminister.account.exception.IllegalBalanceException;

import java.math.BigDecimal;


public class CustomerAccount implements Account {

    private AccountRule rule;
    private BigDecimal balance;

    public CustomerAccount() {
        this.balance = BigDecimal.ZERO;
        this.rule = null;
    }

    public CustomerAccount(AccountRule rule) {
        this();
        if (rule == null)
            throw new RuntimeException(AccountErrors.FIELD_NULL + " : rule");
        this.rule = rule;
    }

    public void add(Double addedAmount) {
        if (addedAmount == null)
            throw new RuntimeException(AccountErrors.PARAMETER_NULL + " : addedAmount");
        if (addedAmount < 0)
            throw new RuntimeException(AccountErrors.PARAMETER_BELOW_0 + " : addedAmount");
        this.balance = this.balance.add(BigDecimal.valueOf(addedAmount));
    }

    public Double getBalance() {
        return this.balance.doubleValue();
    }

    public Double withdrawAndReportBalance(Double withdrawnAmount, AccountRule rule)
    		throws IllegalBalanceException {
        if (withdrawnAmount == null)
            throw new RuntimeException(AccountErrors.PARAMETER_NULL + " : withdrawnAmount");
        if (rule == null)
            throw new RuntimeException(AccountErrors.PARAMETER_NULL + " : rule");

        BigDecimal potentialBalance = balance.subtract(
                BigDecimal.valueOf(withdrawnAmount < 0 ? -withdrawnAmount : withdrawnAmount));
        if (rule.withdrawPermitted(potentialBalance.doubleValue())) {
            this.balance = potentialBalance;
            return potentialBalance.doubleValue();
        }
        throw new IllegalBalanceException(potentialBalance.doubleValue());
    }

    public Double withdrawAndReportBalance(Double withdrawnAmount)
            throws IllegalBalanceException {
        return withdrawAndReportBalance(withdrawnAmount, this.rule);
    }
}
