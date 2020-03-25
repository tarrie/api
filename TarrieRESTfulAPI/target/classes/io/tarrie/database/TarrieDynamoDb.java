package io.tarrie.database;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.BatchGetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import io.tarrie.Utility;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.condensed.UserCondensed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

  public static final int MAX_WAIT_INTERVAL = 10;

  public static void main(String[] args)
      throws MalformedURLException, AddressException, MalformedInputException {

    UserCondensed testNewUser = new UserCondensed();
    testNewUser.setName("Becky Bartlock");
    testNewUser.setEmailAddr("beckb@u.northwestern.edu");
    testNewUser.setId("becky_b199.8_");
    testNewUser.setIdCopy("becky_b199.8_");
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
   *
   * @param ids collection of ids to check existence of
   * @return subset of ids that actually exist
   */
  public static List<String> doItemsExist(List<String> ids) throws MalformedInputException {
    // ToDo: exception handling on DynamoDb

    if (ids.size() > 100) {
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

  /**
   * Writes a transaction to DynamoDb
   *
   * @see <a
   *     href="https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.Transactions.html">AWS
   *     Example</a>
   * @param transactionWriteRequest the transaction request to write
   */
  public static void executeTransactionWrite(TransactionWriteRequest transactionWriteRequest)
      throws ConditionalCheckFailedException {
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);
      mapper.transactionWrite(transactionWriteRequest);
    } catch (DynamoDBMappingException ddbme) {
      LOG.error("Client side error in Mapper, fix before retrying. Error: " + ddbme.getMessage());
    } catch (ResourceNotFoundException rnfe) {
      LOG.error(
          "One of the tables was not found, verify table exists before retrying. Error: "
              + rnfe.getMessage());
    } catch (InternalServerErrorException ise) {
      LOG.error(
          "Internal Server Error, generally safe to retry with back-off. Error: "
              + ise.getMessage());
    } catch (TransactionCanceledException tce) {
      LOG.error(
          "Transaction Canceled, implies a client issue, fix before retrying. Error: "
              + tce.getMessage());
    } catch (ConditionalCheckFailedException ccfe) {
      LOG.error("Conditional write exception" + ccfe.getErrorMessage());
      throw ccfe;
    } catch (Exception ex) {
      LOG.error(
          "An exception occurred, investigate and configure retry strategy. Error: "
              + ex.getMessage());
    }
  }

  public static boolean doesItemExist(String hashKeyValue) {
    return doesItemExist(hashKeyValue, hashKeyValue);
  }

  /** Returns the next wait interval, in milliseconds, using an exponential backoff algorithm. */
  private static long getWaitTimeExp(int retryCount) {
    return ((long) Math.pow(2, retryCount) * 100L);
  }

  /**
   * Checks for unprocessedKeys after a BatchWriteOutcome and resends if it finds some. Exponential
   * back off algo
   *
   * @see <a href="https://docs.aws.amazon.com/general/latest/gr/api-retries.html">Error Retries and
   *     Exponential Backoff in AWS </a>
   * @see <a
   *     href="https://docs.amazonaws.cn/en_us/amazondynamodb/latest/developerguide/batch-operation-document-api-java.html">Batch
   *     Operations Using AWS SDK for Java Document API</a>
   * @param outcome BatchWriteItemOutcome
   */
  private static void checkForUnprocessedKeys(BatchWriteItemOutcome outcome) {
    int retries = 0;
    try {
      do {

        Thread.sleep(Math.min(getWaitTimeExp(retries), getWaitTimeExp(MAX_WAIT_INTERVAL)));

        // Check for unprocessed keys which could happen if you exceed
        // provisioned throughput
        Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

        if (outcome.getUnprocessedItems().size() == 0) {
          System.out.println("No unprocessed items found");
        } else {
          System.out.println("Retrieving the unprocessed items");
          outcome = TarrieDynamoDb.dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
        retries++;
      } while (outcome.getUnprocessedItems().size() > 0);
    } catch (InterruptedException ignored) {
    }
  }

  /**
   * Retries failed batch writes
   * @param outcome failed batches
   */
  private static void checkForUnprocessedKeys(List<DynamoDBMapper.FailedBatch> outcome) {
    if ((outcome == null) || outcome.size() == 0) {
      return;
    }

    for (DynamoDBMapper.FailedBatch failedBatch : outcome) {
      checkForUnprocessedKeys(
          TarrieDynamoDb.dynamoDB.batchWriteItemUnprocessed(failedBatch.getUnprocessedItems()));
    }
  }

  /**
   * Performs batch writes using DynamoDb mapper
   * @see <a href="http://tutorials.jenkov.com/java-generics/methods.html">Java Generics</a>
   * @param objectsToSave the set of pojo's to save
   */
  public static <T> void batchWriteOutcome(Set<T> objectsToSave) {
    DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);
    checkForUnprocessedKeys(mapper.batchSave(objectsToSave));
  }

  /**
   * Performs batch write on a collection of items
   *
   * @param items
   * @return
   */
  public static void batchWriteOutcome(ItemCollection<QueryOutcome> items) {
    int pageNumber = 0;
    int itemsSeen = 0;
    TableWriteItems tableWriteItems = new TableWriteItems(DbConstants.BASE_TABLE);
    ;
    BatchWriteItemOutcome outcome;

    // loop through pages and delete items
    for (Page<Item, QueryOutcome> page : items.pages()) {
      pageNumber++;
      System.out.println("Page: " + pageNumber + " #####");
      for (Item item : page) {
        // add partition and sort key to list()
        tableWriteItems.addHashAndRangePrimaryKeyToDelete(
            DbAttributes.HASH_KEY,
            item.get(DbAttributes.HASH_KEY).toString(),
            DbAttributes.SORT_KEY,
            item.get(DbAttributes.SORT_KEY).toString());

        itemsSeen++;

        if (itemsSeen >= DbConstants.MAX_ITEMS_DYNAMODB_BATCH) {

          // check for unprocessed keys
          outcome = TarrieDynamoDb.dynamoDB.batchWriteItem(tableWriteItems);
          checkForUnprocessedKeys(outcome);

          // reset the writeItems
          tableWriteItems = new TableWriteItems(DbConstants.BASE_TABLE);

          itemsSeen = 0;
        }
      }
    }

    // delete the remaining items to be deleted
    if (itemsSeen > 0) {
      // check for unprocessed keys
      outcome = TarrieDynamoDb.dynamoDB.batchWriteItem(tableWriteItems);
      checkForUnprocessedKeys(outcome);
    }
  }
}
