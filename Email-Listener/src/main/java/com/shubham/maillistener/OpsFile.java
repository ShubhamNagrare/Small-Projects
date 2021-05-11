package com.shubham.maillistener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public abstract class OpsFile {
  public abstract String read(String path) throws IOException;

  
  public abstract String write(String basePath, String verificationForms , String loanApplicationNo, String verificationType,
       String fileName, InputStream inputStream) throws IOException;



  public static OpsFile getFileSystem(String currFileSystem) {
    OpsFile losFile = new LocalFileSystem();

  //  String fileSystem = configuration.get("upload_file_system").asText();
    if (currFileSystem.equalsIgnoreCase(Constants.UPLOAD_FILESYSTEM_S3)) {
      losFile = new S3();
    } else if (currFileSystem.equalsIgnoreCase(Constants.UPLOAD_FILESYSTEM_LOCALFILESYSTEM)) {
      losFile = new LocalFileSystem();
    }
    return losFile;
  }


  public static String getFilePath(String baseDir, String verificationForms , String loanApplicationNo, String verificationType) {
    return baseDir + "/" + verificationForms + "/" + loanApplicationNo + "/" + verificationType ;
  }

}
