package io.tarrie.database;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

/// DYNAMO DB
/// https://www.baeldung.com/dynamodb-local-integration-tests
/// https://stackoverflow.com/questions/26901613/easier-dynamodb-local-testing
/// https://stackoverflow.com/questions/3239039/set-value-of-private-static-field
public class TestDbHelper {

  private DynamoDBProxyServer dynamoDBProxyServer;
  S3Mock s3proxyServer;
  private final AWSCredentials credentials = new BasicAWSCredentials("access", "secret");
  AmazonS3 amazonS3;

  public static String getAvailablePort() throws RuntimeException {
    try (final ServerSocket serverSocket = new ServerSocket(0)) {
      return String.valueOf(serverSocket.getLocalPort());
    } catch (IOException e) {
      throw new RuntimeException("Available port was not found", e);
    }
  }

  private void createBaseTable(DynamoDB dynamoDB) throws InterruptedException {

    Table table =
        dynamoDB.createTable(
            DbConstants.BASE_TABLE,
            Arrays.asList(
                new KeySchemaElement(DbAttributes.HASH_KEY, KeyType.HASH), // Partition
                // key
                new KeySchemaElement(DbAttributes.SORT_KEY, KeyType.RANGE)), // Sort key
            Arrays.asList(
                new AttributeDefinition(DbAttributes.HASH_KEY, ScalarAttributeType.S),
                new AttributeDefinition(DbAttributes.SORT_KEY, ScalarAttributeType.S)),
            new ProvisionedThroughput(10L, 10L));
    try {
      table.waitForActive();
    } catch (InterruptedException e) {
      throw new InterruptedException(
          "[TestDbHelper.createBaseTable()] table.waitForActive() interrupted");
    }
  }

  public void SetUpDynamoDB() throws Exception {
    // setup DynamoDb Local
    System.setProperty("sqlite4java.library.path", "native-libs");
    String port = getAvailablePort();
    dynamoDBProxyServer =
        ServerRunner.createServerFromCommandLineArgs(new String[] {"-inMemory", "-port", port});
    dynamoDBProxyServer.start();

    AmazonDynamoDB awsDynamoDb =
        AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                    "http://localhost:" + port, DbConstants.DYNAMO_DB_REGION.getName()))
            .build();

    DynamoDB dynamoDB = new DynamoDB(awsDynamoDb);

    // set DynamoDb local to be used in main production code
    Table table = dynamoDB.getTable(DbConstants.BASE_TABLE);

    createBaseTable(dynamoDB);
    TarrieDynamoDb.setDbForTesting(awsDynamoDb, dynamoDB);
  }

  public void TearDownDynamoDb() throws Exception {
    if (dynamoDBProxyServer != null) {
      dynamoDBProxyServer.stop();
    }
  }

  /// https://www.linkedin.com/pulse/integration-testing-aws-dynamodb-s3-spring-anmol-bhatia/
  public void SetUpS3() {
    String port = getAvailablePort();

    s3proxyServer =
        new S3Mock.Builder().withPort(Integer.parseInt(port)).withInMemoryBackend().build();

    s3proxyServer.start(); // Start the Mock S3 server locally on available port

    amazonS3 =
        AmazonS3ClientBuilder.standard()
            .withPathStyleAccessEnabled(true)
            .withCredentials(
                new AWSStaticCredentialsProvider(
                    new AnonymousAWSCredentials())) // use anonymous credentials.
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                    "http://localhost:" + port, DbConstants.S3_REGION.getName()))
            .build();

    amazonS3.createBucket(DbConstants.IMG_S3_BUCKET);

    TarrieS3.setDbForTesting(amazonS3);
  }

  public void TearDownS3() throws Exception {
    if (s3proxyServer != null) {
      amazonS3.shutdown();
      s3proxyServer.stop();
    }
  }
}
