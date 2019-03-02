package utills;

import dataStructure.Category;
import dataStructure.Transaction;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HTTPMessenger {
    private static final String urlString = "";
    public static String authLogin(String login, String password){
        String response = "Connection timeout";
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //PASTE YOUR CODE HERE
        }catch (java.io.IOException exc){
            response = "Wrong URL";
        }

        return response;//RETURN "OK", if sign in was successful, return "NOTMATCH" if login does not match password
    }
    public static String authRegister(String login, String password){
        String response = "Connection timeout";
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //PASTE YOUR CODE HERE
        }catch (java.io.IOException exc){
            response = "Wrong URL";
        }

        return response;//RETURN "OK", if sign in was successful, OR "INUSE" if login is already taken
    }
    public static String loadData(ArrayList<Transaction> transactions, HashMap<Integer, Category> categories){
        String response = "Connection timeout";
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //PASTE YOUR CODE HERE
        }catch (java.io.IOException exc){
            response = "Wrong URL";
        }
        return response; //Return "OK" if data loaded
    }
    public static String sendNewTransaction(Transaction transaction){
        String response = "OK";
        //send transaction info to server
        return response;
    }
}
