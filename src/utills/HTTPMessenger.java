package utills;

import dataStructure.Category;
import dataStructure.Transaction;
import dataStructure.User;
import javafx.collections.ObservableList;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HTTPMessenger {
    private static final String urlMainPath ="http://localhost:8080";
    public User user;
    public void setUserData(User userdata){
        user = userdata;
    }
    private static HTTPMessenger link;
    private HTTPMessenger(){

    }
    public static HTTPMessenger getInstance(){
        if(link == null){
            link = new HTTPMessenger();
        }
        return link;
    }

    public int authLogin(String login, String password) {
        StringBuilder urlString = new StringBuilder(urlMainPath);
        urlString.append("/signin")
                .append("?login=")
                .append(login).append("&password=")
                .append(password);
        // Пока что пароль передается в url, есть 2 выхода, использоваться HTTPS или вместо пароля его хеш отправлять
        try {
            URL url = new URL(urlString.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String token = "token";
            String refreshToken = "refresh";
            //if the email and password correct, update userdata
            user = new User(login, password, token, refreshToken);
            return connection.getResponseCode();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            return 500;
        }
    }

    public int authRegister(String login, String password) {
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
    public int loadData(){
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
    public int sendNewTransaction(Transaction transaction){
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
    public void updateTransaction(Transaction transaction){
        //TODO IMPLEMENT TRANSACTION UPDATE
    }
    public void deleteTransaction(int id){
        //TODO IMPLEMENT TRANSACTION DELETE
    }
}
