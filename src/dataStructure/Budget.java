package dataStructure;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;

public class Budget implements Serializable {
    private static Budget link;
    public static Budget getInstance(){
        if(link == null){
            link = new Budget();
        }
        return link;
    }
    public int currentLimit;
    public HashMap<Pair<Integer, Integer>, Integer> history;
    public int defaultValue;
    private Budget(){
        history = new HashMap<>();
        defaultValue = 20000;
    }
    public void load(){

    }

}
