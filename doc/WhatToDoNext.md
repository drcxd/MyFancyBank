# Current Situation

Basically, most of the new functionalities about stock transaction
have been implemented. The application now is not connected with a
database. So what is left to do is to connect the database and the
application. There might also be some bug to fix, some additional
feature you would like to add. 

# Preparation

## Set the Environment

First you need to install everything needed to integrate database into
our current application.

## Read the Source Code

For the application side, please try to follow how `Data.User` and
`Account` objects are created and stored. Because this is crucial in
the process of reading database data and recreate all these objects.

Then try to follow when the data of the application is modified, this
is where writing to database should occur.

# Load Database

When the application starts up, the data stored in the database need
to be fetched. There are two functions, namely `loadAllUser` and
`loadAllStock`, left unimplemented which are responsible to load the
data.

## `LoadAllUser`

In this function, the following things need to be done.

1. Get all the user data from the data base and recreate the
   `Data.User` object. All the `User` ojbects are held in `name2User`
   in `Bank` class. There is a member variable `globalUserID` which
   should be maintained during this process. After all the user
   information have been loaded, this variable should be one larger
   than the largest existing user ID. However, since the number id of
   user is not actually used, this could be ignored.

2. For each user, the `Account` objects possessed by the user need to
   be recreated. Each user has a `HashMap` to hold accounts. All the
   accounts must also be held in the `id2Account`, `id2StockAccount`
   and `id2MoneyAccount` member variables in the `Bank` class. The
   member `globalAccountID` must be maintained during this
   process. After all the account information have been loaded, this
   variable should be one larger than the largest existing account ID.
   
## `LoadAllStock`

In this function, all the stock information should be fetched and the
`Stock` objects should be placed in `id2Stock` memeber in `Bank` class.

# Write to Database

When some data changed, for example, a new user is created, a new
account is created, a transaction happened, then it need to be stored
in the database. This need to be done in almost everywhere in this
application.

# Other Improvement

Some feature is not implemented yet.

1. Bank manager could not see the total earnings.

2. Log system may not be complete now, since the operation of
   adding/removing/buying/selling stock is not logged.
   
...