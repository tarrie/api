package io.tarrie.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.tarrie.database.contants.EntityType;
import io.tarrie.database.contants.EntityTypeEnum;
import io.tarrie.database.exceptions.HttpErrorCodeException;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.constants.CharacterLimit;
import io.tarrie.database.contants.EventRelationshipEnum;
import io.tarrie.model.events.EventRelationship;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
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
    if (hashTags == null) {
      return;
    }

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
    if (isStringNull(strDateTime)) {
      return;
    }
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

  /** Checks if id is valid of type entitiy type */
  public static boolean isIdValid(String id, EntityTypeEnum entityTypeEnum) {
    Pattern pattern = Pattern.compile(String.format("(?<entityType>%s)#", entityTypeEnum));
    Matcher matcher = pattern.matcher(id);
    return matcher.find();
  }

  public static String urlDecode(String value) throws UnsupportedEncodingException {
    return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
  }

  /**
   * Gets the entity type given a rawId: Group, User, Event EVT#-1742985703BLbsdU -> EVT
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
   * Gets EventRelationshipEnum from a given a ID : HOST#EVT#-1742985703BLbsdU -> HOST
   * @param id id
   * @return {@link EventRelationshipEnum}
   */
  public static EventRelationshipEnum getEventRelationshipFromId(String id)
      throws MalformedInputException {

    Pattern pattern =
        Pattern.compile(
            String.format(
                "(?<eventRelationship>%s|%s|%s)#",
                EventRelationshipEnum.HOST, EventRelationshipEnum.RSVP, EventRelationshipEnum.SAVED));
    Matcher matcher = pattern.matcher(id);

    if (matcher.find()) {
      return EventRelationshipEnum.valueOf(matcher.group("eventRelationship"));
    } else {
      throw new MalformedInputException("Invalid Tarrie id: " + id);
    }
  }

  /**
   * EVT#-1742985703BLbsdU -> HOST#EVT#-1742985703BLbsdU
   */
  public static String eventIdToEventRelationship(
      String eventId, EventRelationshipEnum relationship) {
    return String.format("%s#%s", relationship, eventId);
  }
  /**
   * Returns the entity if prefixed by a EventRelationship
   *
   * @param id
   * @return HOST#EVT#-1742985703BLbsdU -> EVT#-1742985703BLbsdU
   */
  public static String getEntityIdFromEventRelationshipPrefix(String id) {

    return id.replaceAll(
        String.format(
            "%s#|%s#|%s#",
            EventRelationshipEnum.HOST, EventRelationshipEnum.RSVP, EventRelationshipEnum.SAVED),
        "");
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

  public static boolean isStringNull(String str) {
    return str == null;
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
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper.writeValueAsString(pojo);
  }

  public static String pojoToJsonUnquotedFields(Object pojo) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper.writeValueAsString(pojo);
  }

  @JsonProperty()
  public static String mapToJsonUnquotedFields(Map map) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper.writeValueAsString(map);
  }

  public static Map pojoToMap(Object pojo) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.convertValue(pojo, Map.class);
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

  public static void isValidEntitySet(Collection<String> entitySet, String errorPrefix)
      throws MalformedInputException {

    if (entitySet == null || entitySet.isEmpty()) {
      return;
    }

    for (String entityId : entitySet) {
      if (!(Utility.isIdValid(entityId))) {
        throw new MalformedInputException(
            String.format("[%s] malformed id: %s", errorPrefix, entityId));
      }
    }
  }
  /**
   * Get's the response body from a CloseableHttpResponse http response
   *
   * @param response: http response
   * @return the response body
   * @throws IOException if can't convert response body to string
   */
  public static String responseBodyToString(CloseableHttpResponse response) {

    HttpEntity httpEntity = response.getEntity();

    try {
      return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
    } catch (IOException e) {
      return response.toString();
    }
  }

  public static Response processHttpErrorCodeException(HttpErrorCodeException e) {
    Map<String, String> errorMap;
    try {
      errorMap = HttpErrorCodeException.ErrorMsgToMap(e);
    } catch (IOException ex) {
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(
              String.format(
                  " Something terribly wrong, could not run HttpErrorCodeException.ErrorMsgToMap(e) ; %s",
                  e.getMessage()))
          .build();
    }
    return Response.status(Integer.parseInt(errorMap.get("code")))
        .type(MediaType.TEXT_PLAIN_TYPE)
        .entity(errorMap.get("message"))
        .build();
  }
}
