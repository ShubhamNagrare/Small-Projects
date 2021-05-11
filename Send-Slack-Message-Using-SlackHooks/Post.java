
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Post {
  
  public static void postSlack(String url, JsonNode postParams, JsonNode requestHeaders) {
    
    System.out.println("Starting Posting:");
    
    try {
    
    URL obj = new URL(url);
    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
    postConnection.setRequestMethod("POST");
    
    Iterator<String> reqIterator = requestHeaders.fieldNames();
    
    while(reqIterator.hasNext()) {
      String key = reqIterator.next();
      postConnection.setRequestProperty(key, requestHeaders.get(key).asText());
    }
    
    postConnection.setDoOutput(true);
    OutputStream os = postConnection.getOutputStream();
    os.write(new ObjectMapper().writeValueAsBytes(postParams));
    os.flush();
    os.close();
    
    int responseCode = postConnection.getResponseCode();
    System.out.println("THis is ResponseCode: " + responseCode);
    
    if(responseCode == HttpURLConnection.HTTP_OK) {
      
      BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
      
      String inputLine;
      
      StringBuffer response = new StringBuffer();
      while((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      
      System.out.println("Finish!!!");
    }
    else {
      System.out.println(" I am in Else...:(");
    }
    }
    catch(Exception e) {
      System.out.println("I am Exception" + e);
    }
    
    
    
  }

}
