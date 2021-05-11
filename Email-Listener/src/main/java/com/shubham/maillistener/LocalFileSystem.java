package com.shubham.maillistener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;


public class LocalFileSystem extends OpsFile {

  public String read(String filePath) throws IOException {

    System.out.println("Entering read...");

    if (!(new File(filePath).exists())) {
      throw new IOException("the specified path does not exist !!! " + filePath);
    }

    if (!(new File(filePath).canRead())) {
      throw new IOException("Read permission missing !!! " + filePath);
    }

    File file = new File(filePath);

    FileInputStream fileInputStream = new FileInputStream(file);

    byte fileData[] = new byte[(int) file.length()];
    fileInputStream.read(fileData);
    String base64File = Base64.getEncoder().encodeToString(fileData);
    fileInputStream.close();

    System.out.println("Exiting read...\n");

    return base64File;
  }



  public String write(String basePath, String verificationForms , String loanApplicationNo, String verificationType,
      String fileName, InputStream inputStream) throws IOException {
  
   System.out.println("Entering write in localFileSystem.....");
   

       
       
   if (!(new File(Constants.LOCAL_ROOT_DIRECTORY).exists())) {
   throw new IOException("the specified path does not exist !!! " + Constants.LOCAL_ROOT_DIRECTORY);
   }
  
   if (!(new File(Constants.LOCAL_ROOT_DIRECTORY).canWrite())) {
   throw new IOException("Write permission missing !!! " + Constants.LOCAL_ROOT_DIRECTORY);
   }
  
   String retPath = Constants.LOCAL_ROOT_DIRECTORY + "/" + OpsFile.getFilePath( basePath, verificationForms , loanApplicationNo, verificationType);

  
  
   // if the directory does not exist, create it
   File documentTypeFolder = new File(retPath);
   if (!documentTypeFolder.exists()) {
     System.out.println("creating directory: " + documentTypeFolder.getName());
   documentTypeFolder.mkdirs();
   }
   
   
   OutputStream outputStream = null;

   try {
   int i = 0;
   
    outputStream = new FileOutputStream(new File( retPath + "/" + new Date().getTime() + "_" + fileName ));
   
   int read = 0;
   byte[] bytes = new byte[1024];

   while ((read = inputStream.read(bytes)) != -1) {
       outputStream.write(bytes, 0, read);
   }
   
   } catch (IOException e) {
     e.printStackTrace();
 } finally {
     if (outputStream != null) {
         try {
             outputStream.close();
         } catch (IOException e) {
             e.printStackTrace();
         }

     }
 }
  
   System.out.println(" Generated Successfully...");
   System.out.println("Entering write in localFileSystem.....");
   return retPath;
  
   }

}
