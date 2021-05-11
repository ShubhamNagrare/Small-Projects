package com.shubham.maillistener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

public class S3 extends OpsFile {
  private static final Logger logger = LoggerFactory.getLogger(S3.class);

  private String accessKey = Constants.AWS_S3_ACCESSKEY;
  private String secretKey = Constants.AWS_S3_SECRETKEY;
  private String bucketName = Constants.AWS_S3_BUCKETNAME;
  private String region = Constants.AWS_S3_REGION;


  public String read(String filePath) throws IOException {

    logger.trace("Entering read...");
    logger.trace("Exiting read...\n");

    return null;
  }


  public String write(String basePath, String verificationForms , String loanApplicationNo, String verificationType,
      String fileName, InputStream inputStream) throws IOException {
    logger.trace("Entering write in S3 FileSystem.....");
    // logger.trace("Total " + inputStream + " MultipartFiles ");

    String retVal = "";

    String retPath = OpsFile.getFilePath(basePath, verificationForms, loanApplicationNo, verificationType);
    
    System.out.println("AWS S3 RETPATH :" + retPath);

    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.AP_SOUTH_1).build();
    TransferManager tm = TransferManagerBuilder.standard().withS3Client(amazonS3).build();


    // To check whether Access-key & Secret-Key is Valid or Not
    try {
      if (amazonS3.listBuckets().size() > 0) {


        // SANITY CHECKS....to check whether Bucket Exist or Not.....
        if (amazonS3.doesBucketExist(bucketName)) {
          logger.trace("Bucket exist.....");

          int i = 0;
          ObjectMetadata objectMetadata = new ObjectMetadata();
          objectMetadata = new ObjectMetadata();
          tm.upload(bucketName, retPath + "/" + new Date().getTime() + "_"
              + fileName,
              inputStream, objectMetadata);
          // logger.trace("Document saved successfully..." + multipartFile.getOriginalFilename());
          retVal = retPath;
        } else {
          logger.trace("Error.............Bucket dont exist...");
          logger.trace("Document not created !!!");
        }


      }
    } catch (Exception e) {
      throw new IOException(
          "Your AWS S3 Credentials or bucket name is mismatch. Please check and try again !!!");
    }


    logger.trace("Exiting write in S3 FileSystem.....");
    return retVal;
  }

}
