package io.tarrie.database;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.BatchGetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import io.tarrie.Utility;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.database.datamodel.NewUser;
import io.tarrie.database.exceptions.MalformedInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.mail.internet.AddressException;
import java.net.MalformedURLException;
import java.util.*;

public class TarrieDynamoDb {
  private static final Logger LOG = LogManager.getLogger(TarrieDynamoDb.class);
  // AWS Constants
  private static final String AWS_ACCESS_KEY_ID = Utility.getParam("AWS_ACCESS_KEY_ID");
  private static final String AWS_SECRET_ACCESS_KEY = Utility.getParam("AWS_SECRET_ACCESS_KEY");

  private static final AWSCredentials credentials =
      new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
  public static final AmazonDynamoDB awsDynamoDb =
      AmazonDynamoDBClientBuilder.standard()
          .withRegion(DbConstants.DYNAMO_DB_REGION)
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .build();
  public static DynamoDB dynamoDB = new DynamoDB(awsDynamoDb);
  // private static final DynamoDB dynamoDB = new DynamoDB(awsDynamoDb);

  public static void main(String[] args)
      throws MalformedURLException, AddressException, MalformedInputException {

    NewUser testNewUser = new NewUser();
    testNewUser.setName("Becky Bartlock");
    testNewUser.setEmailAddr("beckb@u.northwestern.edu");
    testNewUser.setUserId("becky_b199.8_");
    testNewUser.setUserIdCopy("becky_b199.8_");
    /*
        DynamoDBMapper mapper = new DynamoDBMapper(awsDynamoDb);
        mapper.save(testNewUser);

        System.out.println(doesItemExist("USR#becky_b1998", "USR#becky_b1998"));
        System.out.println(doesItemExist("USR#becky_b1998", "USR#becky_b1998"));

        updateAttribute("USR#becky_b1998","USR#becky_b1998","test","valuetest");
    */
    ArrayList<String> testString = new ArrayList<>();
    testString.add("USR#jide69");
    testString.add("USR#becky_b1998");
    testString.add("USR#becky_b199.8_");
    testString.add("USR#becky_b199.8_BULLSHIT");

    System.out.println(doItemsExist(testString));
  }

  /**
   * Updates a attribute (or creates) a attribute associated to a item
   *
   * @param hashKeyValue the hash/primary key
   * @param rangeKeyValue the range/sort key
   * @param attributeName the name of the attribute to update
   * @param attributeValue the value of the attribute
   */
  public static void updateAttribute(
      String hashKeyValue, String rangeKeyValue, String attributeName, String attributeValue) {
    UpdateItemSpec updateItemSpec =
        new UpdateItemSpec()
            .withPrimaryKey(
                DbAttributes.HASH_KEY, hashKeyValue, DbAttributes.SORT_KEY, rangeKeyValue)
            .withUpdateExpression(String.format("set %s = :%s", attributeName, attributeName))
            .withValueMap(
                new ValueMap().withString(String.format(":%s", attributeName), attributeValue))
            .withReturnValues(ReturnValue.UPDATED_NEW);

    try {
      LOG.info("Updating the item...");
      UpdateItemOutcome outcome =
          dynamoDB.getTable(DbConstants.BASE_TABLE).updateItem(updateItemSpec);
      LOG.info("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
    } catch (Exception e) {
      LOG.error("Unable to update item: " + attributeName + " " + attributeValue, e);
    }
  }

  public static void updateAttribute(
      String hashKeyValue, String attributeName, String attributeValue) {
    updateAttribute(hashKeyValue, hashKeyValue, attributeName, attributeValue);
  }
  /**
   * Checks if item exists on dynamodb
   *
   * @param hashKeyValue the hash/primary key
   * @param rangeKeyValue the range/sort key
   * @return true if item exists
   */
  public static boolean doesItemExist(String hashKeyValue, String rangeKeyValue) {
    GetItemSpec spec =
        new GetItemSpec()
            .withPrimaryKey(
                DbAttributes.HASH_KEY, hashKeyValue, DbAttributes.SORT_KEY, rangeKeyValue)
            .withProjectionExpression(DbAttributes.HASH_KEY);
    Item item = dynamoDB.getTable(DbConstants.BASE_TABLE).getItem(spec);

    return item != null;
  }

  /**
   * Checks to see if the inputted ids exist.
   * @param ids collection of ids to check existence of
   * @return subset of ids that actually exist
   */
  public static List<String> doItemsExist(List<String> ids) {
    // ToDo: exception handling on DynamoDb

    if (ids.size()>100){
      throw new MalformedInputException("Too many ids to check max amount is 100");
    }

    // fill up TableKeysAndAttributes
    TableKeysAndAttributes keysAndAttributes = new TableKeysAndAttributes(DbConstants.BASE_TABLE);
    for (String id : ids) {
      keysAndAttributes.addHashAndRangePrimaryKey(
          DbAttributes.HASH_KEY, id, DbAttributes.SORT_KEY, id);
    }
    keysAndAttributes.withProjectionExpression(DbAttributes.HASH_KEY);

    // get item from DynamoDB
    BatchGetItemSpec spec = new BatchGetItemSpec().withTableKeyAndAttributes(keysAndAttributes);
    BatchGetItemOutcome itemOutcome = dynamoDB.batchGetItem(spec);

    // process the itemOutcome to a list of foundId's
    List<Map<String, AttributeValue>> unprocessedIds =
        itemOutcome.getBatchGetItemResult().getResponses().get(DbConstants.BASE_TABLE);
    ArrayList<String> foundIds = new ArrayList<>();
    for (Map<String, AttributeValue> map : unprocessedIds) {
      foundIds.add(map.get(DbAttributes.HASH_KEY).getS());
    }
    // returned processed ids
    return foundIds;
  }

  public static boolean doesItemExist(String hashKeyValue) {
    return doesItemExist(hashKeyValue, hashKeyValue);
  }
}
