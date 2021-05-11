package com.shubham.maillistener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Utils {
  
  public static String sendParsedVerificationDataToOps(JsonNode postParams) throws IOException{
    
    System.out.println("Entering sendParsedVerificationDataToOps");
    
    StringBuffer response = new StringBuffer("");
    
    JsonNode requestHeaders = ((ObjectNode) new ObjectMapper().createObjectNode())
        .put("Content-Type", "application/json");

    try {

        URL obj = new URL(Constants.OPS_VERIFICATION_DATA_URL);
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setConnectTimeout(15000);
        postConnection.setRequestMethod("POST");

        Iterator<String> reqIterator = requestHeaders.fieldNames();

        while (reqIterator.hasNext()) {
            String key = reqIterator.next();
            postConnection.setRequestProperty(key, requestHeaders.get(key).asText());
        }
        postConnection.setDoOutput(true);
        OutputStream os = postConnection.getOutputStream();
        os.write(new ObjectMapper().writeValueAsBytes(postParams));
        os.flush();
        os.close();
        int responseCode = postConnection.getResponseCode();
        System.out.println("POST Response Code :  " + responseCode);
        System.out.println("POST Response Message : " + postConnection.getResponseMessage());
        //apiResponse.setStatusCode(responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
          //  apiResponse.setAsSuccess(ApiResponse.SUCCESS_STATUS);
          //  apiResponse.setData(new ObjectMapper().readTree(response.toString()));

        } else {
          //  apiResponse.setAsError(ApiResponse.ERROR_STATUS);
        }
    } catch (JsonParseException e) {
      System.out.println(e.getLocalizedMessage());
      //  apiResponse.setAsSuccess(ApiResponse.SUCCESS_STATUS);
      //  apiResponse.setData(((ObjectNode) new ObjectMapper().createObjectNode()).put("data", response.toString()));

    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
        //apiResponse.setAsError(ApiResponse.ERROR_STATUS);
    }
    
    return "SUCCESS";
    
  }

}
