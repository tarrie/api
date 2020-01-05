package io.tarrie;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.common.collect.Lists;
import io.tarrie.api.model.consumes.CreateUser;
import io.tarrie.database.TarrieDynamoDb;
import io.tarrie.database.TarrieS3;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.datamodel.NewUser;
import io.tarrie.database.exceptions.MalformedInputException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.mail.internet.AddressException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
  private static final Logger LOG = LogManager.getLogger(Controller.class);

  public static void main(String[] args) throws AddressException, IOException {
    Controller controller = new Controller();
    CreateUser newUser = new CreateUser();
    newUser.id = "jide69";
    newUser.name = "Jide";
    newUser.emailAddr = "jide@gmail.com";

    // controller.createUser(newUser);

    // File initialFile = new File("../pictures/coolpic.jpeg");
    // InputStream targetStream = new FileInputStream(initialFile);
    // controller.uploadProfileImg(targetStream,"image/jpeg","USR#jide69");

    ArrayList<String> testString = new ArrayList<>();
    testString.add("USR#jide69");
    testString.add("USR#becky_b1998");
    testString.add("USR#becky_b199.8_");
    testString.add("USR#becky_b199.8_BULLSHIT");

    System.out.println(controller.userExists(testString));
  }

  /**
   * Creates a tarrie user
   *
   * @param user A {@link NewUser} object
   * @throws AddressException thrown if email address is malformed
   * @throws MalformedInputException thrown if userId is not alphanumeric
   */
  public void createUser(CreateUser user) throws AddressException, MalformedInputException {
    NewUser newUser = new NewUser();
    newUser.setEmailAddr(user.emailAddr);
    newUser.setName(user.name);
    newUser.setUserId(user.id);
    newUser.setUserIdCopy(user.id);

    if (!(TarrieDynamoDb.doesItemExist(newUser.getUserId()))) {
      LOG.info(String.format("Created new user: %s", newUser.getUserId()));
      DynamoDBMapper mapper = new DynamoDBMapper(TarrieDynamoDb.awsDynamoDb);
      mapper.save(newUser);
    } else {
      LOG.error(
          String.format(
              "Failed to create new user since id already exists: %s", newUser.getUserId()));
      throw new MalformedInputException("userId already exists: " + user.id);
    }
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
  public void uploadProfileImg(InputStream is, String mimeType, String entityId)
      throws IOException, MalformedInputException {
    String s3Url = TarrieS3.uploadProfileImg(is, mimeType, entityId);
    TarrieDynamoDb.updateAttribute(entityId, DbAttributes.IMG_PATH, s3Url);
  }

  /**
   * Checks if a subset of userIds exist
   *
   * @param userIds the userIds to check
   * @return subset of userIds that exist
   */
  public List<String> userExists(List<String> userIds) {
    // Batch request can only be 100 at a time so partition the input into chunks of 100
    List<List<String>> partition = Lists.partition(userIds, 100);

    List<String> idsFound = new ArrayList<>();
    for (List<String> batch : partition) {
      idsFound.addAll(TarrieDynamoDb.doItemsExist(batch));
    }
    return idsFound;
  }
}
