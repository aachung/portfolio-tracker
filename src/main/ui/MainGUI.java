package ui;

// Runs GUI
public class MainGUI {
    // EFFECTS: runs GUI from the portfolio screen
    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen();
        try {
            Thread.sleep(5000);
            splashScreen.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new PortfolioScreen();
    }
}
