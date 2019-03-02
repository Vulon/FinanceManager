package Debug;

import dataStructure.Transaction;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class MyRandomGenerator {
    public static Transaction generateTransaction(){
        int amount = ThreadLocalRandom.current().nextInt(10, 401);
        int category = ThreadLocalRandom.current().nextInt(0, 4000);
        Date date = new Date();
        String note = "Some default note here, should test it later";
        return new Transaction(amount, category, date, note);
    }
}
