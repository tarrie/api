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
    private static final String AWS_SECRET_ACCESS_KEY_SQS = Utility.getParam("AWS_SECRET_ACCESS_KEY_SQS");
    private final Regions REGION = Regions.US_EAST_1;
    private  AmazonDynamoDB awsDynamoDb;

    public DynamoDb(){
        AWSCredentials credentials =
                new BasicAWSCredentials(AWS_ACCESS_KEY_ID_SQS, AWS_SECRET_ACCESS_KEY_SQS);

        awsDynamoDb = AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();



    }

    public void createTable(String tableName) throws InterruptedException {
        // Create an instance of the DynamoDB class.
        DynamoDB dynamoDB = new DynamoDB(awsDynamoDb);

        List<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));

        List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(6L));

        Table table = dynamoDB.createTable(request);

        table.waitForActive();

    }
}
