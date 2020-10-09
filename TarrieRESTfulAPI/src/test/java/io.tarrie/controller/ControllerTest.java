package io.tarrie.controller;

import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import io.tarrie.database.exceptions.*;
import io.tarrie.model.events.EventRelationship;
import io.tarrie.model.events.CreateEvent;
import io.tarrie.model.events.Event;
import io.tarrie.model.events.HostEvent;
import io.tarrie.utilities.Utility;
import io.tarrie.database.TarrieDynamoDb;
import io.tarrie.database.TestDbHelper;
import io.tarrie.database.contants.ImgTypes;
import io.tarrie.model.Location;
import io.tarrie.model.condensed.UserCondensed;
import io.tarrie.model.consumes.CreateGroup;
import io.tarrie.model.consumes.CreateUser;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import javax.mail.internet.AddressException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/// https://junit.org/junit5/docs/current/user-guide/
/// https://www.tutorialspoint.com/junit/junit_test_framework.htm
/// https://www.baeldung.com/junit-5

/// Ordering: https://mkyong.com/junit5/junit-5-test-execution-order/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {
  public static final String userName1 = "Jake Stricht";
  public static final String userEmail1 = "jake@gmail.com";
  public static final String unformattedUserId1 = "northwestern_6960";
  public static final String formattedUserId1 = "USR#northwestern_6960";

  public static final String userName2 = "Becky B";
  public static final String userEmail2 = "beck2020@u.northwestern.com";
  public static final String unformattedUserId2 = "beckb_triDelt";
  public static final String formattedUserId2 = "USR#beckb_triDelt";

  public static final String groupId1 = "boogoParty33333";
  public static final String formattedGroupId1 = "GRP#boogoParty33333";

  public static Event createdEvent;
  // Variables to set up DynamoDb Local
  private static final TestDbHelper testDbHelper = new TestDbHelper();

  public static void listTables(ListTablesResult result, String method) {
    System.out.println(
        "found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
    for (String table : result.getTableNames()) {
      System.out.println(table);
    }
  }

  @BeforeAll
  public static void setupClass() throws Exception {
    testDbHelper.SetUpDynamoDB();
    testDbHelper.SetUpS3();
  }

  @AfterAll
  public static void teardownClass() throws Exception {
    testDbHelper.TearDownDynamoDb();
    testDbHelper.TearDownS3();
  }

  @Test
  @DisplayName("Create Dummy Group: Invalid User")
  @Order(1)
  public void createDummyGroupNoUser()
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    UserCondensed owner = new UserCondensed();

    owner.setId(formattedUserId1);
    owner.setName(userName1);

    Location loc = new Location();
    loc.setCity("Evanston");
    loc.setState("IL");
    loc.setLocName("Northwestern University");

    CreateGroup group = new CreateGroup();
    group.setGroupId(groupId1);
    group.setOwner(owner);
    group.setName("Boogo Party");
    group.setBio("Dude's who like to party");
    group.setLocation(Utility.pojoToMap(loc));

    assertThrows(MalformedInputException.class, () -> Controller.createGroup(group));
  }

  @Test
  @DisplayName("Create Dummy Users")
  @Order(2)
  public void createDummyUsers() throws MalformedInputException, AddressException, IOException {
    CreateUser newUser1 = new CreateUser();
    newUser1.setId(formattedUserId1);
    newUser1.setName(userName1);
    newUser1.setEmailAddr(userEmail1);

    CreateUser newUser2 = new CreateUser();
    newUser2.setId(formattedUserId2);
    newUser2.setName(userName2);
    newUser2.setEmailAddr(userEmail2);

    Controller.createUser(newUser1);
    Controller.createUser(newUser2);

    assertTrue(TarrieDynamoDb.doesItemExist(newUser1.getId()));
    assertTrue(TarrieDynamoDb.doesItemExist(newUser2.getId()));
  }

  @Test
  @DisplayName("Create Dummy Group: valid User")
  public void convertDynamoDbPojoToMap()
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    UserCondensed owner = new UserCondensed();

    owner.setId(formattedUserId1);
    owner.setName(userName1);

    Location loc = new Location();
    loc.setCity("Evanston");
    loc.setState("IL");
    loc.setLocName("Northwestern University");

    CreateGroup group = new CreateGroup();
    group.setGroupId(groupId1);
    group.setOwner(owner);
    group.setName("Boogo Party");
    group.setBio("Dude's who like to party");
    group.setLocation(Utility.pojoToMap(loc));
  }

  @Test
  @DisplayName("Create Dummy Group: valid User")
  @Order(3)
  public void createDummyGroup()
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
          MalformedInputException, TarrieExistenceError {

    UserCondensed owner = new UserCondensed();
    owner.setId(formattedUserId1);
    owner.setName(userName1);

    Location loc = new Location();
    loc.setCity("Evanston");
    loc.setState("IL");
    loc.setLocName("Northwestern University");

    CreateGroup group = new CreateGroup();
    group.setGroupId(formattedGroupId1);
    group.setOwner(owner);
    group.setName("Boogo Party");
    group.setBio("Dude's who like to party");
    group.setLocation(Utility.pojoToMap(loc));

    Controller.createGroup(group);
    assertTrue(TarrieDynamoDb.doesItemExist(group.getGroupId()));
  }

  @Test
  @Order(4)
  public void uploadDummyImg()
          throws MalformedInputException, IOException, ProcessingException {

    File dummyImg = new File("../pictures/coolpic.jpeg");
    InputStream is = new FileInputStream(dummyImg);
    Controller.uploadProfileImg(is, ImgTypes.JPEG, formattedGroupId1);

    dummyImg = new File("../pictures/dancing80s.gif");
    is = new FileInputStream(dummyImg);
    Controller.uploadProfileImg(is, ImgTypes.GIF, formattedUserId1);
  }

  @Test
  @Order(5)
  public void createEvent()
          throws MalformedInputException, IOException, HttpCloseException, IllegalAccessException,
          HttpErrorCodeException, URISyntaxException, InvocationTargetException,
          HttpResponseException, NoSuchMethodException, ProcessingException {

    HashSet<String> coordinators = new HashSet<>();
    coordinators.add(formattedUserId2);

    // HashSet<String> invitedEntityIds = new HashSet<>();
    // invitedEntityIds.add(formattedUserId1);

    Location loc = new Location();
    loc.setZipCode(60201);
    loc.setCity("Evanston");
    loc.setState("IL");
    loc.setLocName("Northwestern University");
    loc.setLine1("Kellogg Global Hub");

    // EventPrivacy eventPrivacy = new EventPrivacy();
    // eventPrivacy.setInvitable(true);
    // eventPrivacy.setVisibilityType(EventVisibilityType.Public.toString());

    // HashSet<String> hashTags = new HashSet<>();
    // hashTags.add("#yeet");
    // hashTags.add("#yeetcode");

    // DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int
    // secondOfMinute, int millisOfSecond)
    String _startTime =
        new DateTime(2020, 12, 25, 12, 0, 0, 0)
            .withZone(DateTimeZone.UTC)
            .toDateTimeISO()
            .toString();
    String _endTime =
        new DateTime(2020, 12, 28, 12, 0, 0, 0)
            .withZone(DateTimeZone.UTC)
            .toDateTimeISO()
            .toString();

    CreateEvent createEvent = new CreateEvent();
    createEvent.setName("**BoogoParty**2");
    createEvent.setCoordinators(coordinators);
    createEvent.setCreatorId(formattedGroupId1);
    // createEvent.setEventPrivacy(eventPrivacy);
    createEvent.setLocation(Utility.pojoToMap(loc));
    createEvent.setLinkSharing(true);
    // createEvent.setInvitedEntityIds(invitedEntityIds);
    createEvent.setBio("Libpusm labrum et al took tokk leetzial");
    // createEvent.setHashTags(hashTags);
    createEvent.setStartTime(_startTime);
    createEvent.setEndTime(_endTime);

    // passes if no exceptions
    Optional<Boolean> useDynamo = Optional.of(true);
    createdEvent = Controller.createEvent(createEvent,useDynamo);
    // Check event created
    System.out.println(createdEvent.getId());
    assertTrue(TarrieDynamoDb.doesItemExist(createdEvent.getId()));

    // check event created under the host
    String host_pk = formattedGroupId1;
    String host_sk = String.format("%s#%s", EventRelationship.HOST, createdEvent.getId());
    assertTrue(TarrieDynamoDb.doesItemExist(host_pk,host_sk));

  }

  @Test
  @Order(6)
  public void listEvents() {
    List<HostEvent> hostedEvents = Controller.getHostedEvents(formattedGroupId1);
    assertEquals(createdEvent.getId(),Utility.getEntityIdFromEventRelationshipPrefix(hostedEvents.get(0).getEventId()));
  }

  /*

    @Test
    @DisplayName("Create Dummy Followers")
    @Order(3)
    public void createDummyFollowers()
        throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
            MalformedInputException {
      // group1 is following user2
      Controller.followEntity(formattedGroupId1, formattedUserId2);

      // user2 is following group1
      Controller.followEntity(formattedUserId2, formattedGroupId1);

      // user2 is following user1
      Controller.followEntity(formattedUserId2, formattedUserId1);
    }

    @Test
    @Order(4)
    public void createDummyContacts()
        throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
            MalformedInputException {
      // group1 is following user2
      Controller.addContact(formattedGroupId1, formattedUserId2);

      // user2 is following group1
      Controller.addContact(formattedUserId2, formattedGroupId1);

      // user2 is following user1
      Controller.addContact(formattedUserId2, formattedUserId1);
    }

    @Test
    @Order(5)
    public void joinDummyGroup()
        throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
            MalformedInputException {
      Controller.joinGroup(formattedUserId2, formattedGroupId1);

    }
  */

}
