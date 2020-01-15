package io.tarrie.model.events;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.*;
import io.tarrie.model.constants.CharacterLimit;
import io.tarrie.model.constants.EventLimits;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * ToDo: Add support for polls, documents associated with event
 */
@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel(description="A Tarrie event")
public class Event {
    private String id;
    private String idCopy;
    private EventPrivacy privacy;
    private String name;
    private String imgPath;
    private Location loc;
    private String startTime;
    private String endTime;
    private boolean linkSharing;
    private String bio;

    private Entity hostInfo;// has to be queried
    private List<User> rsvps; // has to be queried
    private Set<String> hashTags;
    private List<String> invitedEntityIds; // has to be queried
    private List<SharableLink> sharableLinks; // has to be queried
    private int rsvpNum; //atomic counter

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

    @DynamoDBAttribute(attributeName = DbAttributes.LOC)
    @ApiModelProperty(notes = "the location of event")
    @NotNull
    public Location getLoc() {
        return loc;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.BIO)
    @ApiModelProperty(value = "The description of the event")
    @Size(min=0, max= CharacterLimit.LARGE)
    public String getBio() {
        return bio;
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
    @Size(max = DbConstants.HASH_TAG_PER_EVENT)
    public Set<String> getHashTags() {
        return hashTags;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.EVENT_PRIVACY)
    @ApiModelProperty(notes = "The privacy specifications of the events")
    @NotNull
    public EventPrivacy getPrivacy() {
        return privacy;
    }

    @DynamoDBIgnore // has to be queried
    @ApiModelProperty(value = "Collection of SharableLinks associated to the event",hidden = true)
    public List<SharableLink> getSharableLinks() {
        return sharableLinks;
    }

    @DynamoDBIgnore // has to be queried
    @ApiModelProperty(value = "Collection of entityIds invited to event")
    @NotNull
    public List<String> getInvitedEntityIds() {
        return invitedEntityIds;
    }

    @DynamoDBIgnore // has to be queried
    @ApiModelProperty(notes = "the user's that have rsvp'd (going to) to the event")
    @NotNull
    public List<User> getRsvps() {
        return rsvps;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.LINK_SHARING)
    @ApiModelProperty(value = "Boolean saying if link sharing is on or not(default is off)")
    public boolean isLinkSharing() {
        return linkSharing;
    }

    /* ********* setters ***********/
    public void setPrivacy(EventPrivacy privacy) {
        this.privacy = privacy;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setRsvpNum(int rsvpNum) {
        this.rsvpNum = rsvpNum;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setHostInfo(Entity hostInfo) {
        this.hostInfo = hostInfo;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHashTags(Set<String> hashTags) {
        this.hashTags = hashTags;
    }

    public void setInvitedEntityIds(List<String> invitedEntityIds) {
        this.invitedEntityIds = invitedEntityIds;
    }

    public void setLinkSharing(boolean linkSharing) {
        this.linkSharing = linkSharing;
    }

    public void setRsvps(List<User> rsvps) {
        this.rsvps = rsvps;
    }

    public void setSharableLinks(List<SharableLink> sharableLinks) {
        this.sharableLinks = sharableLinks;
    }

    public void setIdCopy(String idCopy) {
        this.idCopy = idCopy;
    }
}
