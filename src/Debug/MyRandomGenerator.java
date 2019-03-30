package Debug;

import dataStructure.Category;
import dataStructure.Transaction;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MyRandomGenerator {
    public static Transaction generateTransaction(ObservableList<Category> categories){
        int amount = ThreadLocalRandom.current().nextInt(10, 401);
        int category = ThreadLocalRandom.current().nextInt(0, categories.size());
        long delta = ThreadLocalRandom.current().nextLong(100, 5000000000l);
        Date date = new Date();
        date.setTime(date.getTime() - delta);

        String note = "Some default note here, should test it later";
        return new Transaction(amount, categories.get(category), date.getTime(), note);
    }
    private static final String [] baseNames = {"Tools", "Travel", "bike", "Parking", "Music", "Phone",
            "Medicine", "Fast Food", "Art", "Sport", "Car", "Metro", "Food"};
    public static void fillBaseCategories(ObservableList<Category> categories){
        Random random = new Random();
        for(int i = 1; i < Category.MAXICONCOUNT; i++){

            Color color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            String style = "#" + color.toString().substring(2,10);

            Category category = new Category(i, baseNames[random.nextInt(baseNames.length - 1)], style, i, random.nextBoolean());
            categories.add(category);
        }
    }
}
