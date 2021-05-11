



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Slack {
  
 
  
  public static void sendNotificationToSlack(String title, String color, Boolean mrkdwnIn, ArrayNode fields) {
    
    System.out.println(" Entering slack booot : ");
    
    final String url = "";
    
    System.out.println("Putting data slots");
    
    JsonNode urlParameter = new ObjectMapper().createObjectNode();
    JsonNode slackData = new ObjectMapper().createObjectNode();
    
    System.out.println("Putting data slots");
    
    ((ObjectNode) slackData).put("title", title);
    ((ObjectNode) slackData).put("color", color);
    ((ObjectNode) slackData).put("mrkdwn_in", mrkdwnIn);
    ((ObjectNode) slackData).put("fields", fields);
    
    System.out.println("Putting data slots");
    
    ((ObjectNode) urlParameter).set("attachments", new ObjectMapper().createArrayNode().add(slackData));
    
    JsonNode requestHeaders = new ObjectMapper().createObjectNode();
    ((ObjectNode) requestHeaders).put("Content-Type", "application/json");
    
   // Post posting = new Post();
    
    Post.postSlack(url, urlParameter, requestHeaders);
    
    System.out.println(" Data sent to postSlack for posting:");
    
    
  }

}
