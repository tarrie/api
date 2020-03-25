package io.tarrie;

import io.tarrie.controller.Controller;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.database.contants.ImgTypes;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.database.exceptions.TarrieGroupException;
import io.tarrie.model.Location;
import io.tarrie.model.condensed.UserCondensed;
import io.tarrie.model.constants.MembershipType;
import io.tarrie.model.consumes.CreateGroup;
import io.tarrie.model.consumes.CreateUser;

import javax.mail.internet.AddressException;
import java.io.*;

public class Test {
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

  public static void main(String[] args)
      throws AddressException, IOException, MalformedInputException, TarrieGroupException {
    // createDummyUsers();
    // createDummyGroup();
    // createDummyFollowers();
    // createDummyContacts();

    // joinDummyGroup();
    // uploadDummyImg();
    // promoteToAdmin();
    // demoteFromAdmin();

  }

  static void createDummyUsers() throws AddressException, IOException, MalformedInputException {
    CreateUser newUser1 = new CreateUser();
    newUser1.setId(unformattedUserId1);
    newUser1.setName(userName1);
    newUser1.setEmailAddr(userEmail1);

    CreateUser newUser2 = new CreateUser();
    newUser2.setId(unformattedUserId2);
    newUser2.setName(userName2);
    newUser2.setEmailAddr(userEmail2);

    Controller.createUser(newUser1);
    Controller.createUser(newUser2);
  }

  static void createDummyGroup() throws MalformedInputException {
    UserCondensed owner = new UserCondensed();
    owner.setId(formattedUserId1);
    owner.setName(userName1);

    Location loc = new Location();
    loc.city = "Evanston";
    loc.state = "IL";
    loc.locName = "Northwestern University";

    CreateGroup group = new CreateGroup();
    group.setGroupId(groupId1);
    group.setOwner(owner);
    group.setName("Boogo Party");
    group.setBio("Dude's who like to party");
    group.setLocation(loc);

    Controller.createGroup(group);
  }

  static void createDummyFollowers() throws MalformedInputException {
    // group1 is following user2
    Controller.followEntity(formattedGroupId1, formattedUserId2);

    // user2 is following group1
    Controller.followEntity(formattedUserId2, formattedGroupId1);

    // user2 is following user1
    Controller.followEntity(formattedUserId2, formattedUserId1);
  }

  static void createDummyContacts() throws MalformedInputException {
    // group1 is following user2
    Controller.addContact(formattedGroupId1, formattedUserId2);

    // user2 is following group1
    Controller.addContact(formattedUserId2, formattedGroupId1);

    // user2 is following user1
    Controller.addContact(formattedUserId2, formattedUserId1);
  }

  static void joinDummyGroup() throws MalformedInputException {
    Controller.joinGroup(formattedUserId2, formattedGroupId1);
  }

  static void promoteToAdmin() throws MalformedInputException, TarrieGroupException {
    String owner = formattedUserId1;
    String user = formattedUserId2;
    String group = formattedGroupId1;
    Controller.promoteUserToAdmin(owner, user, group);
  }

  static void demoteFromAdmin() throws MalformedInputException, TarrieGroupException {
    String owner = formattedUserId1;
    String user = formattedUserId2;
    String group = formattedGroupId1;
    Controller.demoteUserFromAdmin(owner, user, group);
  }

  static void transferGroupOwnerShip()  throws MalformedInputException, TarrieGroupException{
    String owner = formattedUserId1;
    String user = formattedUserId2;
    String group = formattedGroupId1;
    Controller.transferGroupOwner(owner , user, group, MembershipType.NULL);
  }

  static void uploadDummyImg() throws IOException, MalformedInputException {
    File dummyImg = new File("../pictures/coolpic.jpeg");
    InputStream is = new FileInputStream(dummyImg);
    Controller.uploadProfileImg(is, ImgTypes.JPEG, formattedGroupId1);

    dummyImg = new File("../pictures/dancing80s.gif");
    is = new FileInputStream(dummyImg);
    Controller.uploadProfileImg(is, ImgTypes.GIF, formattedUserId1);
  }
}
