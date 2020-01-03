package io.tarrie.database;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBGeneratedUuid;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import io.tarrie.Utility;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.database.datamodel.User;

import javax.mail.internet.AddressException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DynamoDb {

  // AWS Constants
  private static final String AWS_ACCESS_KEY_ID = Utility.getParam("AWS_ACCESS_KEY_ID");
  private static final String AWS_SECRET_ACCESS_KEY =
      Utility.getParam("AWS_SECRET_ACCESS_KEY");

  private static final AWSCredentials credentials =
      new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
  private static final AmazonDynamoDB awsDynamoDb =
      AmazonDynamoDBClientBuilder.standard()
          .withRegion(DbConstants.DYNAMO_DB_REGION)
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .build();
  //private static final DynamoDB dynamoDB = new DynamoDB(awsDynamoDb);

  public static void main(String[] args) throws MalformedURLException, AddressException {
    User testUser = new User();
    testUser.setImgPath(DbConstants.DEFAULT_USER_IMG);
    testUser.setName("Becky Bartlock");
    testUser.setEmailAddr("beckb@u.northwestern.edu");
    testUser.setUserId("becky_b1998");
    testUser.setUserIdCopy("becky_b1998");

    DynamoDBMapper mapper = new DynamoDBMapper(awsDynamoDb);
    mapper.save(testUser);

  }



}
