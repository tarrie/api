package io.tarrie.database.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tarrie.utilities.Utility;

import java.io.IOException;
import java.util.Map;

public class HttpResponseException extends Exception {

  public HttpResponseException(String errorMessage) {
    super(errorMessage);
  }


}
