package ui;

import model.Account;
import model.StockHolding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

// Represents the account screen of a selected account
public class AccountScreen extends JFrame implements ActionListener {
    private Account account;
    private static JFrame frame;
    private static JPanel panel;
    private static JPanel actionsPanel;
    private static JPanel holdingsPanel;
    private static JPanel buySellPanel;
    private static JButton deposit;
    private static JButton withdraw;
    private static JButton buy;
    private static JButton sell;
    private static JTextField symbol = new JTextField(5);
    private static JTextField shares = new JTextField(5);
    private static JTextField price = new JTextField(5);

    // EFFECTS: constructs an account screen for a given account
    public AccountScreen(Account account) {
        this.account = account;
        frame = new JFrame();
        setFrame();

        panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(account.getName()));
        panel.setLayout(new GridLayout(2, 0));

        actionsPanel = new JPanel();
        addButtons();

        holdingsPanel = new JPanel();
        addHoldings();

        frame.add(panel);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets visible aspects of frame
    private void setFrame() {
        frame.setSize(500, 250);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getSize().width) / 2;
        int y = (screenSize.height - frame.getSize().height) / 2;
        frame.setLocation(x,y);

        String sep = System.getProperty("file.separator");
        Image icon = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + sep
                + "data" + sep + "icon.png");
        frame.setIconImage(icon);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("My Account");
    }

    // MODIFIES: this
    // EFFECTS: adds buttons for account actions
    private void addButtons() {
        deposit = new JButton("Deposit");
        deposit.setBounds(10, 80, 80, 25);

        withdraw = new JButton("Withdraw");
        withdraw.setBounds(40, 80, 80, 25);

        buy = new JButton("Buy");
        buy.setBounds(70, 80, 80, 25);

        sell = new JButton("Sell");
        sell.setBounds(100, 80, 80, 25);

        actionsPanel.add(deposit);
        actionsPanel.add(withdraw);
        actionsPanel.add(buy);
        actionsPanel.add(sell);

        deposit.addActionListener(this);
        withdraw.addActionListener(this);
        buy.addActionListener(this);
        sell.addActionListener(this);

        panel.add(actionsPanel);
    }

    // MODIFIES: this
    // EFFECTS: displays account holdings
    private void addHoldings() {
        holdingsPanel.setLayout(new GridLayout(5, 2));
        JLabel balance = new JLabel("Cash Balance: " + String.format("$%.2f", account.getBalance()));
        holdingsPanel.add(balance);
        for (Map.Entry<String, StockHolding> entry : account.getHoldings().entrySet()) {
            String symbol = entry.getKey();
            int shares = entry.getValue().getShares();

            JLabel label = new JLabel(symbol + ": " + shares + " shares");
            holdingsPanel.add(label);
        }
        panel.add(holdingsPanel);
    }

    // EFFECTS: processes action based on button pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == deposit) {
            doDeposit();
        }
        if (source == withdraw) {
            doWithdrawal();
        }
        if (source == buy) {
            doBuy();
        }
        if (source == sell) {
            doSell();
        }
    }

    // MODIFIES: this, account
    // EFFECTS: conducts a deposit transaction
    private void doDeposit() {
        String input = JOptionPane.showInputDialog("Enter amount to deposit");
        if (input != null) {
            double amount = Double.parseDouble(input);
            account.deposit(amount);
            holdingsPanel.removeAll();
            addHoldings();
            frame.revalidate();
            frame.repaint();
        } else {
            frame.repaint();
        }
    }

    // MODIFIES: this, account
    // EFFECTS: conducts a withdrawal transaction
    private void doWithdrawal() {
        String input = JOptionPane.showInputDialog("Enter amount to withdraw");
        if (input != null) {
            double amount = Double.parseDouble(input);
            account.withdraw(amount);
            holdingsPanel.removeAll();
            addHoldings();
            frame.revalidate();
            frame.repaint();
        } else {
            frame.repaint();
        }

    }

    // MODIFIES: this, account
    // EFFECTS: conducts a buy transaction
    // source: https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
    private void doBuy() {
        buySellPanel = new JPanel();
        buySellPanel.add(new JLabel("Stock Symbol"));
        buySellPanel.add(symbol);
        buySellPanel.add(new JLabel("Shares"));
        buySellPanel.add(shares);
        buySellPanel.add(new JLabel("Price"));
        buySellPanel.add(price);

        int input = JOptionPane.showConfirmDialog(null, buySellPanel, "Buy stock", JOptionPane.OK_CANCEL_OPTION);

        if (input == JOptionPane.OK_OPTION) {
            account.buyStock(symbol.getText(), Integer.parseInt(shares.getText()),
                    Double.parseDouble(price.getText()));

            holdingsPanel.removeAll();
            addHoldings();
            frame.revalidate();
            frame.repaint();
        }
    }

    // MODIFIES: this, account
    // EFFECTS: conducts a sell transaction
    // source: https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
    private void doSell() {
        buySellPanel = new JPanel();
        buySellPanel.add(new JLabel("Stock Symbol"));
        buySellPanel.add(symbol);
        buySellPanel.add(new JLabel("Shares"));
        buySellPanel.add(shares);
        buySellPanel.add(new JLabel("Price"));
        buySellPanel.add(price);

        int input = JOptionPane.showConfirmDialog(null, buySellPanel, "Sell stock", JOptionPane.OK_CANCEL_OPTION);

        if (input == JOptionPane.OK_OPTION) {
            account.sellStock(symbol.getText(), Integer.parseInt(shares.getText()),
                    Double.parseDouble(price.getText()));

            holdingsPanel.removeAll();
            addHoldings();
            frame.revalidate();
            frame.repaint();
        }
    }
}
