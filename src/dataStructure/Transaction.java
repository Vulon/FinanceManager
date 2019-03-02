package dataStructure;

import java.util.Date;
import java.util.GregorianCalendar;

public class Transaction {
    private int amount;
    private int categoryId;
    private String note;
    private Date date;
    public static GregorianCalendar calendar;

    public Transaction(int amount, int categoryId,  Date date, String note) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.note = note;
        this.date = date;
        if(calendar == null){
            calendar = new GregorianCalendar();
        }
    }

    public Transaction(int amount, int categoryId, Date date) {
        this.amount = amount;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
