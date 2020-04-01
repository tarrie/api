package io.tarrie.database;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import io.tarrie.Utility;

import java.util.ArrayList;
import java.util.List;

public class DynamoDb {
  private static final String AWS_ACCESS_KEY_ID_SQS = Utility.getParam("AWS_ACCESS_KEY_ID_SQS");
  private static final String AWS_SECRET_ACCESS_KEY_SQS =
      Utility.getParam("AWS_SECRET_ACCESS_KEY_SQS");
  private static final Regions REGION = Regions.US_EAST_1;

  private static final AWSCredentials credentials =
      new BasicAWSCredentials(AWS_ACCESS_KEY_ID_SQS, AWS_SECRET_ACCESS_KEY_SQS);

  private static final AmazonDynamoDB awsDynamoDb =
      AmazonDynamoDBClientBuilder.standard()
          .withRegion(REGION)
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .build();

  private static final DynamoDB dynamoDB = new DynamoDB(awsDynamoDb);

  public DynamoDb() {}

  public void createTable(String tableName) throws InterruptedException {
    try {

      List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
      attributeDefinitions.add(
          new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));

      List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
      keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

      // https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/JavaDocumentAPITablesExample.html
      CreateTableRequest request =
          new CreateTableRequest()
              .withTableName(tableName)
              .withKeySchema(keySchema)
              .withAttributeDefinitions(attributeDefinitions)
              .withProvisionedThroughput(
                  new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));

      System.out.println("Issuing CreateTable request for " + tableName);
      Table table = dynamoDB.createTable(request);

      System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
      table.waitForActive();
    } catch (Exception e) {
      System.err.println("CreateTable request failed for " + tableName);
      System.err.println(e.getMessage());
    }
  }

  static void deleteExampleTable(String tableName) {

    Table table = dynamoDB.getTable(tableName);
    try {
      System.out.println("Issuing DeleteTable request for " + tableName);
      table.delete();

      System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");

      table.waitForDelete();
    } catch (Exception e) {
      System.err.println("DeleteTable request failed for " + tableName);
      System.err.println(e.getMessage());
    }
  }
}
