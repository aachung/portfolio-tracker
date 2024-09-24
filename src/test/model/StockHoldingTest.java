package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockHoldingTest {
    private StockHolding testStockHolding;

    @BeforeEach
    public void setup() {
        testStockHolding = new StockHolding("TD", 100, 93);
    }

    @Test
    public void testStockHolding() {
        assertEquals("TD", testStockHolding.getSymbol());
        assertEquals(100, testStockHolding.getShares());
        assertEquals(93, testStockHolding.getPrice());
        assertEquals(93, testStockHolding.getAverageCost());
        assertEquals(9300, testStockHolding.getMarketValue());
        assertEquals(9300, testStockHolding.getBookCost());
        assertEquals(0, testStockHolding.getGainLoss());
    }

    @Test
    public void testStockHoldingToJson() {
        JSONObject json = testStockHolding.toJson();
        assertEquals(json.get("symbol"), testStockHolding.getSymbol());
        assertEquals(json.get("shares"), testStockHolding.getShares());
        assertEquals(json.get("price"), testStockHolding.getPrice());
        assertEquals(json.get("averageCost"), testStockHolding.getAverageCost());
        assertEquals(json.get("marketValue"), testStockHolding.getMarketValue());
        assertEquals(json.get("bookCost"), testStockHolding.getBookCost());
        assertEquals(json.get("gainLoss"), testStockHolding.getGainLoss());
    }
}
