package io.tarrie.database.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class HttpErrorCodeException extends Exception {
  public HttpErrorCodeException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public HttpErrorCodeException(String errorMessage) {
    super(errorMessage);
  }

  public HttpErrorCodeException(Integer errorCode, String errorMessage) {
    super(errorMsgToJson(errorCode, errorMessage));
  }

  /**
   * Get the error message and code
   *
   * @param aError
   * @return {"code": error code, "message": the reason}
   * @throws IOException if can't convert json to map
   */
  public static Map<String, String> ErrorMsgToMap(HttpErrorCodeException aError)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> map = mapper.readValue(aError.getMessage(), Map.class);
    return map;
  }

  private static String errorMsgToJson(Integer errorCode, String errorMessage) {
    return new JSONObject()
        .put("code", Integer.toString(errorCode))
        .put("message", errorMessage)
        .toString();
  }
}
