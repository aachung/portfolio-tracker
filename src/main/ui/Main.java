package ui;

import java.io.FileNotFoundException;

// Runs portfolio app
public class Main {
    // EFFECTS: runs portfolio app
    // throws FileNotFoundException if user tries to load non-existing file
    public static void main(String[] args) {
        try {
            new PortfolioApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
