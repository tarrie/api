package io.tarrie.model.events;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.Entity;
import io.tarrie.model.EventPrivacy;
import io.tarrie.model.Location;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
public class HostEvent extends Event {
    private String hostId;
    private String eventId;
    private String startTime;
    private Entity hostInfo;
    private String endTime;
    private String name;
    private String imgPath;
    private Location loc;


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

    @Override
    @DynamoDBAttribute(attributeName = DbAttributes.START_TIME)
    public String getStartTime() {
        return startTime;
    }

    @Override
    @DynamoDBAttribute(attributeName = DbAttributes.HOST_INFO)
    public Entity getHostInfo() {
        return hostInfo;
    }

    @Override
    @DynamoDBAttribute(attributeName = DbAttributes.END_TIME)
    public String getEndTime() {
        return endTime;
    }

    @Override
    @DynamoDBAttribute(attributeName = DbAttributes.NAME)
    public String getName() {
        return name;
    }

    @Override
    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    public String getImgPath() {
        return imgPath;
    }

    @Override
    @DynamoDBAttribute(attributeName = DbAttributes.LOC)
    public Location getLoc() {
        return loc;
    }

    /* ********** Setters *************/
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setHostInfo(Entity hostInfo) {
        this.hostInfo = hostInfo;
    }

    @Override
    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

}
