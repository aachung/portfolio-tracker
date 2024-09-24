package persistence;

import model.Account;
import model.Portfolio;
import model.StockHolding;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Portfolio portfolio = new Portfolio("Test Portfolio");
            JsonWriter writer = new JsonWriter("./data\0asdf.json");
            writer.open();
            fail("IOException should be thrown");
        } catch (IOException e) {
            // all good
        }
    }

    @Test
    void testWriterEmptyPortfolio() {
        try {
            Portfolio portfolio = new Portfolio("Empty Portfolio");
            JsonWriter writer = new JsonWriter("./data/testEmptyPortfolio.json");
            writer.open();
            writer.write(portfolio);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyPortfolio.json");
            portfolio = reader.read();
            assertEquals("Empty Portfolio", portfolio.getName());
            Map<Integer, Account> accounts = portfolio.getAccounts();
            assertEquals(0, accounts.size());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPortfolio() {
        try {
            Portfolio portfolio = new Portfolio("Test Portfolio");
            Account account1 = new Account("Test Account 1", 123000);
            portfolio.addAccount(account1);
            account1.addStock("ABC", 100, 35, 20 ,
                    3500, 2000, 1500);
            account1.addStock("DEF", 800, 10, 10,
                    8000, 8000, 0);
            Account account2 = new Account("Test Account 2", 456000);
            portfolio.addAccount(account2);
            account2.addStock("GHI", 100, 0.85, 0.88 ,
                    85, 88, -3);
            JsonWriter writer = new JsonWriter("./data/testGeneralPortfolio.json");
            writer.open();
            writer.write(portfolio);
            writer.close();

            JsonReader reader = new JsonReader("./data/testGeneralPortfolio.json");
            portfolio = reader.read();
            assertEquals("Test Portfolio", portfolio.getName());
            Map<Integer, Account> accounts = portfolio.getAccounts();
            assertEquals(2, accounts.size());
            checkAccount("Test Account 1", 123000, portfolio.retrieveAccount(1));
            checkAccount("Test Account 2", 456000, portfolio.retrieveAccount(2));
            Map<String, StockHolding> holdings1 = portfolio.retrieveAccount(1).getHoldings();
            assertEquals(2, holdings1.size());
            checkHolding("ABC", 100, 35, 20,
                    3500, 2000, 1500, holdings1.get("ABC"));
            checkHolding("DEF", 800, 10, 10,
                    8000, 8000, 0, holdings1.get("DEF"));
            Map<String, StockHolding> holdings2 = portfolio.retrieveAccount(2).getHoldings();
            assertEquals(1, holdings2.size());
            checkHolding("GHI", 100, 0.85, 0.88,
                    85, 88, -3, holdings2.get("GHI"));
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}
