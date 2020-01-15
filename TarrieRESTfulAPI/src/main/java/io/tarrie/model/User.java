package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.Utility;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.condensed.EntityCondensed;
import io.tarrie.model.condensed.GroupCondensed;
import io.tarrie.model.events.EventCondensed;
import io.tarrie.model.constants.CharacterLimit;
import io.tarrie.model.events.Event;
import io.tarrie.model.messages.invitesAndRequests.From;
import io.tarrie.model.messages.invitesAndRequests.To;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel(description = "A Tarrie user")
public class User {
    private String id;
    private String idCopy;
    private  String imgPath;
    private String name;
    private String emailAddr;
    private Date createdTime;
    private List<EntityCondensed> contacts;
    private List<EntityCondensed> following;
    private List<GroupCondensed> memberships;
    private List<EventCondensed> hostedEvents;
    private List<EventCondensed> savedEvents;
    private List<EntityCondensed> rsvpEvents;
    private List<From> messagesFrom;
    private List<To> messagesTo;
    // ToDo: List of conversations

    /* ********* getters ***********/

    // Hash key - primary key
    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    @ApiModelProperty(notes = "The unique identifier for the user", example = "USR#beck_b_123")
    @NotNull
    @Size(min=1, max= CharacterLimit.SMALL)
    public String getId() {
        return id;
    }

    // range key
    @DynamoDBRangeKey(attributeName=DbAttributes.SORT_KEY)
    public String getIdCopy() {
        return idCopy;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.NAME)
    @ApiModelProperty(notes = "the username")
    @NotNull
    @Size(min=1, max= CharacterLimit.SMALL)
    public String getName() {
        return name;
    }


    @DynamoDBAttribute(attributeName = DbAttributes.DATA)
    @ApiModelProperty(notes = "Email address of the user")
    @NotNull
    public String getEmailAddr() {
        return emailAddr;
    }


    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    @ApiModelProperty(notes = "url that holds user profile pic")
    @NotNull
    public String getImgPath() {
        return imgPath;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.CREATED_TIME)
    @ApiModelProperty(notes = "the date/time user was created", hidden = true)
    @NotNull
    public Date getCreatedTime() { return createdTime; }

    @DynamoDBIgnore
    @ApiModelProperty(notes = "The contacts of the user")
    public List<EntityCondensed> getContacts() {
        return contacts;
    }
    @DynamoDBIgnore
    @ApiModelProperty(notes = "The entities that the user is following")
    public List<EntityCondensed> getFollowing() {
        return following;
    }
    @DynamoDBIgnore
    @ApiModelProperty(notes = "The entities that the user is following")
    public List<EntityCondensed> getRsvpEvents() {
        return rsvpEvents;
    }
    @DynamoDBIgnore
    @ApiModelProperty(notes = "The events that the user is hosting")
    public List<EventCondensed> getHostedEvents() {
        return hostedEvents;
    }
    @DynamoDBIgnore
    @ApiModelProperty(notes = "The events that the user has saved")
    public List<EventCondensed> getSavedEvents() {
        return savedEvents;
    }
    @DynamoDBIgnore
    @ApiModelProperty(notes = "The messages from the user")
    public List<From> getMessagesFrom() {
        return messagesFrom;
    }
    @DynamoDBIgnore
    @ApiModelProperty(notes = "The groups that the user is a member of")
    public List<GroupCondensed> getMemberships() {
        return memberships;
    }

    @DynamoDBIgnore
    @ApiModelProperty(notes = "The messages to the user")
    public List<To> getMessagesTo() {
        return messagesTo;
    }

    /* ********* setters ***********/

    public void setEmailAddr(String email) throws AddressException {
        emailAddr= Utility.getEmailAddressFromString(email).toString();
    }

    public void setSavedEvents(List<EventCondensed> savedEvents) {
        this.savedEvents = savedEvents;
    }

    public void setRsvpEvents(List<EntityCondensed> rsvpEvents) {
        this.rsvpEvents = rsvpEvents;
    }

    public void setMessagesTo(List<To> messagesTo) {
        this.messagesTo = messagesTo;
    }

    public void setMessagesFrom(List<From> messagesFrom) {
        this.messagesFrom = messagesFrom;
    }

    public void setMemberships(List<GroupCondensed> memberships) {
        this.memberships = memberships;
    }

    public void setFollowing(List<EntityCondensed> following) {
        this.following = following;
    }

    public void setContacts(List<EntityCondensed> contacts) {
        this.contacts = contacts;
    }

    public void setHostedEvents(List<EventCondensed> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    public void setIdCopy(String idCopy) {
        this.idCopy = idCopy;
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

    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
