package com.priceminister.account.exception;

public enum AccountErrors
{
    ILLEGAL_BALANCE("Illegal account balance"),
    PARAMETER_BELOW_0("Parameter cannot be below 0"),
    PARAMETER_NULL("Parameter cannot be null"),
    FIELD_NULL("Class field cannot be null");

    private String code;

    AccountErrors(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }
}
