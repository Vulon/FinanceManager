package utills;

import Debug.MyRandomGenerator;
import dataStructure.Category;
import dataStructure.Month;
import dataStructure.Transaction;
import dataStructure.User;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatabaseManager {
    private static String BUDGET = "BUDGET";
    private static String CATEGORY = "CATEGORY";
    private static String TRANSACTION = "TRANSACTION";
    private static String USER_DATA = "USER_DATA";
    private static DatabaseManager link = null;
    private static final String DRIVER_NAME = "org.h2.Driver";
    private static final String DATABASE_URL = "jdbc:h2:file:~/app_data";


    public static DatabaseManager getInstance(){
        if(link == null){
            link = new DatabaseManager();
        }
        return link;
    }
    private Connection connection;
    private DatabaseManager(){
        try{
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(DATABASE_URL, "app", "2546");
            createUserTable();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void createUserTable(){
        try{
            Statement userStatement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + USER_DATA + "(EMAIL VARCHAR(30) PRIMARY KEY, PASSWORD VARCHAR(30)" +
                    ", TOKEN VARCHAR(50), REFRESH VARCHAR(50))";
            userStatement.executeUpdate(sql);
            userStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void createTables(String userEmail){ //MUST CALL BEFORE USE DATABASE
        try{
            Statement catStatement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + CATEGORY + "(ID INT PRIMARY KEY," +
                    " NAME VARCHAR(20), COLOR VARCHAR(7), ICONID SMALLINT, IS_INCOME BIT)";
            catStatement.executeUpdate(sql);
            Statement trStatement = connection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS " + TRANSACTION + " (ID INT PRIMARY KEY, AMOUNT DOUBLE, TIMESTAMP LONG, " +
                    "CATEGORY_ID INT, NOTE VARCHAR(40))";
            trStatement.executeUpdate(sql);
            Statement budStatement = connection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS "+ BUDGET + " (MONTH_NUM SMALLINT, YEAR_NUM SMALLINT, AMOUNT DOUBLE, PRIMARY KEY(MONTH_NUM, YEAR_NUM))";
            budStatement.executeUpdate(sql);
            catStatement.close();
            trStatement.close();
            budStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void disconnect(){
        try {
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void insertBaseCategories(){
        try {
            XMLParser parser = new XMLParser();
            ArrayList<Category> categories = parser.getNesIncomes();
            for (int i = 0; i < categories.size(); i++){
                String sql = "SELECT * FROM " + CATEGORY + " WHERE id =" + Integer.toString(categories.get(i).getID()) + ";";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                if (!resultSet.next()){
                    statement.close();
                    Statement insertStatement = connection.createStatement();
                    Category category = categories.get(i);
                    sql = "INSERT INTO " + CATEGORY + " VALUES (" + category.getID() +", '" + category.getName() + "', '"
                    + category.getColor() + "', " + category.getIconId() + ", " + "1)";
                    System.out.println(sql);
                    insertStatement.executeUpdate(sql);
                    insertStatement.close();
                }
            }
            categories = parser.getNesExpenses();
            for (int i = 0; i < categories.size(); i++){
                String sql = "SELECT * FROM " + CATEGORY+ "  WHERE id =" + Integer.toString(categories.get(i).getID()) + ";";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                if (!resultSet.next()){
                    statement.close();
                    Statement insertStatement = connection.createStatement();
                    Category category = categories.get(i);
                    sql = "INSERT INTO " +  CATEGORY + " VALUES (" + category.getID() +", '" + category.getName() + "', '"
                            + category.getColor() + "', " + category.getIconId() + ", " + "0)";
                    System.out.println(sql);
                    insertStatement.executeUpdate(sql);
                    insertStatement.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Category> getIncomeCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + CATEGORY + " WHERE IS_INCOME = 1";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String color = resultSet.getString("color");
                int iconId = resultSet.getInt("iconid");
                boolean is_income = resultSet.getBoolean("is_income");
                categories.add(new Category(id, name, color, iconId, is_income));
            }

            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return categories;
    }
    public ArrayList<Category> getExpenseCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + CATEGORY + " WHERE IS_INCOME = 0";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String color = resultSet.getString("color");
                int iconId = resultSet.getInt("iconid");
                boolean is_income = resultSet.getBoolean("is_income");
                categories.add(new Category(id, name, color, iconId, is_income));
                System.out.println("found " + name + " category");
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return categories;
    }
    public void insertTestTransactions(ObservableList<Category> categories){
        for (int i = 0; i < 5; i++){
            Transaction transaction = MyRandomGenerator.generateTransaction( generateUniqueTransactionID(), categories);
            insertTransaction(transaction);
        }

    }
    public void deleteDebugTransactions(){
        try{
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM " + TRANSACTION + " WHERE NOTE LIKE 'DEBUG'";
            statement.executeUpdate(sql);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Transaction> getThisMonthTransactions(int year, int month){
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            Statement selectStatement = connection.createStatement();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);

            long timestart = calendar.getTimeInMillis() / 1000;
            calendar.add(Calendar.MONTH, 1);
            long timeend = calendar.getTimeInMillis() / 1000;
            String sql = "SELECT * FROM " + TRANSACTION + " WHERE TIMESTAMP >= " + timestart + " AND TIMESTAMP <= " + timeend;
            ResultSet resultSet = selectStatement.executeQuery(sql);
            while (resultSet.next()){
                //amount, category, timestamp, note
                try{
                    int id = resultSet.getInt("id");
                    double amount = resultSet.getDouble("amount");
                    long date = resultSet.getLong("timestamp");
                    int category_id = resultSet.getInt("category_id");
                    String note = resultSet.getString("note");
                    calendar.setTimeInMillis(date * 1000);
                    Category category = getCategoryById(category_id);
                    transactions.add(new Transaction(id, amount, category, calendar.getTimeInMillis(), note));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            selectStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return transactions;
    }
    public Category getCategoryById(int id){
        try {
            Statement selectStatement = connection.createStatement();
            String sql = "SELECT * FROM " + CATEGORY + " WHERE  ID = "+ id;
            ResultSet resultSet = selectStatement.executeQuery(sql);
            if(resultSet.next()){
                String name = resultSet.getString("name");
                String color = resultSet.getString("color");
                int iconId = resultSet.getInt("iconid");
                boolean is_income = resultSet.getBoolean("is_income");
                Category category = new Category(id, name, color, iconId, is_income);
                return category;
            }
            selectStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            Statement selectStatement = connection.createStatement();
            String sql = "SELECT * FROM " + TRANSACTION;
            ResultSet resultSet = selectStatement.executeQuery(sql);
            while (resultSet.next()){
                //amount, category, timestamp, note
                try{
                    int id = resultSet.getInt("id");
                    double amount = resultSet.getDouble("amount");
                    long date = resultSet.getLong("timestamp") * 1000;
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis(date);
                    int category_id = resultSet.getInt("category_id");
                    String note = resultSet.getString("note");
                    Category category = getCategoryById(category_id);
                    transactions.add(new Transaction(id, amount, category, date, note));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            selectStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return transactions;
    }
    public void insertTransaction(Transaction transaction){
        try {
            Statement insertStatement = connection.createStatement();
            String sql = "INSERT INTO " + TRANSACTION + " VALUES(" + transaction.getId()
                    + ", " + transaction.getAmount() + ", " + transaction.getDate() / 1000
                    + ", " + transaction.getCategory().getID() + ", '" + transaction.getNote() +"')";
            System.out.println(sql);
            insertStatement.executeUpdate(sql);
            insertStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int generateUniqueTransactionID(){
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT ID FROM " + TRANSACTION;
            boolean flag = true;
            int unique = 0;
            ResultSet resultSet = statement.executeQuery(sql);
            while (flag){
                unique ++;
                flag = false;
                resultSet.beforeFirst();
                while(resultSet.next()){
                    int id = resultSet.getInt(1);
                    if (id == unique){
                        flag = true;
                        break;
                    }
                }
            }
            statement.close();
            System.out.println("FOUND UNIQUE " + unique);
            return unique;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("NOT FOUND UNIQUE");
        return -1;
    }
    public ArrayList<Month> getThisYearsActiveMonths(int year){
        ArrayList<Month> months = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT TIMESTAMP FROM " + TRANSACTION;
            ResultSet resultSet = statement.executeQuery(sql);
            Calendar calendar = GregorianCalendar.getInstance();
            while(resultSet.next()){
                long date = resultSet.getLong(1) * 1000;
                calendar.setTimeInMillis(date);
                int month = calendar.get(Calendar.MONTH);
                if(calendar.get(Calendar.YEAR) == year){
                    if(!months.contains(Month.values()[month])){
                        months.add(Month.values()[month]);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return months;
    }
    public Transaction getTransaction(int id){
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + TRANSACTION + " WHERE ID = " + id;
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                double amount = resultSet.getDouble("amount");
                long date = resultSet.getLong("timestamp") * 1000;
                int category_id = resultSet.getInt("category_id");
                String note = resultSet.getString("note");
                Category category = getCategoryById(category_id);
                return new Transaction(id, amount, category,date, note);
            }


            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void updateTransaction(Transaction transaction){
        try{
            Statement statement = connection.createStatement();
            String sql = "UPDATE " + TRANSACTION + " SET AMOUNT = " + transaction.getAmount()
                    +", TIMESTAMP = "+ transaction.getDate() /1000 + ", CATEGORY_ID = " + transaction.getCategory().getID()
                    + ", NOTE = '" + transaction.getNote() + "' WHERE ID = " + transaction.getId();
            statement.executeUpdate(sql);

            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteByID(int id){
        try{
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM " + TRANSACTION + " WHERE ID = "+ id;
            statement.executeUpdate(sql);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateUserData(User user){
        try{
            CATEGORY = "CATEGORY_" + user.getEmail();
            TRANSACTION = "TRANSACTION_" + user.getEmail();
            BUDGET = "BUDGET_" + user.getEmail();
            String email = user.getEmail();
            String password = user.getPassword();
            String token = user.getToken();
            String refreshtoken = user.getRefreshToken();
            createTables(user.getEmail());
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " +USER_DATA + " WHERE EMAIL = '"  + email + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                sql = "UPDATE "  + USER_DATA + " SET PASSWORD = '" + password + "', TOKEN = '" + token +"', REFRESH = '"
                + refreshtoken + "' WHERE EMAIL = '" + email + "'";
                Statement upStatement = connection.createStatement();
                upStatement.executeUpdate(sql);
                upStatement.close();
            }else{
                sql = "INSERT INTO " + USER_DATA + " VALUES('" + email + "', '" + password + "', '" + token
                + "', '" + refreshtoken + "')";
                Statement insStatement = connection.createStatement();
                insStatement.executeUpdate(sql);
                insStatement.close();
            }

            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
