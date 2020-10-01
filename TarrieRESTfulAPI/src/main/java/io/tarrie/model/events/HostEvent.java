package io.tarrie.model.events;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.Entity;
import io.tarrie.model.EventPrivacy;
import io.tarrie.model.Location;
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
  private String hostId;
  private String eventId;
  private String startTime;
  private String endTime;
  private String name;
  private String imgPath;
  private Map location;
  private Set<String> coordinators;

  public String convertToJson()
      throws IllegalAccessException, NoSuchMethodException, JsonProcessingException,
          InvocationTargetException {
    Map mapPayload = Utility.pojoToMap(this);
    mapPayload.put("main_pk", this.getHostId());
    mapPayload.put("main_sk", this.getEventId());
    mapPayload.put("data", this.getStartTime());

    mapPayload.remove("hostId");
    mapPayload.remove("eventId");
    mapPayload.remove("startTime");

    return Utility.mapToJsonUnquotedFields(mapPayload);
  }

  /* ********** Getters *************/
  // partition key
  @JsonProperty(DbAttributes.HASH_KEY)
  @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
  public String getHostId() {
    return hostId;
  }

  // sort key
  @JsonProperty(DbAttributes.SORT_KEY)
  @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
  public String getEventId() {
    return eventId;
  }

  @JsonProperty(DbAttributes.EVENT_COORDINATORS)
  @DynamoDBAttribute(attributeName = DbAttributes.EVENT_COORDINATORS)
  public Set<String> getCoordinators() {
    return coordinators;
  }

  @JsonProperty(DbAttributes.DATA)
  @DynamoDBAttribute(attributeName = DbAttributes.DATA) // so we can sort on start time
  public String getStartTime() {
    return startTime;
  }

  @JsonProperty(DbAttributes.END_TIME)
  @DynamoDBAttribute(attributeName = DbAttributes.END_TIME)
  public String getEndTime() {
    return endTime;
  }

  @JsonProperty(DbAttributes.NAME)
  @DynamoDBAttribute(attributeName = DbAttributes.NAME)
  public String getName() {
    return name;
  }

  @JsonProperty(DbAttributes.IMG_PATH)
  @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
  public String getImgPath() {
    return imgPath;
  }

  @JsonSerialize(using = MapGraphQLSerializer.class)
  @JsonProperty(DbAttributes.LOC)
  @DynamoDBTypeConverted(converter = MapTypeConverted.class)
  @DynamoDBAttribute(attributeName = DbAttributes.LOC)
  public Map getLocation() {
    return location;
  }

  /* ********** Setters *************/

  public void setName(String name) {
    this.name = name;
  }

  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public void setLocation(Map location) {
    this.location = location;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  public void setCoordinators(Set<String> coordinators) {
    this.coordinators = coordinators;
  }
}
