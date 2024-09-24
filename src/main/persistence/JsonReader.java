package persistence;

import model.Account;
import model.Portfolio;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads portfolio from JSON data stored in file
// source: methods from CPSC210 JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads portfolio from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Portfolio read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePortfolio(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses portfolio from JSON object and returns it
    private Portfolio parsePortfolio(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Portfolio portfolio = new Portfolio(name);
        addPortfolio(portfolio, jsonObject);
        return portfolio;
    }

    // MODIFIES: portfolio
    // EFFECTS: parses accounts from JSON object and adds them to portfolio
    private void addPortfolio(Portfolio portfolio, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("accounts");
        for (Object json : jsonArray) {
            JSONObject nextAccount = (JSONObject) json;
            addAccount(portfolio, nextAccount);
        }
    }

    // MODIFIES: portfolio
    // EFFECTS: parses account from JSON object and adds it to portfolio
    private void addAccount(Portfolio portfolio, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double balance = jsonObject.getDouble("balance");
        Account account = new Account(name, balance);
        JSONArray jsonArray = jsonObject.getJSONArray("holdings");
        for (Object json : jsonArray) {
            JSONObject nextHolding = (JSONObject) json;
            addHolding(account, nextHolding);
        }
        portfolio.addAccount(account);
    }

    // MODIFIES: this
    // EFFECTS: parses holding from JSON object and adds it to account
    private void addHolding(Account account, JSONObject jsonObject) {
        String symbol = jsonObject.getString("symbol");
        int shares = jsonObject.getInt("shares");
        double price = jsonObject.getDouble("price");
        double averageCost = jsonObject.getDouble("averageCost");
        double marketValue = jsonObject.getDouble("marketValue");
        double bookCost = jsonObject.getDouble("bookCost");
        double gainLoss = jsonObject.getDouble("gainLoss");
        account.addStock(symbol, shares, price, averageCost, marketValue, bookCost, gainLoss);
    }
}
