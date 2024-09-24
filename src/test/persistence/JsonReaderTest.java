package persistence;

import model.Account;
import model.Portfolio;
import model.StockHolding;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/asdf.json");
        try {
            Portfolio portfolio = reader.read();
            fail("IOException should be thrown");
        } catch (IOException e) {
            // all good
        }
    }

    @Test
    void testReaderEmptyPortfolio() {
        JsonReader reader = new JsonReader("./data/testEmptyPortfolio.json");
        try {
            Portfolio portfolio = reader.read();
            assertEquals("Empty Portfolio", portfolio.getName());
            Map<Integer, Account> accounts = portfolio.getAccounts();
            assertEquals(0, accounts.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderPortfolio() {
        JsonReader reader = new JsonReader("./data/testGeneralPortfolio.json");
        try {
            Portfolio portfolio = reader.read();
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
            fail("Couldn't read from file");
        }
    }
}
