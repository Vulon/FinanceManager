package utills;

import dataStructure.Category;
import dataStructure.Transaction;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HTTPMessenger {
    private static final String urlMainPath ="http://localhost:8080";

    public static int authLogin(String login, String password) {
        StringBuilder urlString = new StringBuilder(urlMainPath);
        urlString.append("/signin")
                .append("?login=")
                .append(login).append("&password=")
                .append(password);
        // Пока что пароль передается в url, есть 2 выхода, использоваться HTTPS или вместо пароля его хеш отправлять
        try {
            URL url = new URL(urlString.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return connection.getResponseCode();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            return 500;
        }
    }

    public static int authRegister(String login, String password) {
        StringBuilder urlString = new StringBuilder(urlMainPath);
        urlString.append("/signup");
        try {
            URL url = new URL(urlString.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeUTF(login);
            dos.flush();
            dos.writeUTF(password);
            dos.flush();
            return connection.getResponseCode();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            return 500;
        }
    }
    public static int loadData(ArrayList<Transaction> transactions, ObservableList<Category> categories){
        StringBuilder urlString= new StringBuilder(urlMainPath);
        try{
            URL url = new URL(urlString.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //PASTE YOUR CODE HERE
            int b=0;
            return connection.getResponseCode();
        }catch (java.io.IOException ex){
            ex.printStackTrace();
            return 500;
        }
    }
    public static int sendNewTransaction(Transaction transaction){
        StringBuilder urlString= new StringBuilder(urlMainPath);
        try{
            URL url = new URL(urlString.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int a=1;
            //PASTE YOUR CODE HERE
            return connection.getResponseCode();
        }catch (java.io.IOException ex){
            ex.printStackTrace();
            return 500;
        }
    }
}
