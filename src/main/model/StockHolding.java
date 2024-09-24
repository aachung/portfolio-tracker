package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a stock holding
public class StockHolding implements Writable {
    private String symbol;
    private int shares;
    private double price;
    private double averageCost;
    private double marketValue;
    private double bookCost;
    private double gainLoss;

    // REQUIRES: stock symbol is 3- or 4-letter all-caps string
    // EFFECTS: constructs a stock holding with symbol, shares, price, average cost, market
    // value, book cost and gain/loss
    public StockHolding(String symbol, int shares, double price) {
        this.symbol = symbol;
        this.shares = shares;
        this.price = price;
        this.averageCost = price;
        this.marketValue = shares * price;
        this.bookCost = shares * averageCost;
        this.gainLoss = 0;
    }

    // Setters
    public void setShares(int shares) {
        this.shares = shares;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public void setBookCost(double bookCost) {
        this.bookCost = bookCost;
    }

    public void setGainLoss(double gainLoss) {
        this.gainLoss = gainLoss;
    }

    // Getters
    public String getSymbol() {
        return symbol;
    }

    public int getShares() {
        return shares;
    }

    public double getPrice() {
        return price;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public double getBookCost() {
        return bookCost;
    }

    public double getGainLoss() {
        return gainLoss;
    }

    @Override
    // EFFECTS: returns stock holding as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("symbol", symbol);
        json.put("shares", shares);
        json.put("price", price);
        json.put("averageCost", averageCost);
        json.put("marketValue", marketValue);
        json.put("bookCost", bookCost);
        json.put("gainLoss", gainLoss);
        return json;
    }
}
