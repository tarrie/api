package io.tarrie.model.messages.invitesAndRequests;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.Entity;
import io.tarrie.model.Group;
import io.tarrie.model.TextMessage;
import io.tarrie.model.events.Event;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
public class From {
    private String messageType;
    private TextMessage textMessage;
    private Entity senderInfo;
    private String timeStamp;
    private String receiverId;
    private Event eventInfo;
    private String hasSeenAndTimeStamp;
    private Group groupInfo;

    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    public String getReceiverId() {
        return receiverId;
    }

    // String.format("%s#%s","FROM", timeStamp)
    @DynamoDBRangeKey(attributeName=DbAttributes.SORT_KEY)
    public String getTimeStamp() {
        return timeStamp;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.TEXT_MSG)
    public TextMessage getTextMessage() { return textMessage; }

    @DynamoDBAttribute(attributeName = DbAttributes.GROUP_INFO)
    public Group getGroupInfo() {
        return groupInfo;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.MSG_TYPE)
    public String getMessageType() { return messageType; }

    @DynamoDBAttribute(attributeName =DbAttributes.SENDER_INFO)
    public Entity getSenderInfo() { return senderInfo; }

    //  String.format("%d#%s",hasSeen, timeStamp)
    @DynamoDBAttribute(attributeName=DbAttributes.DATA)
    public String getHasSeenAndTimeStamp() {
        return hasSeenAndTimeStamp;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.EVENT_INFO)
    public Event getEventInfo() {
        return eventInfo;
    }

    public void setTextMessage(TextMessage textMessage) { this.textMessage = textMessage; }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setEventInfo(Event eventInfo) {
        this.eventInfo = eventInfo;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setSenderInfo(Entity senderInfo) {
        this.senderInfo = senderInfo;
    }

    public void setHasSeenAndTimeStamp(String hasSeenAndTimeStamp) {
        this.hasSeenAndTimeStamp = hasSeenAndTimeStamp;
    }

    public void setGroupInfo(Group groupInfo) {
        this.groupInfo = groupInfo;
    }
}
