package com.shubham.maillistener;

public interface Constants {
  
    // Mail Configurations
  public static final String EMAIL_HOST = "mydecisionanalytics.co.uk";
  public static final String EMAIL_RECEPIENT_ID = "shubham.n@mydecisionanalytics.co.uk";
  public static final String EMAIL_RECEPIENT_PASSWORD = "Doraemon@123";
  
  

    // Upload File System Types
    public final static String FILE_SYSTEM = "local";  //local
    public final static String BASE_DIRECTORY = "ops-uploads";
    public final static String BASE_FOLDER = "Verification_Forms";
  
    //AWS S3
    public final static String UPLOAD_FILESYSTEM_S3 = "s3";
    public final static String AWS_S3_ACCESSKEY = "AKIA2ABP2TL4ZXDFEUG7";
    public final static String AWS_S3_SECRETKEY = "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";
    public final static String AWS_S3_BUCKETNAME = "client-financial";
    public final static String AWS_S3_REGION = "ap-south-1";
    
    //LOCAL
    public final static String UPLOAD_FILESYSTEM_LOCALFILESYSTEM = "local";
    public final static String LOCAL_ROOT_DIRECTORY = "C:/Users/Tariq Iqbal";
    
    
    //OPS
    public final static String OPS_VERIFICATION_DATA_URL ="http://localhost:8090/api/getVerifiedDataToSave";
    
    
    //Verification Type
    public final static String VERIFICATION_TECHNICAL = "Technical";
    public final static String VERIFICATION_PROPERTY = "Property";
    public final static String VERIFICATION_LEGAL = "Legal";
    
    public final static String CONFIG_TECHNICAL_VERIFICATION_FORM = "technical_verification_form";
    public final static String CONFIG_PROPERTY_VERIFICATION_FORM = "property_verification_form";
    public final static String CONFIG_LEGAL_VERIFICATION_FORM = "legal_verification_form";




    
    




}
