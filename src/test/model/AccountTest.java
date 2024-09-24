package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account testAccount;
    private Map<String, StockHolding> testHoldings;
    private StockHolding testHolding;

    @BeforeEach
    public void setup() {
        testAccount = new Account("Test", 10000);
        testHoldings = testAccount.getHoldings();
    }

    @Test
    public void testAccount() {
        assertEquals("Test", testAccount.getName());
        assertEquals(10000, testAccount.getBalance());
        assertTrue(testHoldings.isEmpty());
    }

    @Test
    public void testDeposit() {
        testAccount.deposit(500);
        assertEquals(10500, testAccount.getBalance());
        testAccount.deposit(40);
        testAccount.deposit(5);
        assertEquals(10545, testAccount.getBalance());
    }

    @Test
    public void testWithdrawal() {
        testAccount.withdraw(300);
        assertEquals(9700, testAccount.getBalance());
        testAccount.withdraw(20);
        testAccount.withdraw(30);
        assertEquals(9650, testAccount.getBalance());
    }

    @Test
    public void testBuyStockOnce() {
        testAccount.buyStock("BNS", 50, 74);
        assertEquals(6300, testAccount.getBalance());
        assertEquals(1, testHoldings.size());
        testHolding = testHoldings.get("BNS");
        assertEquals("BNS", testHolding.getSymbol());
        assertEquals(50, testHolding.getShares());
        assertEquals(74, testHolding.getPrice());
        assertEquals(74, testHolding.getAverageCost());
        assertEquals(3700, testHolding.getBookCost());
        assertEquals(3700, testHolding.getMarketValue());
        assertEquals(0, testHolding.getGainLoss());
    }

    @Test
    public void testBuyStockMultiple() {
        testAccount.buyStock("RY", 10, 139);
        testAccount.buyStock("RY", 20, 136);
        assertEquals(5890, testAccount.getBalance());
        assertEquals(1, testHoldings.size());
        testHolding = testHoldings.get("RY");
        assertEquals("RY", testHolding.getSymbol());
        assertEquals(30, testHolding.getShares());
        assertEquals(136, testHolding.getPrice());
        assertEquals(137, testHolding.getAverageCost());
        assertEquals(4110, testHolding.getBookCost());
        assertEquals(4080, testHolding.getMarketValue());
        assertEquals(-30, testHolding.getGainLoss());
    }

    @Test
    public void testBuyStocks() {
        testAccount.buyStock("BNS", 50, 74);
        testAccount.buyStock("RY", 10, 139);
        assertEquals(4910, testAccount.getBalance());
        assertEquals(2, testHoldings.size());
    }

    @Test
    public void testSellStock() {
        testAccount.buyStock("BMO", 50, 135);
        testAccount.sellStock("BMO", 30, 134);
        assertEquals(7270, testAccount.getBalance());
        assertEquals(1, testHoldings.size());
        testHolding = testHoldings.get("BMO");
        assertEquals("BMO", testHolding.getSymbol());
        assertEquals(20, testHolding.getShares());
        assertEquals(134, testHolding.getPrice());
        assertEquals(135, testHolding.getAverageCost());
        assertEquals(2700, testHolding.getBookCost());
        assertEquals(2680, testHolding.getMarketValue());
        assertEquals(-20, testHolding.getGainLoss());
        testAccount.sellStock("BMO",20, 136);
        assertEquals(0, testHoldings.size());
        assertEquals(9990, testAccount.getBalance());
    }

    @Test
    public void testAddStock() {
        testAccount.addStock("CM", 100, 62, 61,
                6200, 6100, 100);
        assertEquals(1, testHoldings.size());
        assertEquals(10000, testAccount.getBalance());
        testHolding = testHoldings.get("CM");
        assertEquals("CM", testHolding.getSymbol());
        assertEquals(100, testHolding.getShares());
        assertEquals(62, testHolding.getPrice());
        assertEquals(61, testHolding.getAverageCost());
        assertEquals(6200, testHolding.getMarketValue());
        assertEquals(6100, testHolding.getBookCost());
        assertEquals(100, testHolding.getGainLoss());
    }

    @Test
    public void testRemoveStock() {
        testAccount.buyStock("CM", 100, 62);
        assertEquals(1, testHoldings.size());
        testAccount.removeStock("CM");
        assertEquals(0, testHoldings.size());
    }

    @Test
    public void removeAllHoldings() {
        testAccount.buyStock("CM", 10, 62);
        testAccount.buyStock("BMO", 10, 135);
        testAccount.buyStock("BNS", 10, 74);
        assertEquals(3, testHoldings.size());
        testAccount.removeAllHoldings();
        assertEquals(0, testHoldings.size());
    }

    @Test
    public void testAccountToJson() {
        testAccount.addStock("CM", 100, 62, 61,
                6200, 6100, 100);
        JSONObject json = testAccount.toJson();
        assertEquals(json.get("name"), testAccount.getName());
        assertEquals(json.get("balance"), testAccount.getBalance());
        JSONArray jsonArray = (JSONArray) json.get("holdings");
        assertEquals(1, jsonArray.length());
    }
}