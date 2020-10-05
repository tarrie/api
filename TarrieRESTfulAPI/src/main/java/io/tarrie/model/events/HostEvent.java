package io.tarrie.model.events;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.utilities.MapGraphQLSerializer;
import io.tarrie.utilities.MapTypeConverted;
import io.tarrie.utilities.Utility;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel(description = "This is what saved under the the creator of the event. ")
public class HostEvent {
  String id;
  String eventId;
  Integer lastChangedCounter;
  String data;


  /* ********** Getters *************/
  // partition key
  @ApiModelProperty(value = "id of the user/group")
  @JsonProperty(DbAttributes.HASH_KEY)
  @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
  @DynamoDBIndexRangeKey(globalSecondaryIndexName = DbConstants.GSI_1)
  public String getId() {
    return id;
  }

  // sort key
  @ApiModelProperty(value = "Tells is hosting, rsvp, or save event and gives the eventId")
  @JsonProperty(DbAttributes.SORT_KEY)
  @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
  @DynamoDBIndexHashKey(globalSecondaryIndexName = DbConstants.GSI_1)
  public String getEventId() {
    return eventId;
  }

  @ApiModelProperty(value = "Keeps track of the last changed")
  @JsonProperty(DbAttributes.LAST_CHANGED_COUNTER)
  @DynamoDBAttribute(attributeName = DbAttributes.LAST_CHANGED_COUNTER)
    public Integer getLastChangedCounter() {
    return lastChangedCounter;
  }

  @ApiModelProperty(value = "StartTime of the event (LSK)")
  @JsonProperty(DbAttributes.DATA)
  @DynamoDBAttribute(attributeName = DbAttributes.DATA)
  public String getData() {
    return data;
  }

  @Override
  public String toString() {
    return String.format("HostEvent(main_pk=%s, main_sk=%s, data=%s)",id,eventId,data);
  }

  /* ********** Setters *************/

  public void setData(String data) throws MalformedInputException {
    try {
      Utility.isDateTimeValid(data);
      this.data = data;
    } catch (MalformedInputException e) {
      throw new MalformedInputException(String.format("[HostEvent] data not valid datetime format: %s",data));
    }
  }

  public void setId(String id) {
    this.id =id;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public void setLastChangedCounter(Integer lastChangedCounter) {
    this.lastChangedCounter = lastChangedCounter;
  }

}
