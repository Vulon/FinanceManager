package Debug;

import dataStructure.Category;
import dataStructure.Transaction;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MyRandomGenerator {
    public static Transaction generateTransaction(ObservableList<Category> categories){
        int amount = ThreadLocalRandom.current().nextInt(10, 401);
        int category = ThreadLocalRandom.current().nextInt(0, categories.size());
        Date date = new Date();
        String note = "Some default note here, should test it later";
        return new Transaction(amount, categories.get(category), date, note);
    }
    private static final String [] baseNames = {"Tools", "Travel", "bike", "Parking", "Music", "Phone",
            "Medicine", "Fast Food", "Art", "Sport", "Car", "Metro", "Food"};
    public static void fillBaseCategories(ObservableList<Category> categories){
        Random random = new Random();
        for(int i = 1; i < Category.MAXICONCOUNT; i++){

            Color color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            Category category = new Category(i, baseNames[i], color, i);
            categories.add(category);
        }
    }
}
