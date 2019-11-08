package DBUser;

public class DBUser {

    public static class Builder {
        //private int user_id;
        private String name;
        private int account_id;
        private int account_type;
        private int currency_type;
        private double money;
        private int stock_id;
        private int stock_amount;
        private double interest;
        /*
        public Builder setId(int id) {
            this.user_id = id;
            return this;
        }

        */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setAcc_id(int account_id) {
            this.account_id = account_id;
            return this;
        }
        public Builder setAcc_type(int account_type) {
            this.account_type = account_type;
            return this;
        }
        public Builder setCurr_type(int currency_type) {
            this.currency_type = currency_type;
            return this;
        }
        public Builder setMoney(double money) {
            this.money = money;
            return this;
        }
        public Builder setStock_id(int id) {
            this.stock_id = id;
            return this;
        }
        public Builder setStock_amount(int amount) {
            this.stock_amount = amount;
            return this;
        }
        public Builder setInterest(double interest) {
            this.interest = interest;
            return this;
        }


        public DBUser build() {
            return new DBUser(name, account_id, account_type, currency_type, money, stock_id, stock_amount,interest);
        }
    }
    //private int user_id;
    private String name;
    private int account_id;
    private int account_type;
    private int currency_type;
    private double money;
    private int stock_id;
    private int stock_amount;
    private double interest;
    public DBUser (String name, int account_id, int account_type, int currency_type, double money, int stock_id, int stock_amount, double interest   ) {
        //this.user_id = id;
        this.name = name;
        this.account_id = account_id;
        this.account_type = account_type;
        this.currency_type = currency_type;
        this.money = money;
        this.stock_id = stock_id;
        this.stock_amount = stock_amount;
        this.interest = interest;
    }
    /*
    public int getId() {
        return user_id;
    }


    public void setId(int id) {
        this.user_id = id;
    }
    */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_id) {
        this.account_type = account_type;
    }

    public int getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(int currency_type) {
        this.currency_type = currency_type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int id) {
        this.stock_id = id;
    }

    public int getStock_amount() {
        return stock_amount;
    }

    public void setStock_amount(int id) {
        this.stock_id = id;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }




    @Override
    public String toString() {
        return String.format(" Name: " + this.getName() + " Account_id: " + this.getAccount_id()
                             + " Account_type" + this.getAccount_type() + " Currency_type" + this.getCurrency_type() + " Money:" + this.getMoney()
                             + " Stock_ id : " + this.getStock_id() + " Stock_amount: " + this.getStock_amount()
                             + "Interest_rate : " + this.getInterest());
    }
}
