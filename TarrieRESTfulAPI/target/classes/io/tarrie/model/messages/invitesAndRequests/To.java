package io.tarrie.model.messages.invitesAndRequests;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.Entity;
import io.tarrie.model.TextMessage;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
public class To {
    private String messageType;
    private TextMessage textMessage;
    private Entity receiverInfo; //id, name, imgPath
    private String eventId; // this is just for bookkeeping so don't need more info other than this
    private String senderId;
    private String timeStamp;


    @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
    public String getTimeStamp() {
        return timeStamp;
    }

    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    public String getSenderId() {
        return senderId;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.EVENT_ID)
    public String getEventId() {
        return eventId;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.MSG_TYPE)
    public String getMessageType() {
        return messageType;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.RECV_INFO)
    public Entity getReceiverInfo() {
        return receiverInfo;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.TEXT_MSG)
    public TextMessage getTextMessage() {
        return textMessage;
    }

    public void setReceiverInfo(Entity receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }
}
