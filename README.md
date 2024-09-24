# My Portfolio Tracker

## Project Overview

My project is a **portfolio tracking application**.

*What will the application do?*

My application will allow users track their stock investments across different accounts in their portfolio. 
In particular, users will be able to record purchases and sales of stocks, and view the holdings in each account.

*Who will use it?*

Anyone interested in managing their own investment accounts can use this application! I think a portfolio
tracker can be really useful tool, especially for anyone who wants to monitor their own finances. On a small 
scale, individuals looking to keep track of their investment accounts could use a portfolio tracker. On a 
larger scale, portfolio managers managing client assets could also use a portfolio tracker. 

*Why is this project of interest to you?*

I worked in the finance industry previously and used this type of application to monitor client accounts. 
I am curious about the implementation of this type of program, and I am interested in the ways in which I 
can improve its usability.

## User Stories
- As a user, I want to be able to record the purchase of a stock in an account.
- As a user, I want to be able to record the sale of a stock in an account.
- As a user, I want to be able to view a list of holdings in an account.
- As a user, I want to be able to make cash deposits and withdrawals in an account.
- As a user, I want to be able to save my accounts to file.
- As a user, I want to be able to load my accounts from file.

## Instructions for Grader
1. Run the application from ***MainGUI*** within the ***ui*** package. A custom image will appear while the app loads. 
Then a popup will appear, indicating data has been loaded to the portfolio. Click ***OK*** to reveal the portfolio 
screen, which has a menu, several buttons and a list of loaded accounts.
2. You can add an account by clicking ***Create New Account***. Enter an account name and balance (just decimal numbers 
without a dollar sign or comma), then click ***OK***. Your new account should now be added to the list.
3. You can delete an account by clicking ***Delete Account***. Enter the account number of the account you wish to 
delete, then click ***OK***. Your account should now be removed from the list.
4. You can select an account by clicking on it. This will bring up an account screen where you can view holdings and 
conduct various transactions. You can play around with these.
5. If you return to the portfolio screen from the account screen, click ***Refresh*** to update the accounts.
6. You can locate the visual components of the app in the ***data*** folder. There is an image used for the splash 
screen and an icon used in the top left of the portfolio/account screen windows. The icon also appears in the taskbar.
7. You can save the state of the app by selecting ***File > Save*** in the menu.
8. You can reload the state of the application by selecting ***File > Load*** in the menu.

## Phase 4: Task 2
Thu Mar 30 16:09:33 PDT 2023 - "TFSA" was added to the portfolio  
Thu Mar 30 16:09:33 PDT 2023 - "RRSP" was added to the portfolio  
Thu Mar 30 16:09:33 PDT 2023 - "Cash" was added to the portfolio  
Thu Mar 30 16:10:14 PDT 2023 - "Savings" was added to the portfolio  
Thu Mar 30 16:10:20 PDT 2023 - "RRSP" was removed from the portfolio  
Thu Mar 30 16:10:28 PDT 2023 - "RESP" was added to the portfolio  

## Phase 4: Task 3
If I had more time to work on this project, I would probably refactor the StockHolding, Account and Portfolio classes. 
These three classes are highly coupled and dependent on one another. For example, future changes to StockHolding may 
require substantial changes in Account; changes to Account may require changes in Portfolio. To reduce coupling, it may 
be beneficial to refactor the code using abstraction. That way the Account and Portfolio classes may no longer depend 
directly on the implementation details of the Account and StockHolding classes, respectively.

Additionally, it may be beneficial to employ the Observer design pattern since the Portfolio class requires updates 
when there are changes in the Account class. By utilizing the Observer pattern, the Observer (Portfolio) will be 
notified of changes to the Subject (Account). Similarly, this relationship exists between the Account and StockHolding 
classes. The Observer pattern may be especially helpful if the program were expanded to include multiple portfolios for 
different clients. In this case, maybe a company's stock symbol changes; then all the Observers (portfolios/accounts 
that hold that stock) will need to be notified of the change.