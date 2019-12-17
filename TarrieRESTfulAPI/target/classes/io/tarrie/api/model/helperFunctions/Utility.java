package io.tarrie.api.model.helperFunctions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Utility {

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
