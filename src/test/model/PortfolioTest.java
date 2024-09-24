package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    private Portfolio testPortfolio;
    private Account testAccount1;
    private Account testAccount2;

    @BeforeEach
    public void setup() {
        testPortfolio = new Portfolio("Test Portfolio");
        testAccount1 = new Account("Test Account 1", 2500);
        testAccount2 = new Account("Test Account 2", 500);
    }

    @Test
    public void testPortfolio() {
        assertEquals("Test Portfolio", testPortfolio.getName());
        assertTrue(testPortfolio.getAccounts().isEmpty());
    }

    @Test
    public void testAddAccount() {
        testPortfolio.addAccount(testAccount1);
        assertFalse(testPortfolio.getAccounts().isEmpty());
        testPortfolio.addAccount(testAccount2);
        assertTrue(testPortfolio.containsAccount(1));
        assertTrue(testPortfolio.containsAccount(2));
    }

    @Test
    public void testRemoveAccount() {
        testPortfolio.addAccount(testAccount1);
        testPortfolio.addAccount(testAccount2);
        testPortfolio.removeAccount(1);
        assertEquals(1, testPortfolio.getAccounts().size());
        testPortfolio.removeAccount(2);
        assertEquals(0, testPortfolio.getAccounts().size());
    }

    @Test
    public void testRetrieveAccount() {
        testPortfolio.addAccount(testAccount1);
        testPortfolio.addAccount(testAccount2);
        assertEquals(testAccount1, testPortfolio.retrieveAccount(1));
        assertEquals(testAccount2, testPortfolio.retrieveAccount(2));
        assertNull(testPortfolio.retrieveAccount(3));
    }

    @Test
    public void testGetAccountNumbers() {
        testPortfolio.addAccount(testAccount1);
        testPortfolio.addAccount(testAccount2);
        Set<Integer> accountNumbers = testPortfolio.getAccountNumbers();
        assertTrue(accountNumbers.contains(1));
        assertTrue(accountNumbers.contains(2));
        assertFalse(accountNumbers.contains(3));
    }

    @Test
    public void testGetAccounts() {
        testPortfolio.addAccount(testAccount1);
        testPortfolio.addAccount(testAccount2);
        Map<Integer, Account> accounts = testPortfolio.getAccounts();
        assertTrue(accounts.containsKey(1));
        assertTrue(accounts.containsKey(2));
        assertFalse(accounts.containsKey(3));
    }

    @Test
    public void testPortfolioToJson() {
        testPortfolio.addAccount(testAccount1);
        testPortfolio.addAccount(testAccount2);
        JSONObject json = testPortfolio.toJson();
        assertEquals(json.get("name"), testPortfolio.getName());
        JSONArray jsonArray = (JSONArray) json.get("accounts");
        assertEquals(2, jsonArray.length());
    }
}
