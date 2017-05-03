package com.priceminister.account.exception;


import java.text.DecimalFormat;


public class IllegalBalanceException extends Exception {
    
    private static final long serialVersionUID = -9204191749972551939L;
    public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");
    
    public IllegalBalanceException(Double illegalBalance) {
        super(AccountErrors.ILLEGAL_BALANCE + " : " + DECIMAL_FORMAT.format(illegalBalance));
    }
}
