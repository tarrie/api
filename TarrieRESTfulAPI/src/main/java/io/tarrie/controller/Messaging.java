package io.tarrie.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTransactionWriteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import io.tarrie.database.TarrieDynamoDb;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.MessageType;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.Entity;
import io.tarrie.model.Group;
import io.tarrie.model.Invite;
import io.tarrie.model.TextMessage;
import io.tarrie.model.events.Event;
import io.tarrie.model.messages.EntityInvited;
import io.tarrie.model.messages.invitesAndRequests.From;
import io.tarrie.model.messages.invitesAndRequests.To;

import java.time.Instant;

 class Messaging {
    /**
     * A user is requesting to join a group
     */
    public static void sendGroupJoin(Entity sender, Entity receiver, TextMessage textMessage) throws MalformedInputException {
        sendMessage(MessageType.groupJoin, sender,  receiver, null, null, textMessage);
    }

  /**
   * A group/user is inviting another group/user to attend a event
   *
   */
  public static void sendEventInvite(
      Entity sender, Entity receiver, Event eventInfo, TextMessage textMessage) throws MalformedInputException {
        sendMessage(MessageType.eventInvite, sender,  receiver, eventInfo, null, textMessage);

    }

    /**
     * A group/user is inviting a user to join a group
     * @param sender
     * @param receiver
     * @param groupInfo
     * @param textMessage
     */
    public static void sendGroupInvite(Entity sender, Entity receiver,Group groupInfo, TextMessage textMessage) throws MalformedInputException {

        sendMessage(MessageType.groupInvite, sender,  receiver, null, groupInfo, textMessage);
    }
    /**
     * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.Transactions.html
     * @param messageType
     * @param sender
     * @param receiver
     * @param eventInfo
     * @param textMessage
     */
     static void sendMessage(MessageType messageType, Entity sender, Entity receiver, Event eventInfo, Group groupInfo, TextMessage textMessage) throws MalformedInputException {

         DynamoDBTransactionWriteExpression conditionExpressionForConditionCheck = new DynamoDBTransactionWriteExpression()
                 .withConditionExpression(String.format("attribute_not_exists(%s) AND attribute_not_exists(%s)", DbAttributes.HASH_KEY,DbAttributes.SORT_KEY));

        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
        Instant instant = Instant.now();

        // add transactions
        From messageFrom = sendFromMessage(instant,messageType, sender,receiver,eventInfo,groupInfo,textMessage);
        To messageTo = sendToMessage(instant,messageType, sender.getId(),receiver,eventInfo,textMessage);
        transactionWriteRequest.addPut(messageTo,conditionExpressionForConditionCheck);
        transactionWriteRequest.addPut(messageFrom,conditionExpressionForConditionCheck);

        if ((messageType == MessageType.eventInvite)){
            transactionWriteRequest.addPut(addInvite(eventInfo,receiver),conditionExpressionForConditionCheck);
        }

        if ((messageType == MessageType.groupInvite)){
            transactionWriteRequest.addPut(addInvite(groupInfo,receiver),conditionExpressionForConditionCheck);

        }

        // execute transactions.
        TarrieDynamoDb.executeTransactionWrite(transactionWriteRequest);
    }

     /**
      * THe Event keeps track of everyone invited
      * @param event
      * @param receiver
      * @return
      * @throws MalformedInputException
      */
    private static Invite addInvite(Event event, Entity receiver) throws MalformedInputException {
        if (event==null || receiver==null){
            throw new MalformedInputException("Missing required input. Cant invite to event if reciever or event is null");
        }

        Invite invite = new Invite();
        invite.setEntityId(event.getId());
        invite.setInvitedId(String.format("INVITED#%s",receiver.getId()));

        return invite;
    }

         /**
      * THe Group keeps track of everyone invited
      * @param group
      * @param receiver
      * @return
      * @throws MalformedInputException
      */
    private static Invite addInvite(Group group, Entity receiver) throws MalformedInputException {
        if (group==null || receiver==null){
            throw new MalformedInputException("Missing required input. Cant invite to group if group or reciever is null");
        }

        Invite invite = new Invite();
        invite.setEntityId(group.getId());
        invite.setInvitedId(receiver.getId());

        return invite;
    }

    /**
     * Formats a message timestamp
     * @param fromOrTo "FROM" or "TO" anything else will throw a error
     * @param time time instance
     * @return formatted time stamp
     */
    private static String formatTimeStamp(String fromOrTo,Instant time) throws MalformedInputException {
         if (fromOrTo.equals("FROM") || fromOrTo.equals("TO")){
             return String.format("%s#%s", fromOrTo, time.toString());
         }else{
             throw new MalformedInputException("fromOrTo must be in the set {FROM,TO}");
         }
    }

    /**
     * Formats the hasSeen field to include the timestamp as well so sorting is possible
     * @param isSeen boolean indicating if message has been seen yet
     * @param time time the message was sent/received
     * @return formatted hasSeenAndTimeStamp
     */
    private static String hasSeenAndTimeStamp(boolean isSeen, Instant time){
        return String.format("%d#%s",isSeen ? 1 : 0,time.toString());
    }


    private static From sendFromMessage(Instant time, MessageType messageType, Entity sender, Entity receiver, Event eventInfo, Group groupInfo, TextMessage textMessage) throws MalformedInputException{
        From messageFrom  = new From();


        // formatted time stamp -- will be the range key
        String timeStamp = formatTimeStamp("FROM",time);
        String hasSeenAndTimeStamp = hasSeenAndTimeStamp(false,time);

        // attributes common among all FROM messages
        messageFrom.setSenderInfo(sender); // id, name, imgPath
        messageFrom.setReceiverId(receiver.getId());
        messageFrom.setTimeStamp(timeStamp);
        messageFrom.setHasSeenAndTimeStamp(hasSeenAndTimeStamp);
        messageFrom.setMessageType(messageType.name());
        if (textMessage !=null){
            messageFrom.setTextMessage(textMessage);
        }
        // a invite to a user to join a event
        switch (messageType){
            case eventInvite:
                if (eventInfo == null) {
                    throw new MalformedInputException("required non null input for FROM#eventInvite is a event. This input is null.");
                } else {
                    // startTime, endTime, title, imgPath, location, bio, rsvpNum,
                    messageFrom.setEventInfo(eventInfo);
                }
                break;
            case groupInvite:
                if (groupInfo == null) {
                    throw new MalformedInputException("required non null input for FROM#groupInvite is a group. This input is null.");
                } else {
                    // startTime, endTime, title, imgPath, location, bio, rsvpNum,
                    messageFrom.setGroupInfo(groupInfo);
                }
                break;
            default:
                break;
        }

        return messageFrom;
    }

    private static To sendToMessage(Instant time, MessageType messageType,String senderId, Entity receiver, Event eventInfo, TextMessage textMessage) throws MalformedInputException{
        String timeStamp =formatTimeStamp("TO",time);

        To messageTo = new To();
        messageTo.setSenderId(senderId);
        messageTo.setMessageType(messageType.name());
        messageTo.setReceiverInfo(receiver);  //id, name, imgPath
        messageTo.setTimeStamp(timeStamp);
        if (textMessage != null){
            messageTo.setTextMessage(textMessage);
        }

        if (messageType == MessageType.eventInvite){
            if (eventInfo != null){
                messageTo.setEventId(eventInfo.getId());
            }else{
                throw new MalformedInputException("required input for TO#eventInvite is a eventId. This input is missing.");
            }
        }

        return messageTo;
    }
}
