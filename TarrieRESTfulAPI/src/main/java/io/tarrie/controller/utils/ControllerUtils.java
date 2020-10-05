package io.tarrie.controller.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import io.tarrie.database.TarrieAppSync;
import io.tarrie.database.TarrieDynamoDb;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.database.exceptions.*;
import io.tarrie.model.events.Event;
import io.tarrie.model.events.EventRelationship;
import io.tarrie.model.events.HostEvent;
import io.tarrie.utilities.Utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {
  /**
   * Send all the user/groups associated with event update that event has changed
   *
   * @param listOfRelationships
   * @param event
   * @throws MalformedInputException
   * @throws HttpCloseException
   * @throws IOException
   * @throws HttpErrorCodeException
   * @throws URISyntaxException
   * @throws HttpResponseException
   * @throws ProcessingException
   */
  public static void sendEventRelationshipUpdate(
      Collection<HostEvent> listOfRelationships, Event event)
      throws MalformedInputException, HttpCloseException, HttpErrorCodeException,
          URISyntaxException, HttpResponseException, ProcessingException {

    // loop over the relationships, add the start time, and editEventRelationship
    for (HostEvent hostEvent : listOfRelationships) {
      hostEvent.setData(event.getStartTime());
      TarrieAppSync.editEventRelationship(hostEvent);
    }
  }

  /**
   * Queries dynamoDb using GSI_1 to get all entities that have a relationship to an event
   *
   * @param eventId: The id of the event in question
   * @param relationship: The relationship
   * @return The collection of all
   */
  public static Collection<HostEvent> getAllEventRelationshipForEvent(
      String eventId, EventRelationship relationship) {

    String relationshipSortKey = Utility.eventIdToEventRelationship(eventId, relationship);

    // load the info of the creator of the event - queried from DynamoDb
    DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);

    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
    expressionAttributeValues.put(
        ":hashKey_value", new AttributeValue().withS(relationshipSortKey));

    // Based on: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GSI.html
    DynamoDBQueryExpression<HostEvent> queryExpression =
        new DynamoDBQueryExpression<HostEvent>()
            .withKeyConditionExpression(String.format("%s = :hashKey_value", DbAttributes.SORT_KEY))
            .withExpressionAttributeValues(expressionAttributeValues)
            .withIndexName(DbConstants.GSI_1)
            .withProjectionExpression(
                String.format("%s, %s", DbAttributes.SORT_KEY, DbAttributes.HASH_KEY));

    queryExpression.setConsistentRead(false); // cannot use consistent read on GSI
    return mapper.query(HostEvent.class, queryExpression);
  }
}
