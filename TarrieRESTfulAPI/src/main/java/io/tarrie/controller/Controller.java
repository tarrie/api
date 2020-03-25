package io.tarrie.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.S3Link;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.google.common.collect.Lists;
import io.tarrie.Utility;
import io.tarrie.database.exceptions.TarrieGroupException;
import io.tarrie.model.*;
import io.tarrie.model.condensed.UserCondensed;
import io.tarrie.model.constants.EventRelationship;
import io.tarrie.model.constants.MembershipType;
import io.tarrie.model.consumes.CreateEvent;
import io.tarrie.model.consumes.CreateGroup;
import io.tarrie.model.consumes.CreateUser;
import io.tarrie.model.condensed.EntityCondensed;
import io.tarrie.database.TarrieDynamoDb;
import io.tarrie.database.TarrieS3;
import io.tarrie.database.contants.*;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.events.Event;
import io.tarrie.model.events.HostEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.imageio.ImageIO;
import javax.mail.internet.AddressException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {
  private static final Logger LOG = LogManager.getLogger(Controller.class);

  public static void main(String[] args) throws AddressException, IOException, MalformedInputException {
    Controller controller = new Controller();

    UserCondensed owner = new UserCondensed();
    owner.setId("USR#jide69");
    owner.setImgPath("https://s3.us-east-2.amazonaws.com/tarrie.io/users/pictures/jide69/profile.jpeg");
    owner.setName("Jide");

      Location loc = new Location();
      loc.city="Evanston";
      loc.state="IL";
      loc.locName="Northwestern University";

    CreateGroup group = new CreateGroup();
      group.setGroupId("boogoParty");
        group.setOwner(owner);
        group.setName("Boogo Party");
      group.setBio("Dude's who like to party");

      deleteUser("USR#becky_b1998");

      Optional.of(MembershipType.OWNER);
       // group.setLocation(loc);

     // controller.createGroup(group);



      //controller.createUser(newUser);

      //addConnection("USR#jide69","GRP#boogoParty", ConnectionType.Member, Optional.of(MembershipType.OWNER));
      //addConnection("USR#northwestern_69","GRP#boogoParty", ConnectionType.Member, Optional.empty());

      System.out.println(getMembershipTypeOfUser("USR#jide69","GRP#boogoParty"));
      System.out.println(getMembershipTypeOfUser("USR#northwestern_69","GRP#boogoParty"));

      addConnection("USR#northwestern_69","GRP#boogoParty", ConnectionType.Member, Optional.of(MembershipType.ADMIN));
      System.out.println(getMembershipTypeOfUser("USR#northwestern_69","GRP#boogoParty"));

      transferGroupOwner("USR#jide69" , "USR#northwestern_69", "GRP#boogoParty", MembershipType.NULL);


      //DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond)
      DateTime dt = new DateTime(2004, 12, 25, 12, 0, 0, 0)
              .withZone(DateTimeZone.UTC);
      System.out.println(dt.toDateTimeISO());

      System.out.println(DateTime.parse("2020-01-06T02:42:15.055Z"));

      //System.out.println(Utility.isDateTimeValid("2020-01-06T02:42:15.055Z"));
      //System.out.println(Utility.isDateTimeValid("2020-01-06T02:42:15.05x5Z"));

      System.out.println(Utility.generateRandomString(10));

      // File initialFile = new File("../pictures/coolpic.jpeg");
    // InputStream targetStream = new FileInputStream(initialFile);
    // controller.uploadProfileImg(targetStream,"image/jpeg","USR#jide69");

    //ArrayList<String> testString = new ArrayList<>();
    //testString.add("USR#jide69");
    //testString.add("USR#becky_b1998");
    //testString.add("USR#becky_b199.8_");
   // testString.add("USR#becky_b199.8_BULLSHIT");

    // System.out.println(controller.userExists(testString));
    // System.out.println(isIdUnique("USR#becky_b199.8_BULLSHIT"));

      /*
    sendMessageToInbox(
        "USR#jide69",
        "Jide",
        "https://s3.us-east-2.amazonaws.com/tarrie.io/users/pictures/jide69/profile.jpeg",
        "USR#becky_b1998",
        "what's good?");*/
  }

  /**
   * Checks if a tarrieId is unique
   *
   * @param tarrieId tarrie id example: USR#becky123
   * @return true if unique
   */
  private static boolean isIdUnique(String tarrieId) {
    return !TarrieDynamoDb.doesItemExist(tarrieId);
  }

    /**
     * Checks if a tarrieId is valid. Valid id's are strings that contain alphanumeric characters, ".", or "_"
     * @param id the id to check
     * @return boolean indicating if tarrieId is valid
     */
  public static boolean isGroupIdOrUserIdValid(String id){
      // get the actualId
      Pattern pattern = Pattern.compile(String.format("(%s#|%s#|%s#)?(?<idActual>.*)",EntityType.GROUP,EntityType.USER, EntityType.EVENT));
      Matcher matcher = pattern.matcher(id);

      if (matcher.find()){
          String rawId = matcher.group("idActual");
          return rawId.matches("[A-Za-z0-9/._]+");
      }else{
          return false;
      }

  }

    /**
     * Checks if a newId is valid
     * @param newTarrieId the id to check
     * @return true if the newTarrieId has correct format and is unique
     */
  public static boolean isNewIdValid(String newTarrieId){
      return isGroupIdOrUserIdValid(newTarrieId) && isIdUnique(newTarrieId);
  }


  /**
   * Creates a tarrie user
   *
   * @param user the user to create
   * @throws AddressException thrown if email address is malformed
   * @throws MalformedInputException thrown if userId is not alphanumeric
   */
  public static void createUser(CreateUser user) throws AddressException, MalformedInputException, IOException {
    // format the userId
    String formattedUserId = String.format("%s#%s", EntityType.USER,user.getId() );

    if (isNewIdValid(formattedUserId)){
        user.setId(formattedUserId);
        user.setIdCopy(formattedUserId);
        DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);
        mapper.save(user);
        // add the default img to the user's newly created image path
        //uploadProfileImg(Utility.getInputStreamFromS3ImgUrl(DbConstants.DEFAULT_USER_IMG), ImgTypes.JPG, formattedUserId);

        LOG.info(String.format("Created new user: %s", formattedUserId));
    }else{
        throw new MalformedInputException("userId already exists or is malformed: " + formattedUserId);
    }

  }

    /**
     * Creates a tarrie group
     * @param createGroup the group to create
     */
    public static void createGroup(CreateGroup createGroup) throws MalformedInputException {
        // format the groupId
        String formattedGroupId = String.format("%s#%s", EntityType.GROUP,createGroup.getGroupId());
        String ownerId = createGroup.getOwner().getId();

        // check if valid then create group
        if (isNewIdValid(formattedGroupId) && TarrieDynamoDb.doesItemExist(ownerId)){
            createGroup.setGroupId(formattedGroupId);
            createGroup.setGroupIdCopy(formattedGroupId);
            DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);

            // save group info
            mapper.save(createGroup);
            // set owner as owner of the group
            addConnection(ownerId,formattedGroupId, ConnectionType.Member, Optional.of(MembershipType.OWNER));

            LOG.info(String.format("Created new group: [groupId=%s,ownerId=%s]", formattedGroupId,ownerId));
        }else{

            throw new MalformedInputException(String.format("groupId already exists, or groupId is malformed, or ownerId does not exist: [groupId=%s,ownerId=%s]",formattedGroupId,ownerId));
        }
    }





    /**
     * Creates a tarrie event
     */
    public void createEvent(CreateEvent createEvent) throws MalformedInputException{
        String creatorType = Utility.getEntityType(createEvent.getCreatorId());
        if (creatorType.equals(EntityType.EVENT))
            throw new MalformedInputException("Illegal logic, a event can't create another event");
        if (createEvent.getHashTags().size()> DbConstants.HASH_TAG_PER_EVENT)
            throw new MalformedInputException("Only "+(DbConstants.HASH_TAG_PER_EVENT)+ "hashtags allowed in event");


        // format the event id -- will be uploaded to DynamoDb
        String eventId = String.format("%s#%s",EntityType.EVENT, Utility.generateUniqueId());
        createEvent.setEventId(eventId);
        createEvent.setEventIdCopy(eventId);

        // load the info of the creator of the event - queried from DynamoDb
        DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);
        Entity creator  = mapper.load(Entity.class,createEvent.getCreatorId(),createEvent.getCreatorId());

        // reduced host info
        Entity hostInfo = new Entity();
        hostInfo.setName(creator.getName());
        hostInfo.setImgPath(creator.getImgPath());
        hostInfo.setId(creator.getId());

        // format hashtags
        Set<String> hashTagSet =createEvent.getHashTags().stream()
                .map(String::valueOf).map(s-> String.format("%s%s", DbConstants.HASH_TAG, s))
                .collect(Collectors.toSet());


        // creating the HostEvent object for the creator of the event-- will be uploaded to DynamoDb
        HostEvent hostEvent = new HostEvent();
        hostEvent.setHostId(creator.getId());
        hostEvent.setEventId(String.format("%s#%s", EventRelationship.HOST, eventId));
        hostEvent.setEndTime(createEvent.getEndTime());
        hostEvent.setStartTime(createEvent.getStartTime());
        hostEvent.setImgPath(createEvent.getImgPath());
        hostEvent.setLoc(createEvent.getLocation());
        hostEvent.setName(createEvent.getName());
        hostEvent.setHostInfo(hostInfo);
        mapper.save(hostEvent);

        // create the actual event object -- will be uploaded to DynamoDb
        Event newEvent = new Event();
        newEvent.setId(eventId);
        newEvent.setIdCopy(eventId);
        newEvent.setBio(createEvent.getBio());
        newEvent.setEndTime(createEvent.getEndTime());
        newEvent.setStartTime(createEvent.getStartTime());
        newEvent.setImgPath(createEvent.getImgPath());
        newEvent.setLoc(createEvent.getLocation());
        newEvent.setName(createEvent.getName());
        newEvent.setHostInfo(hostInfo);
        newEvent.setHashTags(hashTagSet);
        newEvent.setLinkSharing(false);
        newEvent.setPrivacy(createEvent.getEventPrivacy());
        newEvent.setRsvpNum(creatorType.equals(EntityType.USER)? 1 : 0);
        mapper.save(newEvent);

        Event eventCondensed = new Event();
        eventCondensed.setId(eventId);
        eventCondensed.setEndTime(createEvent.getEndTime());
        eventCondensed.setStartTime(createEvent.getStartTime());
        eventCondensed.setImgPath(createEvent.getImgPath());
        eventCondensed.setLoc(createEvent.getLocation());
        eventCondensed.setName(createEvent.getName());

        // invite the entities in the list
        for (String id: createEvent.getInvitedEntityIds()){
            Messaging.sendEventInvite(hostInfo, mapper.load(Entity.class,id,id),eventCondensed, null);
        }




        // generate the hashtag entries.
        HashTags.inputHashTag(hashTagSet,eventId );
    }

    public void inviteEntitiesToEvent(String inviterId, List<String> invitees){

    }

    /**
   * Uploads a profile image to S3 and the resultant path to DynamoDb
   *
   * @param is file input stream
   * @param mimeType the mimeType for example: image/png
   * @param entityId the entityId for example: USR#becky123
   * @throws IOException
   * @throws MalformedInputException
   */
  public static void uploadProfileImg(InputStream is, String mimeType, String entityId)
      throws IOException, MalformedInputException {

      if (TarrieDynamoDb.doesItemExist(entityId)){
          String s3Url = TarrieS3.uploadProfileImg(is, mimeType, entityId);
          TarrieDynamoDb.updateAttribute(entityId, DbAttributes.IMG_PATH, s3Url);
      }else{
          throw new MalformedInputException("entity does not exist: "+ entityId);
      }

  }


  /**
   * Checks if a subset of userIds exist
   *
   * @param userIds the userIds to check
   * @return subset of userIds that exist
   */
  public List<String> userExists(List<String> userIds) throws MalformedInputException {
    // Batch request can only be 100 at a time so partition the input into chunks of 100
    List<List<String>> partition = Lists.partition(userIds, 100);

    List<String> idsFound = new ArrayList<>();
    for (List<String> batch : partition) {
      idsFound.addAll(TarrieDynamoDb.doItemsExist(batch));
    }
    return idsFound;
  }

  public static void deleteUser(String tarrieId){

      // delete all user info


        // if owner then transfer ownership if there exist a admin,  otherwise delete group
           // update the admins entry

      Table table = TarrieDynamoDb.dynamoDB.getTable(DbConstants.BASE_TABLE);

      // valueMap provides value substitution. This is used because you can't use literals in any expression, including KeyConditionExpression.
      String valSubId = ":idValue";
      HashMap<String, Object> valueMap = new HashMap<String, Object>();
      valueMap.put(valSubId, tarrieId);

      // query table to get the items to delete
      QuerySpec querySpec = new QuerySpec()
              .withKeyConditionExpression(String.format("%s = %s",DbAttributes.HASH_KEY,valSubId ))
              .withValueMap(valueMap)
              .withProjectionExpression(String.format("%s, %s",DbAttributes.HASH_KEY,DbAttributes.SORT_KEY, DbAttributes.DATA))
              .withMaxPageSize(4);
      ItemCollection<QueryOutcome> items = table.query(querySpec);

      //List<Item> hostedEvents = new ArrayList<>();
      //List<Item> ownedGroups = new ArrayList<>();
      //for (Item item: items){

      //}
      // delete the items
      TarrieDynamoDb.batchWriteOutcome(items);


    }

    /**
     * Returns the membership type (str) of a userId in a groupId
     * @param userId to check
     * @param groupId to check against
     * @return null if not a member otherwise MEMBER, OWNER, or ADMIN
     */
    public static MembershipType getMembershipTypeOfUser(String userId,  String groupId) throws MalformedInputException{

        if (!(Utility.getEntityType(userId).equals(EntityType.USER))){
            return MembershipType.NULL;
        }

        //nameMap provides name substitution. This is used because 'data' is a reserved word in Amazon DynamoDB.
        // You can't use it directly in any expression
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#memType", DbAttributes.DATA);

        // query the table
        GetItemSpec spec =
                new GetItemSpec()
                        .withPrimaryKey(
                                DbAttributes.HASH_KEY, userId, DbAttributes.SORT_KEY, groupId)
                        .withProjectionExpression("#memType")
                .withNameMap(nameMap);
        Item item = TarrieDynamoDb.dynamoDB.getTable(DbConstants.BASE_TABLE).getItem(spec);

        if (item==null){
            return MembershipType.NULL;
        }else{
            String rawMemType = item.get(DbAttributes.DATA).toString();

            // ensure that the user is in fact a group member
            if (rawMemType.contains(MembershipType.MEMBER.name())){

                // parse the resultant string to find the membership type
                Pattern pat = Pattern.compile(String.format(".*(?<memType>%s|%s).*",
                        MembershipType.OWNER.name(),
                        MembershipType.ADMIN.name()));
                Matcher matcher = pat.matcher(rawMemType);
                if (matcher.find()){
                    return MembershipType.valueOf(matcher.group("memType"));
                }else{
                    return MembershipType.MEMBER;
                }
            }else{
                return MembershipType.NULL;
            }
        }
    }

    /**
     * Transfers ownership from transferId to recipientId of group, groupId.
     * @param transferId old ownerId
     * @param recipientId new ownerId
     * @param groupId the group in question
     * @param newMemTypeOfOldOwner The new memType of the owner
     * @throws MalformedInputException if transferId is not owner of group or recipientId is not a member of the group
     */
    public static void transferGroupOwner(String transferId, String recipientId, String groupId, MembershipType newMemTypeOfOldOwner) throws MalformedInputException {

        // check if user is in fact the owner
        MembershipType memTypeOfTransfer = getMembershipTypeOfUser(transferId,groupId);
        MembershipType memTypeOfRecipient = getMembershipTypeOfUser(recipientId,groupId);

        if (memTypeOfTransfer != MembershipType.OWNER){
            throw new MalformedInputException("transferring user is not the owner of the group: "+transferId);
        }
        if (memTypeOfRecipient == MembershipType.NULL){
            throw new MalformedInputException("transferring ownership allowed within group only ");
        }
        if (newMemTypeOfOldOwner ==MembershipType.OWNER ){
            throw new MalformedInputException("there cannot be two owners of the group");
        }

        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();

        Connection updateMemberConnection = new Connection();
        updateMemberConnection.setEntityId(recipientId);
        updateMemberConnection.setRangeKey(groupId);
        updateMemberConnection.setData(String.format("%s#%s#%s",MembershipType.MEMBER.name(),MembershipType.OWNER.name(),recipientId ));
        transactionWriteRequest.addUpdate(updateMemberConnection);


        Connection updateOwnerConnection = new Connection();
        updateOwnerConnection.setEntityId(transferId);
        updateOwnerConnection.setRangeKey(groupId);


        if (newMemTypeOfOldOwner == MembershipType.NULL){
            transactionWriteRequest.addDelete(updateOwnerConnection);
        }else{
            updateOwnerConnection.setData(String.format("%s#%s#%s",MembershipType.MEMBER.name(),newMemTypeOfOldOwner.name(),transferId ));
            transactionWriteRequest.addUpdate(updateOwnerConnection);
        }

        // execute transaction
        TarrieDynamoDb.executeTransactionWrite(transactionWriteRequest);
    }

    /**
     * Follow a entity the connectionId must be of type user or event.
     * @param followerId
     * @param connectionId
     */
    public static void followEntity(String followerId,String connectionId ) throws MalformedInputException{
        addConnection(followerId,connectionId,ConnectionType.Following,Optional.empty());
    }

    /**
     * Adds a contact to a entityId contact lsit
     * @param entityId
     * @param contactId
     * @throws MalformedInputException
     */
    public static void addContact(String entityId, String contactId) throws MalformedInputException{
        addConnection(entityId,contactId,ConnectionType.Contact,Optional.empty());
    }

    /**
     * userId is joining groupId
     * @param userId
     * @param groupId
     * @throws MalformedInputException
     */
    public static void joinGroup(String userId, String groupId) throws MalformedInputException{
        addConnection(userId,groupId,ConnectionType.Member,Optional.of(MembershipType.MEMBER));
    }

    /**
     * Promotes a group member to admin status
     */
    public static void promoteUserToAdmin(String ownerId, String userId, String groupId) throws MalformedInputException, TarrieGroupException {

        MembershipType memType;
        if ((memType = getMembershipTypeOfUser(ownerId,groupId)) != MembershipType.OWNER){
            throw new TarrieGroupException(String.format("Only a owner can promote members to admin: [userId=%s,groupId=%s, memType=%s]",ownerId,groupId,memType));

        }

        if (getMembershipTypeOfUser(userId,groupId) == MembershipType.NULL){
            throw new TarrieGroupException(String.format("User must be a member of group to be promoted to admin: [userId=%s,groupId=%s]",userId,groupId));
        }

        addConnection(userId,groupId, ConnectionType.Member, Optional.of(MembershipType.ADMIN));
    }

    public static void demoteUserFromAdmin(String ownerId, String userId, String groupId) throws TarrieGroupException, MalformedInputException {
        MembershipType memType;
        if ((memType = getMembershipTypeOfUser(ownerId,groupId)) != MembershipType.OWNER){
            throw new TarrieGroupException(String.format("Only a owner can promote members to admin: [userId=%s,groupId=%s, memType=%s]",ownerId,groupId,memType));

        }

        if (getMembershipTypeOfUser(userId,groupId) == MembershipType.NULL){
            throw new TarrieGroupException(String.format("User must be a member of group to be promoted to admin: [userId=%s,groupId=%s]",userId,groupId));
        }
        addConnection(userId,groupId, ConnectionType.Member, Optional.of(MembershipType.MEMBER));

    }
    /**
     * User/Group add a contact
     * @param entityId the id of entity adding contact
     * @param connectionId the contact to add
     * @param connectionType either FOLLOW, MEMBER, or CONTACT
     * @return the new connection
     */
    private static EntityCondensed addConnection(String entityId,String connectionId, ConnectionType connectionType, Optional<MembershipType> OptionalOfmemType) throws MalformedInputException{
        if (entityId.equals(connectionId)){
            throw new MalformedInputException("Can't form connections with self"+String.format("[entityId=%s,connectionId=%s]",entityId,connectionId));
        }

        DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);

        // load contact info from dynamoDb
        Connection tarrieConnection  = mapper.load(Connection.class,connectionId,connectionId);

        switch (connectionType){
            case Following:
                // following events automatically not favorite's
                tarrieConnection.setFavorite(0);
            case Contact:
                // events cant be followed or be a contact
                if (Utility.getEntityType(connectionId).equals(EntityType.EVENT)){
                    throw new MalformedInputException("Events can't be followed or be a contact: "+connectionId);
                }
                // events cant add followers or add contacts
                if (Utility.getEntityType(entityId).equals(EntityType.EVENT)){
                    throw new MalformedInputException("Events can't add followers or add contacts: "+entityId);
                }
                // set range key
                tarrieConnection.setRangeKey(String.format("%s#%s", connectionType.toString(),connectionId));
                break;
            default: // Member

                // only users can be members of groups
                if (!(Utility.getEntityType(entityId).equals(EntityType.USER))){
                    throw new MalformedInputException("Only users can be members of groups :"+entityId);
                }
                // only groups can have members
                if (!(Utility.getEntityType(connectionId).equals(EntityType.GROUP))){
                    throw new MalformedInputException("Only groups can have members :"+entityId);
                }
                // set FAVE
                tarrieConnection.setFavorite(0);

                // set membership type - membership type should not be present for a contact or a follow
                if (OptionalOfmemType.isPresent()){
                    MembershipType memType = OptionalOfmemType.get();
                    switch (memType){
                        case ADMIN:
                            tarrieConnection.setData(String.format("%s#%s#%s", MembershipType.MEMBER.name(), MembershipType.ADMIN.name(),entityId));
                            break;
                        case OWNER:
                            tarrieConnection.setData(String.format("%s#%s#%s", MembershipType.MEMBER.name(), MembershipType.OWNER.name(),entityId));
                            break;
                        default:
                            tarrieConnection.setData(String.format("%s#%s", MembershipType.MEMBER.name(), entityId));
                    }
                }

                // set range key
                tarrieConnection.setRangeKey(connectionId);
        }



        // set hash key
        tarrieConnection.setEntityId(entityId);

        // save the new contact on dynamoDb
        mapper.save(tarrieConnection);

        // return the entity info
        EntityCondensed entityCondensed = new EntityCondensed();
        entityCondensed.setId(connectionId);
        entityCondensed.setImgUrl(tarrieConnection.getImgUrl());
        entityCondensed.setUserName(tarrieConnection.getName());

        return entityCondensed;

    }




    /*

  public static void sendMessageToInbox(
      String senderId, String senderName, String senderImgPath, String receiverId, String text) {

    if (TarrieDynamoDb.doesItemExist(senderId) && TarrieDynamoDb.doesItemExist(receiverId)) {
      DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);
      TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
      Instant datetime = Instant.now();

      io.tarrie.model.messages.inbox.To messageTo =
          new io.tarrie.model.messages.inbox.To(datetime);
      messageTo.setReceiverId(receiverId);
      messageTo.setSenderId(senderId);
      messageTo.setText(text);

      io.tarrie.model.messages.inbox.From messageFrom =
          new io.tarrie.model.messages.inbox.From(datetime);
      messageFrom.setReceiverId(receiverId);
      messageFrom.setText(text);
      messageFrom.setSenderId(senderId);
      messageFrom.setSenderInfo(senderName, senderImgPath);

      // execute transaction
      transactionWriteRequest.addPut(messageTo);
      transactionWriteRequest.addPut(messageFrom);
        transactionWriteRequest.add
      TarrieDynamoDb.executeTransactionWrite(transactionWriteRequest);

    } else {
      throw new MalformedInputException(
          "Failed to send message senderId or receiverId do not exist: "
              + String.format("{senderId:%s,receiverId:%s}", senderId, receiverId));
    }
  }*/
}
