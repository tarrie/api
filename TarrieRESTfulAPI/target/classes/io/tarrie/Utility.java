package io.tarrie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Map;

public class Utility {

  /**
   * This is useful for getting parameters that might be defined either in system properties
   * when running on Elastic Beanstalk) or in environment variables (on command line or IDE)
   */
  public static String getParam(String paramName) {
    String prop = System.getProperty(paramName);
    return (prop != null)? prop : System.getenv(paramName);
  }

  /**
   * Add email address
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
}
