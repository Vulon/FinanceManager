package dataStructure;

import java.util.Date;
import java.util.GregorianCalendar;

public class Transaction {
    private int amount;
    private Category category;
    private String note;
    private Date date;
    public static GregorianCalendar calendar;

    public Transaction(int amount, Category category,  Date date, String note) {
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.date = date;
        if(calendar == null){
            calendar = new GregorianCalendar();
        }
    }

    public Transaction(int amount, Category category, Date date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        note = "";
        if(calendar == null){
            calendar = new GregorianCalendar();
        }
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
