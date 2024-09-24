package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.max;

// Represents a portfolio with a collection of accounts
public class Portfolio implements Writable {
    private String name;
    private Map<Integer, Account> accounts;

    // REQUIRES: name has non-zero length
    // EFFECTS: creates a portfolio with given name and no accounts
    public Portfolio(String name) {
        this.name = name;
        accounts = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an account into portfolio
    public void addAccount(Account account) {
        accounts.put(getNextAccountNumber(), account);
        EventLog.getInstance().logEvent(new Event("\"" + account.getName() + "\" was added to the portfolio"));
    }

    // MODIFIES: this
    // EFFECTS: removes an account from portfolio
    public void removeAccount(int accountNumber) {
        Account account = accounts.get(accountNumber);
        accounts.remove(accountNumber);
        EventLog.getInstance().logEvent(new Event("\"" + account.getName() + "\" was removed from the portfolio"));
    }

    // EFFECTS: returns largest existing account number plus 1
    private int getNextAccountNumber() {
        int nextAccountNumber = 0;

        Set<Integer> accountNumbers = accounts.keySet();
        if (!accounts.keySet().isEmpty()) {
            nextAccountNumber = max(accountNumbers);
        }
        return nextAccountNumber + 1;
    }

    // EFFECTS: returns true if account with given account number is in portfolio
    public boolean containsAccount(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    // EFFECTS: returns account with given account number if it is in portfolio
    public Account retrieveAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public String getName() {
        return name;
    }

    public Set<Integer> getAccountNumbers() {
        return accounts.keySet();
    }

    public Map<Integer, Account> getAccounts() {
        return accounts;
    }

    @Override
    // EFFECTS: returns portfolio as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("accounts", accountsToJson());
        return json;
    }

    // source: iterates over key-value pairs in map https://www.geeksforgeeks.org/iterate-map-java/
    // EFFECTS: returns accounts as JSON array
    private JSONArray accountsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
            jsonArray.put(entry.getValue().toJson());
        }
        return jsonArray;
    }
}
