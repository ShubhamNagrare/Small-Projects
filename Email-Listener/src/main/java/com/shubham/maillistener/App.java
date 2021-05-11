package com.shubham.maillistener;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.MimeBodyPart;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.mail.imap.IMAPFolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class App {
  public static void main(String[] args) {
    System.out.println("Starting....");
    
    System.out.println("Checking Environment Variables....");
    System.out.println();
    
    
    
    
    
    try {
      Properties props = System.getProperties();
      
      System.out.println("NAME :" +  props.get("name").toString());
      
      
      Session session = Session.getInstance(props, null);
      Store store = session.getStore("imap");
      store.connect(Constants.EMAIL_HOST, Constants.EMAIL_RECEPIENT_ID,
          Constants.EMAIL_RECEPIENT_PASSWORD);
      IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
      if (folder == null || !folder.exists()) {
        System.out.println("Invalid folder");
        System.exit(1);
      }
      folder.open(Folder.READ_WRITE);

 MessageCountListener messageCountListener = new MessageCountAdapter() {
   public void messagesAdded(MessageCountEvent ev) {
   System.out.println("Inside  messagesAdded.");
   Message[] msgs = ev.getMessages();
   System.out.println("Got " + msgs.length + " new messages");
          
   
   int cnt =1;
    for (int i = 0; i < msgs.length; i++) {
      
      System.out.println("I AM COUNTING " + cnt);
      cnt++;
            
      Message currMessage = msgs[i];
      String attachFiles = null;
      String messageContent = null;
            try {
              System.out.println("Content-Type:" + currMessage.getContentType());
              System.out.println("Content:" + currMessage.getContent().toString());
              
              //Email From
              String emailFrom = currMessage.getFrom()[i].toString();
              
              //Media Lists
              ArrayNode typeList = new ObjectMapper().createArrayNode();
              ArrayNode sourceList = new ObjectMapper().createArrayNode();
              
              
              //File Upload system
              OpsFile opsFile = OpsFile.getFileSystem(Constants.FILE_SYSTEM);
              
              //If it is Excell & verificationForm 
              InputStream  verificationformInputStream = null;
              String verificationFormLoanApplicationNumber = "";
              String verificationFormVerificationType = "";
              MimeBodyPart excellPart = null;
              

                if (currMessage.getContentType().contains("multipart")) {
                    Multipart multiPart = (Multipart) currMessage.getContent();
                    int numberOfParts = multiPart.getCount();               
                    System.out.println("Number of Parts:" + numberOfParts);
                              
                    
                    
                    //First for loop to get the excell first
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                      
                      System.out.println("INSIDE FIRST FOR LOOOOP");
                      System.out.println();
                      
                      
                      MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                      System.out.println("Part Dispostion:" + part.getDisposition());

                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            System.out.println("HEREE NAME :  " + part.getFileName());
                            
                            String extension = part.getFileName().substring(part.getFileName().lastIndexOf(".") + 1); 
                            System.out.println("EXTENSION : " + extension);
                            System.out.println("EXTENSION : " + extension.length());                           
                            String fileName =  part.getFileName().substring(0, part.getFileName().length() - (extension.length()+1)) ;
                                                  
                            int firstSpace = fileName.indexOf(" ");
                            int lastSpace = fileName.lastIndexOf(" ");
                            
                            int cut = 0;     
                            if(lastSpace>0) {
                              cut = lastSpace;
                            }
                           
                            String typeOfVerification = fileName.substring(0, cut).toString();
                            System.out.println("TYPE OF VERIFICATION: " + typeOfVerification);
                                            
                            if( extension.contains("xlsx") &&  typeOfVerification.contains(Constants.CONFIG_TECHNICAL_VERIFICATION_FORM)   || 
                              typeOfVerification.contains(Constants.CONFIG_PROPERTY_VERIFICATION_FORM)   ||  
                                typeOfVerification.contains(Constants.CONFIG_LEGAL_VERIFICATION_FORM))    {
                              
                              
                              System.out.println("I AM INSIDE FIRST FOR LOOP'S IF........");
                              
                              System.out.println();
                              
                                   System.out.println("I AM PARTTTTTT  " + part.getDataHandler().getInputStream().toString());
                                   //if it is excell and verification form as well
                                   verificationformInputStream = part.getDataHandler().getInputStream();
                                   excellPart = part;
                                   
                                   System.out.println("I AM INPUT STREAME  " + verificationformInputStream.toString());
                                   
                                   
                                   verificationFormLoanApplicationNumber = fileName.substring((lastSpace+1));
                                   verificationFormVerificationType = fileName.substring(0, firstSpace).toString();
                                   
                                   //saving excell to s3/loaclFileSystem
                                   String excellSavedpath = opsFile.write(Constants.BASE_DIRECTORY, Constants.BASE_FOLDER, verificationFormLoanApplicationNumber,
                                       verificationFormVerificationType, part.getFileName(),  excellPart.getDataHandler().getInputStream());
                                   
                                   //adding path to source arrayNode
                                   sourceList.add(excellSavedpath);
                                }              
                        }else {
                          messageContent = part.getContent().toString();
                        }
                    }
                    
                 System.out.println();   System.out.println();             
                    
         //Second for loop for Other Media including other excell
         for (int partCount = 0; partCount < numberOfParts; partCount++) {
           
           System.out.println("INSIDE SECOND FOR LOOOOP");
           System.out.println();
           
           MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
           //System.out.println("Part Dispostion:" + part.getDisposition());

             if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                   // System.out.println("HEREE NAME :  " + part.getFileName());
                                          
                    String extension = part.getFileName().substring(part.getFileName().lastIndexOf(".") + 1); 
                    System.out.println("EXTENSION : " + extension);                          
                    String fileName =  part.getFileName().substring(0, part.getFileName().length() - (extension.length()+1)) ;
                                      
                    int firstSpace = fileName.indexOf(" ");
                    int cut = 0;     
                    if(firstSpace>0) {
                      cut = firstSpace;
                    }
                    
                    String typeOfVerification = fileName.substring(0, cut).toString();
                    System.out.println("TYPE OF VERIFICATION: " + typeOfVerification);
                                    
                  if( !(extension.contains("xlsx") &&  typeOfVerification.contains(Constants.CONFIG_TECHNICAL_VERIFICATION_FORM)   || 
                    typeOfVerification.contains(Constants.CONFIG_PROPERTY_VERIFICATION_FORM)   ||  
                    typeOfVerification.contains(Constants.CONFIG_LEGAL_VERIFICATION_FORM)))    {
                 
                    //saving excell to s3/loaclFileSystem                    
                    String fileSavedpath = opsFile.write(Constants.BASE_DIRECTORY, Constants.BASE_FOLDER, 
                        verificationFormLoanApplicationNumber,
                    verificationFormVerificationType, part.getFileName(),  part.getDataHandler().getInputStream());

                    //adding path to source arrayNode
                    sourceList.add(fileSavedpath);                     
                   }
                  } else {
                    messageContent = part.getContent().toString();
                  }
                }  
         
            /* Both for loop ends here so now we are good to call store data and pass verification 
             * form information  to parse data along with media type and source path to send to ops backend
             to store information in the database */
                 
                    
                  // Call storeData method to parse excelll and send to ops
                 String retStr = storeData(verificationformInputStream, verificationFormVerificationType, 
                     verificationFormLoanApplicationNumber , emailFrom, typeList , sourceList);
                 
                 System.out.println("RETURN STRING :"  + retStr);
                }
              } catch (MessagingException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      };
      folder.addMessageCountListener(messageCountListener);
      int freq = Integer.parseInt("1000");
      while (true) {
        if (folder.isOpen()) {
          folder.idle();
         }
          Thread.sleep(freq);
        }
      }catch (Exception ex) {
      ex.printStackTrace();
    }
    
  }

  
  /**
   * 
   * @param inputStream
   * @throws IOException
   */
  private static String storeData(InputStream inputStream, String verificationFormType, 
      String loanApplicationNumber, String emailFrom, ArrayNode typelist , ArrayNode sourceList)
          throws IOException {

    System.out.println("Inside store data.......");

    JsonNode requestData = new ObjectMapper().createObjectNode();
    String verificationType="";
    
    if(verificationFormType.contains("technical_verification_form")) {
      verificationType = Constants.VERIFICATION_TECHNICAL;
    }else if(verificationFormType.contains("property_verification_form")) {
      verificationType = Constants.VERIFICATION_PROPERTY;
    }else if(verificationFormType.contains("legal_verification_form")) {
      verificationType = Constants.VERIFICATION_LEGAL;
    }

    
    Workbook workbook = new XSSFWorkbook(inputStream);
    Sheet inputSheet = workbook.getSheetAt(0);
    Iterator<Row> iterator = inputSheet.iterator();
    
    int flag=0;
    JsonNode reqData = new ObjectMapper().createObjectNode();
    
    ArrayNode fields = new ObjectMapper().createArrayNode();
    ArrayNode values = new ObjectMapper().createArrayNode();
    
    while (iterator.hasNext()) {
      System.out.println("Inside while looop...");
      
      Row currentRow = iterator.next();
      Iterator<Cell> cellIterator = currentRow.iterator();
      
      
      while (cellIterator.hasNext()) {
        
          Object currentCellValue = null;
          if (currentCellValue == null) {
              System.out.println();
          }
          
          Cell currentCell = cellIterator.next();
          if (currentCell.getCellType() == CellType.STRING) {
              currentCellValue = String.valueOf(currentCell.getStringCellValue());
              if (StringUtils.isBlank(String.valueOf(currentCellValue)))
                  currentCellValue = "";
          } else if (currentCell.getCellType() == CellType.NUMERIC) {
              currentCellValue = currentCell.getNumericCellValue();
              if (currentCellValue == null)
                  currentCellValue = 0.0;
          }
          System.out.print(currentCellValue + ",");
          
          
          if(flag == 0) {
            fields.add(currentCellValue.toString());
          }
          else if(flag != 0) {
            values.add(currentCellValue.toString());
          }         
      }  
      System.out.println("Fields  "  + fields.toString());
      System.out.println("Values  "  + values.toString());
      flag = flag + 1;
      }
    
    for(int i=0; i < fields.size() ; i++) {         
      ((ObjectNode) reqData).put(verificationType + " " + fields.get(i).asText(), values.get(i).asText());
    }   

    
    
    //setting media information   
      ((ObjectNode) reqData).set(verificationType + " " + "Type", typelist);
      ((ObjectNode) reqData).set(verificationType + " " + "Source", sourceList);

    
    System.out.println("Req Data: " + reqData);
    
    
    ((ObjectNode) requestData).put("verificationType", verificationType);
    ((ObjectNode) requestData).put(verificationType + " " + "verificationConfirmationReceivingDate", new Date().toString());
    ((ObjectNode) requestData).put("loanApplicationNumber", loanApplicationNumber);
    ((ObjectNode) requestData).put(verificationType + " " + "emailFrom", emailFrom);
    ((ObjectNode) requestData).set("data", reqData);
      
      
      System.out.println("Flag  : " + flag);
      
      System.out.println("REQUEST Data: " + requestData);
      
      String status = Utils.sendParsedVerificationDataToOps(requestData);

      System.out.println("SUCCESSSSSSS       " + status);
      
      return "Success";
      
    }
    


  



}


