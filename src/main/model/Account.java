package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import java.util.HashMap;
import java.util.Map;

// Represents an account that holds cash and stock investments
public class Account implements Writable {
    private String name;
    private double balance;
    private Map<String, StockHolding> holdings;

    // REQUIRES: name has non-zero length, initialBalance >= 0
    // EFFECTS: creates an account with a given name and balance
    public Account(String name, double initialBalance) {
        this.name = name;
        balance = initialBalance;
        holdings = new HashMap<>();
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: amount is added to balance
    public void deposit(double amount) {
        balance += amount;
    }

    // REQUIRES: amount >= 0 and amount <= getBalance()
    // MODIFIES: this
    // EFFECTS: amount is withdrawn from account
    public void withdraw(double amount) {
        balance -= amount;
    }

    // REQUIRES: shares > 0, price > 0
    // MODIFIES: this
    // EFFECTS: withdraws purchase amount, and updates holdings:
    // if stock is already held, update existing stock holding, otherwise record new stock holding
    public void buyStock(String symbol, int shares, double price) {
        withdraw(shares * price);
        if (holdings.containsKey(symbol)) {
            StockHolding holding = holdings.get(symbol);
            holding.setPrice(price);
            holding.setBookCost(holding.getBookCost() + shares * price);
            holding.setShares(holding.getShares() + shares);
            holding.setAverageCost(holding.getBookCost() / holding.getShares());
            holding.setMarketValue(holding.getShares() * price);
            holding.setGainLoss(holding.getMarketValue() - holding.getBookCost());
        } else {
            holdings.put(symbol, new StockHolding(symbol, shares, price));
        }
    }

    // REQUIRES: must hold stock, shares held >= selling shares, price > 0
    // MODIFIES: this
    // EFFECTS: finds existing stock holding and deposits proceeds from sale:
    // if shares held after sale is 0, remove the stock holding, otherwise update it
    public void sellStock(String symbol, int shares, double price) {
        StockHolding holding = holdings.get(symbol);
        int i = holding.getShares() - shares;
        if (i == 0) {
            removeStock(symbol);
        } else {
            holding.setShares(i);
            holding.setPrice(price);
            holding.setBookCost(holding.getAverageCost() * i);
            holding.setMarketValue(holding.getShares() * price);
            holding.setGainLoss(holding.getMarketValue() - holding.getBookCost());
        }
        deposit(shares * price);
    }

    // MODIFIES: this
    // EFFECTS: adds a stock with fields, does not affect cash balance
    public void addStock(String symbol, int shares, double price, double averageCost,
                         double marketValue, double bookCost, double gainLoss) {
        StockHolding holding = new StockHolding(symbol, shares, price);
        holdings.put(symbol, holding);
        holding.setAverageCost(averageCost);
        holding.setMarketValue(marketValue);
        holding.setBookCost(bookCost);
        holding.setGainLoss(gainLoss);
    }

    // REQUIRES: must hold stock
    // MODIFIES: this
    // EFFECTS: finds existing stock holding and removes it
    public void removeStock(String symbol) {
        holdings.remove(symbol);
    }

    // MODIFIES: this
    // EFFECTS: removes all stock holdings in account
    public void removeAllHoldings() {
        holdings.clear();
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public Map<String, StockHolding> getHoldings() {
        return holdings;
    }

    @Override
    // EFFECTS: returns account as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("balance", balance);
        json.put("holdings", holdingsToJson());
        return json;
    }

    // source: iterates over key-value pairs in map https://www.geeksforgeeks.org/iterate-map-java/
    // EFFECTS: returns holdings as JSON array
    private JSONArray holdingsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, StockHolding> entry : holdings.entrySet()) {
            jsonArray.put(entry.getValue().toJson());
        }
        return jsonArray;
    }
}