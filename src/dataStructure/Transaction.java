package dataStructure;

import java.util.Date;
import java.util.GregorianCalendar;

public class Transaction {
    private int amount;
    private Category category;
    private String note;
    public static String defaultTime = "00:00";
    private GregorianCalendar calendar;

    public Transaction(int amount, Category category,  long timeStamp, String note) {
        this.amount = amount;
        this.category = category;
        this.note = note;
        calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
    }

    public Transaction(int amount, Category category, long timeStamp) {
        this.amount = amount;
        this.category = category;

        note = "";
        calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getDate() {
        return calendar.getTimeInMillis();
    }

    public void setDate(long timeStamp) {
        calendar.setTimeInMillis(timeStamp);
    }

    public GregorianCalendar getCalendar(){
        return calendar;
    }
    public void setCalendar(GregorianCalendar calendar){
        this.calendar = calendar;
    }
}
