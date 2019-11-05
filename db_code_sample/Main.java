import db.StockReader;
import db.UserReader;
import stock.Stock;
import User.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        // Stock reader test
        StockReader sd = new StockReader();
        for (int i = 1; i < 10; i++) {
            // call this line if want to add a new stock to a data base
            Stock s = new Stock.Builder().setId(i).setName("test" + i).setPrice(100.0 + i).build();
            sd.insert(s);
            System.out.println(sd.getAllIds());
        }
        // delete stock function
        sd.delete(2);
        // get all available stock ids
        System.out.println(sd.getAllIds());

        // get a specific stock id

        System.out.println(sd.getById(3));
        // get the price of that stock id
        System.out.println(sd.getById(3).getPrice());

        // User reader test
        UserReader ud = new UserReader();
        // add user
        // sample checking account insertion , put -1 for stock id for both checking and saving account
        User u1 = new User.Builder().setName("John").setAcc_id(0000).setAcc_type(0).setCurr_type(0).setMoney(100.0).setStock_id(-1).setStock_amount(-1).setInterest(0.15).build();
        ud.insert(u1);
        // sample saving account insertion
        User u2 = new User.Builder().setName("John").setAcc_id(0001).setAcc_type(1).setCurr_type(0).setMoney(5000.0).setStock_id(-1).setStock_amount(-1).setInterest(0.15).build();
        ud.insert(u2);
        //sample stock account insertion
        User u3 = new User.Builder().setName("John").setAcc_id(0002).setAcc_type(2).setCurr_type(0).setMoney(10000.0).setStock_id(1).setStock_amount(125).setInterest(-1).build();
        ud.insert(u3);
        User u4 = new User.Builder().setName("John").setAcc_id(0002).setAcc_type(2).setCurr_type(0).setMoney(10000.0).setStock_id(2).setStock_amount(35).setInterest(-1).build();
        ud.insert(u4);


        // print all accounts and asscociated info with this user
        ArrayList<User> user_info = ud.getByName("John");
        for (int i = 0; i < user_info.size(); i++) {
            System.out.println(user_info.get(i));
        }

        // Test for API
        // 1. get checking account by name
        double checking_amount = ud.getCheckingByName("John");
        System.out.println("checking amcount for John: " + checking_amount );
        // 2. get saving account by name
        double saving_amount = ud.getSavingingByName("John");
        System.out.println("saving amcount for John: " + saving_amount );
        // 3. get all stock ids by name
        ArrayList<Integer> stock_ids = ud.getStockIdByName("John");
        System.out.println(" stock ids  for John: " + stock_ids.toString() );

        // 4. get all user_name
        ArrayList<String> all_user_names = ud.getAllUser();
        System.out.println(" All user names: " + all_user_names.toString() );
        // 5. get all account ids for a user
        ArrayList<Integer> Accounts_id = ud.getAccountId("John");
        System.out.println(" All accounts that John has : " + Accounts_id.toString() );
        // 6. get account contents by id
        ArrayList<User> Accounts_id_user = ud.getByAccountId(2);
        System.out.println(" All contents associcate with account id 2 : " + Accounts_id_user.toString() );

        // 7. get all stock id
        System.out.println(sd.getAllIds());
        // 8. get stock by id
        System.out.println(sd.getById(3));


        sd.closeConnection();
    }
}
