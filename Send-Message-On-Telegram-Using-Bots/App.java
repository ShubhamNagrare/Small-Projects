package com.shubham.telegram;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class App {

  public static void main(String[] args) {
    
    
    String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

    String apiToken = "xyz"; //xyz=Api token, you will get from telegram while creating bot
    String chatId = "pqr" ;    // pqr = charId of you Telegram channel 
    String text = "SUCCESSFULLY DONE TELEGRAM BOT !!!! :)";

    urlString = String.format(urlString, apiToken, chatId, text);
    
    try{
    URL url = new URL(urlString);
    URLConnection conn = url.openConnection();

    StringBuilder sb = new StringBuilder();
    InputStream is = new BufferedInputStream(conn.getInputStream());
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String inputLine = "";
    while ((inputLine = br.readLine()) != null) {
        sb.append(inputLine);
    }
    String response = sb.toString();
    System.out.println(response);

  
  }
  catch(Exception e){
    System.out.println("I AM EXCEPTION " + e);
  }
  }
}
