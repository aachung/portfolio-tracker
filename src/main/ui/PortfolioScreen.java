package ui;

import model.Account;
import model.Event;
import model.EventLog;
import model.Portfolio;
import persistence.JsonReader;
import persistence.JsonWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

// Represents the portfolio screen when user opens app
// source: https://www.youtube.com/watch?v=5o3fMLPY7qY&ab_channel=AlexLee
public class PortfolioScreen extends JFrame implements ActionListener {
    private Portfolio portfolio;
    private Account account;
    private static JFrame frame;
    private static JPanel panel;
    private static JPanel actionsPanel;
    private static JPanel accountsPanel;
    private static JMenuItem save;
    private static JMenuItem load;
    private static JMenuItem quit;
    private static JButton createAccount;
    private static JButton deleteAccount;
    private static JButton refresh;
    private static final String JSON_STORE = "./data/portfolio.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs a portfolio screen and loads portfolio from file
    public PortfolioScreen() {
        portfolio = new Portfolio("Annie's Portfolio");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        frame = new JFrame();
        setFrame();

        panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(portfolio.getName()));
        panel.setLayout(new GridLayout(2, 0));

        actionsPanel = new JPanel();
        addButtons();

        accountsPanel = new JPanel();
        loadPortfolio();
        loadAccounts();

        panel.add(actionsPanel);
        panel.add(accountsPanel);

        frame.add(panel);
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame, "Loaded " + portfolio.getName() + " from " + JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: sets visible aspects of frame
    private void setFrame() {
        frame.setSize(450, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getSize().width) / 2;
        int y = (screenSize.height - frame.getSize().height) / 2;
        frame.setLocation(x,y);

        String sep = System.getProperty("file.separator");
        Image icon = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + sep
                + "data" + sep + "icon.png");
        frame.setIconImage(icon);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                for (Event e: EventLog.getInstance()) {
                    System.out.println(e.getDate() + " - " + e.getDescription());
                }
                System.exit(0);
            }
        });
        frame.setTitle("My Portfolio App");

        addMenu();
    }

    // MODIFIES: this
    // EFFECTS: adds menu to screen
    // source: https://www.youtube.com/watch?v=RndW8n98IWE&ab_channel=ProgrammingGuru
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        quit = new JMenuItem("Quit");

        menu.add(save);
        menu.add(load);
        menu.add(quit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        save.addActionListener(this);
        load.addActionListener(this);
        quit.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to screen
    private void addButtons() {
        createAccount = new JButton("Create New Account");
        deleteAccount = new JButton("Delete Account");
        refresh = new JButton("Refresh");

        createAccount.addActionListener(this);
        deleteAccount.addActionListener(this);
        refresh.addActionListener(this);

        actionsPanel.add(createAccount);
        actionsPanel.add(deleteAccount);
        actionsPanel.add(refresh);
    }

    // EFFECTS: processes action based on button pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == save) {
            savePortfolio();
        }
        if (source == load) {
            loadPortfolio();
            loadAccounts();
        }
        if (source == quit) {
            System.exit(0);
        }
        if (source == createAccount) {
            createAccount();
            loadAccounts();
        }
        if (source == deleteAccount) {
            deleteAccount();
            loadAccounts();
        }
        if (source == refresh) {
            loadAccounts();
        }
    }

    // MODIFIES: this, portfolio
    // EFFECTS: creates an account based on user input and displays it on screen
    private void createAccount() {
        JPanel createPanel = new JPanel();
        createPanel.add(new JLabel("Account Name"));
        JTextField accountName = new JTextField(10);
        createPanel.add(accountName);

        createPanel.add(new JLabel("Account Balance"));
        JTextField accountBalance = new JTextField(10);
        createPanel.add(accountBalance);

        int input = JOptionPane.showConfirmDialog(null, createPanel, "Create a new account",
                JOptionPane.OK_CANCEL_OPTION);

        if (input == JOptionPane.OK_OPTION) {
            String getName = accountName.getText();
            double getBalance = Double.parseDouble(accountBalance.getText());
            portfolio.addAccount(new Account(getName, getBalance));
        }
    }

    // MODIFIES: this, portfolio
    // EFFECTS: deletes an account based on user input and removes it from screen
    private void deleteAccount() {
        String input = JOptionPane.showInputDialog("Account Number");
        if (input != null) {
            int accountNumber = Integer.parseInt(input);
            portfolio.removeAccount(accountNumber);
        } else {
            frame.repaint();
        }
    }

    // MODIFIES: this
    // EFFECTS: loads portfolio accounts on screen
    private void loadAccounts() {
        accountsPanel.removeAll();
        accountsPanel.setLayout(new GridLayout(0, 1));
        for (Map.Entry<Integer, Account> entry : portfolio.getAccounts().entrySet()) {
            int accountNumber = entry.getKey();
            String accountName = entry.getValue().getName();
            double accountBalance = entry.getValue().getBalance();

            JButton button = new JButton(accountNumber + ". " + accountName + " "
                    + String.format("$%.2f", accountBalance));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    account = portfolio.retrieveAccount(accountNumber);
                    new AccountScreen(account);
                }
            });
            accountsPanel.add(button);
        }
        panel.add(accountsPanel);
        frame.revalidate();
        frame.repaint();
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
