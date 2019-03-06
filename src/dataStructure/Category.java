package dataStructure;

import javafx.scene.paint.Color;

public class Category {
    public static final int MAXICONCOUNT = 12;
    private String name;
    private Color color;
    private int iconId;
    private int ID;

    public Category(int ID, String name, Color color, int iconId) {
        this.name = name;
        this.color = color;
        this.iconId = iconId;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getIconId() {
        return iconId;
    }

    public int getID() {
        return ID;
    }
}
