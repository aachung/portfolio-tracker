package persistence;

import model.Account;
import model.StockHolding;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkAccount(String name, int balance, Account account) {
        assertEquals(name, account.getName());
        assertEquals(balance, account.getBalance());
    }

    protected void checkHolding(String symbol, int shares, double price, double averageCost,
                                double marketValue, double bookCost, double gainLoss, StockHolding holding) {
        assertEquals(symbol, holding.getSymbol());
        assertEquals(shares, holding.getShares());
        assertEquals(price, holding.getPrice());
        assertEquals(averageCost, holding.getAverageCost());
        assertEquals(marketValue, holding.getMarketValue());
        assertEquals(bookCost, holding.getBookCost());
        assertEquals(gainLoss, holding.getGainLoss());
    }
}
