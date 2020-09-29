package io.tarrie.model.events;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.swagger.annotations.ApiModel;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.Entity;
import io.tarrie.model.EventPrivacy;
import io.tarrie.model.Location;
import io.tarrie.utilities.MapTypeConverted;

import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel(description="This is what saved under the the creator of the event. ")
public class HostEvent{
    private String hostId;
    private String eventId;
    private String startTime;
    private Entity hostInfo;
    private String endTime;
    private String name;
    private String imgPath;
    private Map location;
    private Set<String> coordinators;


    /* ********** Getters *************/
    // partition key
    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    public String getHostId() {
        return hostId;
    }

    // sort key
    @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
    public String getEventId() {
        return eventId;
    }


    @DynamoDBAttribute(attributeName = DbAttributes.EVENT_COORDINATORS)
    public Set<String> getCoordinators() {
        return coordinators;
    }


    @DynamoDBAttribute(attributeName = DbAttributes.DATA) //so we can sort on start time
    public String getStartTime() {
        return startTime;
    }


    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    @DynamoDBAttribute(attributeName = DbAttributes.HOST_INFO)
    public Entity getHostInfo() {
        return hostInfo;
    }


    @DynamoDBAttribute(attributeName = DbAttributes.END_TIME)
    public String getEndTime() {
        return endTime;
    }


    @DynamoDBAttribute(attributeName = DbAttributes.NAME)
    public String getName() {
        return name;
    }


    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    public String getImgPath() {
        return imgPath;
    }

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


    public void setHostInfo(Entity hostInfo) {
        this.hostInfo = hostInfo;
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
