# Fancy Bank

The classes of the project have been seperated into to categories. One
is for the data classes, namely the back end. The other for the UI
classes, the front end.

These two parts are connected by two core classes which are `DlgBank`
and `Bank`. `DlgBank` is the root for almost all the interfaces and
`Bank` is the core of the backend. Each of them contains one reference
to the other so they could transport the data between the front and
back end.

# Compile and Run

You need to put the source code in an Eclipse Java Project then run
the main function in `Bank.java`. To start the program, run the main function in `Bank.java` 

To connect to the databse : 
1. download sql jdbc at : http://dev.mysql.com/downloads/connector/j/ , in IntelliJ, add the jdbc from 
file -> project structure -> Libraries -> + 
2. Intall MAMP at http://www.mamp.info/en/downloads/ (do not use pro)
3. Open preferences, configure Apache Port to be 8888 and MySQL Port to 8889 
4. Then click Open Web Start Page button, a start web page will be opened where you can interact with
MySQL in a web UI instead of terminal. When it opens, choose Tools->phpMyAdmin.
5. Under phpMyAdmin, you should be able to see a list of databases. Create a new database called
591database (or whatever name you pref). And use utf8_general_ci as the collation.
6. In `MySQLDBUtil` change the `DB_NAME` to match the database name created in above step and change`PORT_NUM` to 8889

# Clairification

- This application do not provide password for an user, since security
  is not the focus of the assignment.
  
- The way a user to take out loan is to create a loan account. A loan
  account has a negative initial value which is the value the user
  chooses to take out. The user could save or transact to this account
  to pay for the loan, but could not withdraw or transact from
  it. When the value of this account is equal to zero, the user
  could destroy this account which means the loan is payed off.
  
- The manager has an account to store all the fee collected. However,
  except the fee collection log, there is no way to check the
  manager's account.

- The money for the loan is not deducted from the banker's
  account. The money comes from nowhere.
  
- The collateral the usres need to take out loans is their deposit in
  the bank.
  
- The way to pay interest (to saving accounts and loans) is to use a
  button in the manager interface.

- The money earned by the loan will not be added to the manager's account.

# Data Classes

## Bank

This is the interface of the back end. It also defines a lot of
constants which are used in this project. It manages the users and the
accounts which in this assignment would only exist in the memory.

This class offers all the methods the front end need to perform
certain tasks or fetch data they need to display. This is mainly an
utility class, so no abstraction is given. However, it is easy to
extract some methods of it which are essential to a bank, such as
`createUser`, `createAccount`, `saveMoneyToAccount`,
`withdrawMoneyFromAccount`, `transactMoney` and define a interface for
all bank objects. Then add a wrapper class for the abstracted `Bank`
object to offer the front end utility methods.

## User

This class represents the user of our bank. It has a name and an
numeral ID. It also holds all the references of the accounts it owns.

## Account

This is the base class for all account objects. It defines the data
member and methods an account object should have. Now we have
`SavingAccount`, `CheckingAccount` and `LoanAccount` three subclasses.
If we need to add more type of accounts we could just inherit from the
`Account` object.

### CheckingAccount

This is the subclass of `Account`. It implements the `transact`
methods which is now only allowed on the `CheckingAccount` object.

### SavingAccount

This is the subclass of `Account`. Since all of its methods has been
implemented in its base class. It only provide the type information of
the actual object.

### LoanAccount

In this project, I make the loan as a subclass of an `Account`
object. Since most of the operation a loan need is similar to an
account, the inheritance could take advantage of what has been
implemented in the `Account` object. The special behavir of a loan
could be achieved by override the methods of its base class.

## Deposit

This is the class represents all the money a user put in one account.
It could hold different kinds of currency. It is easy to add new
currency for it to hold.

## Money

This class represents the money in the bank. It is a very important
class which is used everywhere in the project. It defines the currency
and amount of the money. It also provides methods for the user to
compare which one is larger or lesser of two `Money` objects. It also
defines the exchange-rate for different currencies.

## Msg

A wrapper class for the Java built-in `String`. The class is used to
pass error message from the back end to the user interface.

## Log

This class is the log keeper. It stores all the log and provides
method to take paricular log. There is a global log for the manager
and a per-user log for each user to check.

# User Interface Classes

## DlgBank

This class is the main frame of the application. It switches from
panels to panels to provide different functionality. It is also an
interface provider for other UI classes. It stores an reference of the
`Bank` object and will call its method to pass and retrive data.

## In-Project Reusable Component

These classes are UI components which appear in more than one place so
I make them reusable classes to provide more efficiency.

### BankPanel

This is the base classes for some other classes. It holds a reference
to the `DlgBank` object to call its mehtods.

### MoneyInputer

Since the user has to input information about the amount and currency
of money almost everywhere, this class contains the text field to
input number and the currency type selection combo box. This could be
added to any container.

### MessageWindow

This class is used to present the error or hint message for the user.

### AccountInfoItem

This class is designed to present all the information of one account
and provide interface for the user to operate with their account.

### AccountInfoPanel

This class presents part of the `AccountInfoItem`. But it is also used
in other places so I make it a single component which could be added
anywhere.

### DlgLog

This is a seperate frame which used to present the content of
log. This could be used both by the manager and the user.

## Spcific Classes

These classes are most developed for a particular task.

### CreateAccountPanel

This class is the interface for the user to create new accounts. By
creating a loan account, a user takes out a loan.

### DlgUser

This class contains the account information of a particular user. It
is viewed by the bank manager.

### ManagerPanel

This class provides the interface for the manager to pay interest,
view log and check up users.

### UserLoginPanel

This class provides the entrance for users. They could create new user
or log in to a old one.

### UserPanel

This is the main panel which provides the users all the operation they
could done.

### WelcomePanel

This is the initial panel of the application. In this panel, the user
of the program could choose to login as an user of the bank or the
bank manager.

# Data Base Classes 
## BaseDBReader 
This is the base class for a data base reader. It initialize a connection with the local database. Right now it has 4 child classes `InfoReader`,`LogReader`,`StockReader`,`UserReader`.
### InfoReader 
Extracts global information about the banking system including interest rate openning_account fee etc.
### LogReader 
Writes and extracts user and global logs 
### UserReader 
Writes, modifies or retrieves account information for a user 
### StockReader 
Writes, modifies or retrieves stock information 
## MySQLDBUtil 
Saves login information for the database 
## MySQLTableCreation 
Creates empty data schemas. Right now we have 4 tables. User table stores all account information associated with each account id. Logs table stores user logs and global logs. Stocks save stock id and its price. Info table stores global information for the bank. 
