package dataStructure;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Category {
    public static final int MAXICONCOUNT = 32;
    private String name;
    private String color; //#ffffff
    private int iconId;
    private int ID;
    private boolean isIncome;

    public Category(int ID, String name, String color, int iconId, boolean isIncome) {
        this.name = name;
        this.color = color;
        this.iconId = iconId;
        this.ID = ID;
        this.isIncome = isIncome;

    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getIconId() {
        return iconId;
    }

    public int getID() {
        return ID;
    }

    public boolean checkIsIncome(){return isIncome;}
}
