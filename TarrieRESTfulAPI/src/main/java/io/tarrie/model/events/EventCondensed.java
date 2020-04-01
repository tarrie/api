package io.tarrie.model.events;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.model.*;
import io.tarrie.model.constants.CharacterLimit;
import io.tarrie.model.events.Event;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@ApiModel
public class EventCondensed{


    private String id;
    private String idCopy;
    private EventPrivacy privacy;
    private String name;
    private String imgPath;
    private Location loc;
    private String startTime;
    private String endTime;
    private Entity hostInfo;
    private Set<String> hashTags;
    private int rsvpNum;
    private Set<String> coordinators;


    /* ********* getters ***********/
    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    @ApiModelProperty(notes = "The unique identifier for the event")
    @NotNull
    public String getId() {
        return id;
    }

    @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
    public String getIdCopy() {
        return idCopy;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.RSVP_NUM)
    @ApiModelProperty(notes = "the num of rsvps to the event")
    public int getRsvpNum() {
        return rsvpNum;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.RSVP_NUM)
    @ApiModelProperty(notes = "the id's of the coordinators of the event")
    public Set<String> getCoordinators() {
        return coordinators;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.LOC)
    @ApiModelProperty(notes = "the location of event")
    @NotNull
    public Location getLoc() {
        return loc;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    @ApiModelProperty(notes = "url on S3 that holds the events image")
    @NotNull
    public String getImgPath() {
        return imgPath;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.NAME)
    @ApiModelProperty(notes = "The name of the event")
    @Size(min = 1, max = CharacterLimit.SMALL)
    @NotNull
    public String getName() {
        return name;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.END_TIME)
    @ApiModelProperty(notes = "end time of the event")
    public String getEndTime() {
        return endTime;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.HOST_INFO)
    @ApiModelProperty(notes = "the entity hosting the event. Either a User or a Group")
    @NotNull
    public Entity getHostInfo() {
        return hostInfo;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.DATA)
    @ApiModelProperty(notes = "start time of the event")
    @NotNull
    public String getStartTime() {
        return startTime;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.HASH_TAGS)
    @ApiModelProperty(notes = "hash tags associated with event")
    public Set<String> getHashTags() {
        return hashTags;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.EVENT_PRIVACY)
    @ApiModelProperty(notes = "The privacy specifications of the events")
    @NotNull
    public EventPrivacy getPrivacy() {
        return privacy;
    }


    /* ********* setters ***********/
    public void setIdCopy(String idCopy) {
        this.idCopy = idCopy;
    }

    public void setHashTags(Set<String> hashTags) {
        this.hashTags = hashTags;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setRsvpNum(int rsvpNum) {
        this.rsvpNum = rsvpNum;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setPrivacy(EventPrivacy privacy) {
        this.privacy = privacy;
    }

    public void setCoordinators(Set<String> coordinators) { this.coordinators = coordinators; }
}
