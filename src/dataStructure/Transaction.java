package dataStructure;

import java.util.GregorianCalendar;

public class Transaction {
    private double amount;     /* changed from int, has to be double*/
    private Category category;
    private String note;
    private int id = -1;
    public static String defaultTime = "00:00";
    private GregorianCalendar calendar;

    public Transaction(int id, double amount, Category category,  long timeStamp, String note) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.note = note;
        calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
    }

    public Transaction(int id, double amount, Category category, long timeStamp) {
        this.amount = amount;
        this.category = category;
        this.id = id;

        note = "";
        calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GregorianCalendar getCalendar(){
        return calendar;
    }
    public void setCalendar(GregorianCalendar calendar){
        this.calendar = calendar;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        Transaction o = (Transaction)obj;
        if(id != o.id){
            return false;
        }
        if (getDate() != o.getDate()){
            return false;
        }
        if (o.amount != amount){
            return false;
        }
        if(category.getID() != o.getCategory().getID()){
            return  false;
        }if (note != o.note){
            return false;
        }
        return true;
    }
}
