package io.tarrie.controller;

import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import io.tarrie.utilities.Utility;
import io.tarrie.database.TarrieDynamoDb;
import io.tarrie.database.TestDbHelper;
import io.tarrie.database.contants.ImgTypes;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.database.exceptions.TarrieExistenceError;
import io.tarrie.model.Location;
import io.tarrie.model.condensed.UserCondensed;
import io.tarrie.model.consumes.CreateGroup;
import io.tarrie.model.consumes.CreateUser;
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
    System.out.println("Error Group");

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
    System.out.println("Creating User");
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
  @Order(3)
  public void createDummyGroup()
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
          MalformedInputException, TarrieExistenceError {
    System.out.println("Creating Group");

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
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
          MalformedInputException, IOException {
    System.out.println("Upload Img");

    File dummyImg = new File("../pictures/coolpic.jpeg");
    InputStream is = new FileInputStream(dummyImg);
    Controller.uploadProfileImg(is, ImgTypes.JPEG, formattedGroupId1);

    dummyImg = new File("../pictures/dancing80s.gif");
    is = new FileInputStream(dummyImg);
    Controller.uploadProfileImg(is, ImgTypes.GIF, formattedUserId1);
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
