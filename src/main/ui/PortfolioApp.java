package ui;

import model.Account;
import model.Portfolio;
import model.StockHolding;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Portfolio tracking application
// source: some methods from CPSC210 TellerApp https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class PortfolioApp {
    private Portfolio portfolio;
    private Account account;
    private Scanner input;
    private static final String JSON_STORE = "./data/portfolio.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: creates a portfolio app with given name
    public PortfolioApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        portfolio = new Portfolio("Annie's Portfolio");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runPortfolio();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runPortfolio() {
        boolean keepGoing = true;

        while (keepGoing) {
            mainMenu();
            String command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // EFFECTS: displays main menu to user
    private void mainMenu() {
        System.out.println("\nHello! What would you like to do?");
        System.out.println("\t1 -> Select an existing account");
        System.out.println("\t2 -> Create a new account");
        System.out.println("\t3 -> Delete an account");
        System.out.println("\t4 -> Save portfolio");
        System.out.println("\t5 -> Load portfolio");
        System.out.println("\tQ -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command from main menu
    private void processCommand(String command) {
        switch (command) {
            case "1":
                accountsMenu();
                break;
            case "2":
                createNewAccount();
                break;
            case "3":
                removeAccount();
                break;
            case "4":
                savePortfolio();
                break;
            case "5":
                loadPortfolio();
                break;
            default:
                System.out.println("Selection not valid... Please try again");
                break;
        }
    }

    private void removeAccount() {
        boolean keepGoing = true;

        while (keepGoing) {

            if (portfolio.getAccounts().isEmpty()) {
                System.out.println("No accounts in portfolio - please create a new account or load portfolio");
                keepGoing = false;
            } else {

                System.out.println("\nChoose an account:");
                Map<Integer, Account> accounts = portfolio.getAccounts();
                accounts.forEach((key, value) -> System.out.println("\t" + key + " -> " + value.getName()));

                try {
                    int accountNumber = input.nextInt();
                    if (portfolio.containsAccount(accountNumber)) {
                        portfolio.removeAccount(accountNumber);
                        keepGoing = false;
                    } else {
                        System.out.println("Selection not valid... Please try again");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid number corresponding to an account");
                    input.nextLine();
                }
            }
        }
    }

    // EFFECTS: displays list of existing accounts for user to select
    private void accountsMenu() {
        boolean keepGoing = true;

        while (keepGoing) {

            if (portfolio.getAccounts().isEmpty()) {
                System.out.println("No accounts in portfolio - please create a new account or load portfolio");
                keepGoing = false;
            } else {

                System.out.println("\nChoose an account:");
                Map<Integer, Account> accounts = portfolio.getAccounts();
                accounts.forEach((key, value) -> System.out.println("\t" + key + " -> " + value.getName()));

                try {
                    int accountNumber = input.nextInt();
                    if (portfolio.containsAccount(accountNumber)) {
                        account = portfolio.retrieveAccount(accountNumber);
                        keepGoing = optionsMenu();
                    } else {
                        System.out.println("Selection not valid... Please try again");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid number corresponding to an account");
                    input.nextLine();
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new account in portfolio
    private void createNewAccount() {
        boolean keepGoing = true;

        System.out.print("\nAccount name:");
        String name = input.next();

        while (keepGoing) {
            System.out.print("\nAccount balance: $");
            try {
                double balance = input.nextDouble();
                Account account = new Account(name, balance);
                portfolio.addAccount(account);
                this.account = account;
                keepGoing = optionsMenu();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a positive number");
                input.nextLine();
            }
        }
    }

    // EFFECTS: displays options menu to user, includes all transactions and return to main menu
    private boolean optionsMenu() {
        boolean keepGoing = true;

        while (keepGoing) {
            System.out.println("\nSelect from:");
            System.out.println("\t1 -> Deposit");
            System.out.println("\t2 -> Withdraw");
            System.out.println("\t3 -> Buy stock");
            System.out.println("\t4 -> Sell stock");
            System.out.println("\t5 -> View all holdings");
            System.out.println("\tR -> Return to main menu");
            String option = input.next();
            option = option.toLowerCase();
            keepGoing = processOption(option);
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: processes user command from option menu
    private boolean processOption(String option) {
        switch (option) {
            case "1":
                doDeposit();
                break;
            case "2":
                doWithdrawal();
                break;
            case "3":
                doBuy();
                break;
            case "4":
                doSell();
                break;
            case "5":
                viewAllHoldings();
                break;
            case "r":
                return false;
            default:
                System.out.println("Selection not valid... PLease try again");
                break;
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: conducts a deposit transaction
    private void doDeposit() {
        double amount = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter amount to deposit: $");
            try {
                amount = input.nextDouble();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }

        if (amount < 0) {
            System.out.println("Cannot deposit negative amount...\n");
            doDeposit();
        } else {
            account.deposit(amount);
            printNewBalance(account);
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts a withdrawal transaction
    private void doWithdrawal() {
        double amount = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter amount to withdraw: $");
            try {
                amount = input.nextDouble();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }

        if (amount < 0) {
            System.out.println("Cannot withdraw negative amount...\n");
            doWithdrawal();
        } else if (amount > account.getBalance()) {
            System.out.println("Insufficient balance on account...\n");
            doWithdrawal();
        } else {
            account.withdraw(amount);
            printNewBalance(account);
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts a purchase transaction
    private void doBuy() {
        System.out.print("Enter stock symbol to buy: ");
        String stock = input.next();
        input.nextLine();
        System.out.print("Enter shares to buy: ");
        int shares = input.nextInt();
        input.nextLine();
        System.out.print("Enter purchase price: $");
        double price = input.nextDouble();
        if (price * shares > account.getBalance()) {
            System.out.println("Insufficient funds for purchase...");
            printNewBalance(account);
        } else {
            account.buyStock(stock, shares, price);
            System.out.println("You have purchased " + shares + " " + stock + " at " + String.format("$%.2f", price));
            printNewBalance(account);
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts a sale transaction
    private void doSell() {
        System.out.print("Enter stock symbol to sell: ");
        String stock = input.next();
        input.nextLine();
        System.out.print("Enter shares to sell: ");
        int shares = input.nextInt();
        input.nextLine();
        System.out.print("Enter sale price: $");
        double price = input.nextDouble();
        account.sellStock(stock, shares, price);
        System.out.println("You have sold " + shares + " " + stock + " at " + String.format("$%.2f", price));
        printNewBalance(account);
    }

    // EFFECTS: displays cash balance and all stock holdings in selected account
    private void viewAllHoldings() {
        System.out.printf("Cash balance: $%.2f\n", account.getBalance());
        Map<String, StockHolding> holdings = account.getHoldings();
        if (holdings.isEmpty()) {
            System.out.println("No stock holdings");
        } else {
            holdings.forEach((key, value) -> System.out.println(key + ": "
                    + "\n\tShares: " + value.getShares()
                    + "\n\tPrice: " + String.format("$%.2f", value.getPrice())
                    + "\n\tAverage cost: " + String.format("$%.2f", value.getAverageCost())
                    + "\n\tMarket value: " + String.format("$%.2f", value.getMarketValue())
                    + "\n\tBook cost: " + String.format("$%.2f", value.getBookCost())
                    + "\n\tGain/loss: " + String.format("$%.2f", value.getGainLoss())));
        }
    }

    // EFFECTS: prints new cash balance of account to screen
    private void printNewBalance(Account account) {
        System.out.printf("Your cash balance is now $%.2f\n", account.getBalance());
    }

    // EFFECTS: saves portfolio to file
    private void savePortfolio() {
        try {
            jsonWriter.open();
            jsonWriter.write(portfolio);
            jsonWriter.close();
            System.out.println("Saved " + portfolio.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: loads portfolio from file
    private void loadPortfolio() {
        try {
            portfolio = jsonReader.read();
            System.out.println("Loaded " + portfolio.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}
