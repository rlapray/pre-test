package com.priceminister.account;


import static org.junit.Assert.*;

import com.priceminister.account.exception.AccountErrors;
import com.priceminister.account.exception.IllegalBalanceException;
import org.hamcrest.CoreMatchers;
import org.junit.*;

import com.priceminister.account.implementation.*;
import org.junit.rules.ExpectedException;


/**
 * Please create the business code, starting from the unit tests below.
 * Implement the first test, the develop the code that makes it pass.
 * Then focus on the second test, and so on.
 * 
 * We want to see how you "think code", and how you organize and structure a simple application.
 * 
 * When you are done, please zip the whole project (incl. source-code) and send it to recrutement-dev@priceminister.com
 * 
 */
public class CustomerAccountTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AccountRule defaultRule;
    private AccountRule premiumRule;

    @Before
    public void setUp() throws Exception {
        defaultRule = new CustomerAccountRuleDefault();
        premiumRule = new CustomerAccountRulePremium();
    }
    
    /**
     * Tests that an empty account always has a balance of 0.0, not a NULL.
     */
    @Test
    public void testAccountNoRuleWithoutMoneyHasZeroBalance() {
        CustomerAccount nca = new CustomerAccount();
        assertNotNull(nca.getBalance());
        assertEquals(nca.getBalance(), Double.valueOf(0));
    }

    /**
     * Tests that an empty account with a default rule always has a balance of 0.0, not a NULL.
     */
    @Test
    public void testAccountWithRuleWithoutMoneyHasZeroBalance() {
        CustomerAccount nca = new CustomerAccount(premiumRule);
        assertNotNull(nca.getBalance());
        assertEquals(nca.getBalance(), Double.valueOf(0));
    }
    
    /**
     * Adds money to the account and checks that the new balance is as expected.
     */
    @Test
    public void testAddPositiveAmount() {
        CustomerAccount nca = new CustomerAccount();
        nca.add(42.d);
        assertEquals(nca.getBalance(), Double.valueOf(42.d));
    }


    /**
     * Adds negative amount of money to the account and expect an exception.
     */
    @Test
    public void testAddNegativeAmount() {
        CustomerAccount nca = new CustomerAccount();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(CoreMatchers.containsString(AccountErrors.PARAMETER_BELOW_0.toString()));
        nca.add(-42.d);
    }

    /**
     * Adds a null amount of money to the account and expect an exception.
     */
    @Test
    public void testAddNullAmount() {
        CustomerAccount nca = new CustomerAccount();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(CoreMatchers.containsString(AccountErrors.PARAMETER_NULL.toString()));
        nca.add(null);
    }

    /**
     * Withdraw a less than we just added, with a default rule
     * @throws IllegalBalanceException
     */
    @Test
    public void testDefaultWithdraw() throws IllegalBalanceException {
        CustomerAccount nca = new CustomerAccount();
        nca.add(84.d);
        nca.withdrawAndReportBalance(42.d, defaultRule);
        assertEquals(nca.getBalance(), Double.valueOf(42.d));
    }

    /**
     * Withdraw more money than we just added, with a premium rule (up to 100e in credit possible, so no error)
     * @throws IllegalBalanceException
     */
    @Test
    public void testPremiumWithdraw() throws IllegalBalanceException {
        CustomerAccount nca = new CustomerAccount();
        nca.add(84.d);
        nca.withdrawAndReportBalance(84.d + 42.d, premiumRule);
        assertEquals(nca.getBalance(), Double.valueOf(-42.d));

    }

    /**
     * Withdraw money withotu specifying the rule when withdrawing, using constructor provided rule.
     * @throws IllegalBalanceException
     */
    @Test
    public void testWithdrawWithoutRule() throws IllegalBalanceException {
        CustomerAccount nca = new CustomerAccount(premiumRule);
        nca.add(84.d);
        nca.withdrawAndReportBalance(42.d);
        assertEquals(nca.getBalance(), Double.valueOf(42.d));
    }

    /**
     * Tests that an illegal withdrawal throws the expected exception.
     * Use the logic contained in CustomerAccountRuleDefault;
     */
    @Test
    public void testDefaultWithdrawAndReportBalanceIllegalBalance() throws IllegalBalanceException {
        CustomerAccount nca = new CustomerAccount();
        thrown.expect(IllegalBalanceException.class);
        thrown.expectMessage(CoreMatchers.containsString(IllegalBalanceException.DECIMAL_FORMAT.format(-42.d)));
        nca.withdrawAndReportBalance(42.d, defaultRule);
    }

    /**
     * Withdraw a null amount of money
     * @throws IllegalBalanceException
     */
    @Test
    public void testDefaultWithdrawAndReportBalanceIllegalBalanceWithNullValue() throws IllegalBalanceException {
        CustomerAccount nca = new CustomerAccount();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(CoreMatchers.containsString(AccountErrors.PARAMETER_NULL.toString()));
        nca.withdrawAndReportBalance(null, defaultRule);
    }

    /**
     * Withdraw money with a null rule
     * @throws IllegalBalanceException
     */
    @Test
    public void testDefaultWithdrawAndReportBalanceIllegalBalanceWithNullRule() throws IllegalBalanceException {
        CustomerAccount nca = new CustomerAccount();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(CoreMatchers.containsString(AccountErrors.PARAMETER_NULL.toString()));
        nca.withdrawAndReportBalance(42.d, null);
    }

    /**
     * Construct a CustomerAccount with a null rule
     */
    @Test
    public void testAccountWithRuleAtNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(CoreMatchers.containsString(AccountErrors.FIELD_NULL.toString()));
        CustomerAccount nca = new CustomerAccount(null);
    }

}

