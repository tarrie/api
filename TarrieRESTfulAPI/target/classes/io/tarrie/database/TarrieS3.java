package io.tarrie.database;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import io.tarrie.database.contants.EntityType;
import io.tarrie.database.exceptions.MalformedInputException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.io.IOUtils;
import io.tarrie.Utility;
import io.tarrie.database.contants.DbConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TarrieS3 {
  private static final Logger LOG = LogManager.getLogger(TarrieS3.class);

  // AWS Constants
  private static final String AWS_ACCESS_KEY_ID = Utility.getParam("AWS_ACCESS_KEY_ID");
  private static final String AWS_SECRET_ACCESS_KEY = Utility.getParam("AWS_SECRET_ACCESS_KEY");
  private static final AWSCredentials credentials =
      new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
  public static String SUFFIX = "/";
  public static AmazonS3 s3client =
      AmazonS3ClientBuilder.standard()
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .withRegion(DbConstants.S3_REGION)
          .build();

  public static void main(String[] args) throws IOException, URISyntaxException, EncoderException {
    TarrieS3 test = new TarrieS3();
    test.listBucketContent(DbConstants.IMG_S3_BUCKET);
    //test.createFolder("tarrie.io","test");

    System.out.println("Working Directory = " +
            System.getProperty("user.dir"));

    File initialFile = new File("../pictures/becky_b1998.jpeg");
    InputStream targetStream = new FileInputStream(initialFile);

    System.out.println(test.uploadProfileImg(targetStream,"image/jpeg", "GRP#becky_b1998"));

  }

  /**
   * Create a folder on s3
   */
  public void createFolder(String bucketName, String folderName) {
    // create meta-data for your folder and set content-length to 0
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(0);
    // create empty content
    InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
    // create a PutObjectRequest passing the folder name suffixed by /
    PutObjectRequest putObjectRequest =
        new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent, metadata);
    // send request to S3 to create folder
      s3client.putObject(putObjectRequest);
  }

  /**
   * Uploads profile image to S3 of the form : /{entityType}/pictures/{entityId}/profile.{mimeType}
   * <ul>
   *     <li>See https://stackoverflow.com/questions/35582224/uploading-image-to-s3-bucket-java-sdk</li>
   *     <li>See https://sysadmins.co.za/aws-java-sdk-detect-if-s3-object-exists-using-doesobjectexist/</li>
   * </ul>
   * @param is inputstream that holds img
   * @param mimeType the mimeType of the image for example: image/jpeg
   * @param entityId the id of the entity for example: EVT#123xyz
   * @return IOException if can't convert inputstream to a byte array
   * @throws MalformedInputException invalid mimeType or invalid entityId
   */
  public static String  uploadProfileImg(InputStream is,String mimeType, String entityId) throws MalformedInputException, IOException {
    String info = String.format("{entityId:%s, mimeType:%s}",entityId, mimeType );

    // check if mimeType is in required types
    if (!(DbConstants.ACCEPTABLE_MIME_IMAGES.contains(mimeType))){
      throw new MalformedInputException("Invalid img MIME type:"+info );
    }

    // split the entityId by "#" to get the entityType
    String[] arrOfStr = entityId.split("#", 2);
    String entityType = arrOfStr[0];
    String id = arrOfStr[1];

    // split the mimeType by "/" to get the file suffix
    String[] arrOfStr2 = mimeType.split("/", 2);
    String fileSuffix = arrOfStr2[1];


    byte[] contents = IOUtils.toByteArray(is);
    InputStream stream = new ByteArrayInputStream(contents);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(contents.length);
    metadata.setContentType(mimeType);

    String folderName = SUFFIX+"pictures"+SUFFIX+id;
    switch (entityType){
      case EntityType.GROUP:
        folderName = "groups"+folderName;
        break;
      case EntityType.USER:
        folderName = "users"+folderName;
        break;
      case EntityType.EVENT:
        folderName = "events"+folderName;
        break;
      default:
        throw new MalformedInputException("Invalid Tarrie entity id: "+info );
    }

    // post to s3
    String s3url = null;
    try{
      String fileName = folderName+SUFFIX+"profile."+fileSuffix;
      PutObjectResult putObjectResult=s3client.putObject(new PutObjectRequest(
              DbConstants.IMG_S3_BUCKET, fileName, stream, metadata));

      // formulate s3url of newly created image
      String region = DbConstants.S3_REGION.toString().toLowerCase().replace("_","-");
      s3url =  String.format("https://s3.%s.amazonaws.com/%s/%s", region, DbConstants.IMG_S3_BUCKET, fileName);

    }catch (AmazonServiceException e){
      LOG.error(
          "The call was transmitted successfully, but Amazon S3 couldn't process \n"
              + "it, so it returned an error response: "+info);
      // ToDo: Error retry
    }catch (SdkClientException e){
      LOG.error("Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3:"+ info);
      // ToDo: Error retry
    }
    return s3url;
  }


  public void deleteProfileImg(String s3url){
    Pattern pattern = Pattern.compile(".*amazonaws\\.com/"+DbConstants.IMG_S3_BUCKET+"/(?<fileName>.*)\\.*");
    Matcher matcher = pattern.matcher(s3url);

    if (matcher.find()){
      String fileName = matcher.group("fileName");
      s3client.deleteObject(DbConstants.IMG_S3_BUCKET, fileName);

    }else{
      throw new MalformedInputException("Invalid S3 url: "+s3url );
    }
  }

  /**
   * List the items in a s3 bucket
   */
  public void listBucketContent(String bucketName) {
    ObjectListing listing =
        s3client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
    for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
      System.out.println(
          " -> "
              + objectSummary.getKey()
              + "  "
              + "(size = "
              + objectSummary.getSize() / 1024
              + " KB)");
    }
  }

  /**
   * List buckets associated with aws account
   */
  public void listBuckets() {
    for (Bucket bucket : s3client.listBuckets()) {
      System.out.println(" - " + bucket.getName());
    }
  }
}
