import java.util.Date;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class App {
  
  public static void main(String []args) {
    
    
    System.out.println("I am in Main");
    
   // Slack s = new Slack();
    
    String title =  "Slack Message !!!!";
    String color = "#7CD197";
    Boolean mrkdwnIn = true;
    ArrayNode fields = new ObjectMapper().createArrayNode()
        .add(new ObjectMapper().createObjectNode().put("title", "Shubham")
            .put("value", new Date().toString()).put("short", true))
        .add(new ObjectMapper().createObjectNode().put("title", "Nitin")
            .put("value", 1).put("short", true));
    
    
    Slack.sendNotificationToSlack(title, color, mrkdwnIn, fields);
    
    
    
  }

}
