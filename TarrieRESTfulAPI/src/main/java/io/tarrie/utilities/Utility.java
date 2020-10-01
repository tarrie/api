package io.tarrie.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.tarrie.database.contants.EntityType;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.constants.CharacterLimit;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

  /** Downloads img from S3 and returns the input stream. */
  public static InputStream getInputStreamFromS3ImgUrl(String s3Url) throws IOException {
    URL url = new URL(s3Url);
    URLConnection connection = url.openConnection();
    return connection.getInputStream();
  }

  /**
   * Hashtags can only contain letters, numbers, and underscores (_), no special characters. 140
   * character limit. Must start with #
   *
   * @param hashTags hashtag to check
   * @throws MalformedInputException if invalid hashtag
   */
  public static void verifyHashTags(Set<String> hashTags) throws MalformedInputException {
    for (String tag : hashTags) {
      if (tag.charAt(0) != '#') {
        throw new MalformedInputException("Hashtags must start with (#): " + tag);
      }

      if (!(tag.substring(1).matches("[a-zA-Z0-9_]+"))) {
        throw new MalformedInputException(
            "Hashtags can only contain letters, numbers, and underscores (_), no special characters.:"
                + tag);
      }

      if (!(tag.length() < CharacterLimit.MEDIUM - 10)) {
        throw new MalformedInputException(
            "Hashtags are subject to the standard "
                + (CharacterLimit.MEDIUM - 10)
                + " characters limit: "
                + tag);
      }

      if ((tag.length() < 2)) {
        throw new MalformedInputException("Hashtags too small less than 2 chars :" + tag);
      }
    }
  }

  public static boolean isUrlvalid(String url) {
    return new UrlValidator().isValid(url);
  }

  /**
   * Generates a random essentially unique id: first component is the hashed datetime the second
   * component is a random 6 char string.
   *
   * @return id
   */
  public static String generateUniqueId() {
    return String.format("%d%s", Instant.now().hashCode(), generateRandomString(6));
  }
  /**
   * Generate a random alphanumeric string of length `targetStringLength`
   *
   * @see <a href="https://www.baeldung.com/java-random-string">taken from this link</a>
   * @param targetStringLength the target length of the string
   * @return random string of length targetStringLength
   */
  public static String generateRandomString(int targetStringLength) {
    return RandomStringUtils.random(targetStringLength, true, true);
  }

  /**
   * Checks if a datetime is valid. Throws a error if not.
   *
   * @param strDateTime datetime formatted string to check
   */
  public static void isDateTimeValid(String strDateTime) throws MalformedInputException {
    try {
      DateTime.parse(strDateTime);
    } catch (IllegalArgumentException e) {
      throw new MalformedInputException("Datetime is malformed:" + strDateTime);
    }
  }

  /**
   * Checks if a Tarrie Id is valid
   *
   * @param id
   */
  public static boolean isIdValid(String id) {
    Pattern pattern =
        Pattern.compile(
            String.format(
                "(?<entityType>%s|%s|%s)#", EntityType.GROUP, EntityType.EVENT, EntityType.USER));
    Matcher matcher = pattern.matcher(id);
    return matcher.find();
  }

  /**
   * Gets the entity type given a rawId: Group, User, Event
   *
   * @param rawId Tarrie id
   * @return entity identifier
   */
  public static String getEntityType(String rawId) throws MalformedInputException {
    Pattern pattern =
        Pattern.compile(
            String.format(
                "(?<entityType>%s|%s|%s)#", EntityType.GROUP, EntityType.EVENT, EntityType.USER));
    Matcher matcher = pattern.matcher(rawId);
    if (matcher.find()) {
      return matcher.group("entityType");
    } else {
      throw new MalformedInputException("Invalid Tarrie id: " + rawId);
    }
  }

  /**
   * Returns the actual id of a entity. Example: EVT#xyz123 -> xyz123
   *
   * @param rawId Tarrie id
   * @return Tarrie id w/o the entity identifier
   */
  public static String getEntityId(String rawId) throws MalformedInputException {
    Pattern pattern =
        Pattern.compile(
            String.format(
                "(%s#|%s#|%s#)(?<idActual>.*)",
                EntityType.GROUP, EntityType.EVENT, EntityType.USER));
    Matcher matcher = pattern.matcher(rawId);

    if (matcher.find()) {
      return matcher.group("idActual");
    } else {
      throw new MalformedInputException("Invalid Tarrie id: " + rawId);
    }
  }

  /**
   * This is useful for getting parameters that might be defined either in system properties when
   * running on Elastic Beanstalk) or in environment variables (on command line or IDE)
   */
  public static String getParam(String paramName) {
    String prop = System.getProperty(paramName);
    return (prop != null) ? prop : System.getenv(paramName);
  }

  /**
   * Add email address
   *
   * @param email
   * @throws AddressException
   */
  public static InternetAddress getEmailAddressFromString(String email) throws AddressException {
    InternetAddress emailAddr = new InternetAddress(email);
    emailAddr.validate();
    return emailAddr;
  }
  /**
   * Checks if a string is null or empty
   *
   * @param str
   * @return
   */
  public static boolean isStringNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }

  /**
   * Converts a plain old java object (pojo) to json string
   *
   * @param pojo java object
   * @return json string
   * @throws JsonProcessingException
   */
  public static String pojoToJson(Object pojo) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper.writeValueAsString(pojo);
  }

  public static String pojoToJsonUnquotedFields(Object pojo) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper.writeValueAsString(pojo);
  }

  public static Map pojoToMap(Object pojo)
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    return PropertyUtils.describe(pojo);
  }

  /**
   * Loads a property file, To get a property: properties.getProperty("user")
   *
   * @param pathToFileFromRoot: Path the file from root, eg /src/main/resources/graphql.properties
   * @return
   * @throws IOException
   */
  public static Properties loadPropertyValues(String pathToFileFromRoot) {
    InputStream input = null;
    try {
      input = new FileInputStream(pathToFileFromRoot);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Properties prop = new Properties();
    try {
      prop.load(input);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return prop;
  }

  public static String mapToString(Map map) throws JsonProcessingException {

    return new ObjectMapper().writeValueAsString(map);
  }

  /**
   * Get's the response body from a CloseableHttpResponse http response
   *
   * @param response: http response
   * @return the response body
   * @throws IOException if can't convert response body to string
   */
  public static String responseBodyToString(CloseableHttpResponse response) throws IOException {
    HttpEntity httpEntity = response.getEntity();
    return EntityUtils.toString(httpEntity, "UTF-8");
  }
}
