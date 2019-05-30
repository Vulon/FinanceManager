package dataStructure;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;

public class Budget implements Serializable {
    public double currentLimit;
    public HashMap<Pair<Integer, Integer>, Double> history; //Month-year key -> limit for that month
    public int defaultValue;

    private static Budget link;
    public static Budget getInstance(){
        if(link == null){
            link = new Budget();
        }
        return link;
    }

    private Budget(){
        history = new HashMap<>();
        defaultValue = 20000;
    }
    public void load(){

    }

}
