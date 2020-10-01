package io.tarrie.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.tarrie.database.exceptions.HttpCloseException;
import io.tarrie.database.exceptions.HttpErrorCodeException;
import io.tarrie.database.exceptions.HttpResponseException;
import io.tarrie.model.consumes.CreateEvent;
import io.tarrie.model.events.Event;
import io.tarrie.model.events.HostEvent;
import io.tarrie.utilities.Utility;
import org.apache.commons.lang3.CharSet;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import software.amazon.awssdk.utils.Pair;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Based on: https://www.baeldung.com/aws-appsync-spring
// https://www.baeldung.com/httpclient-post-http-request

public class TarrieAppSync {
  private static final String graphqlPropFileName = "src/main/resources/graphql.properties";
  private static final Properties properties = Utility.loadPropertyValues(graphqlPropFileName);

  static Properties getProperties() {
    return properties;
  }


  /**
   * Gets context for http post request
   */
  private static Pair<CloseableHttpClient, HttpPost> _getHttpClient() {

    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(properties.getProperty("ApiUrl"));
    httpPost.setHeader("x-api-key", properties.getProperty("ApiKey"));
    httpPost.setHeader(
        "Accept", String.format("%s, %s", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
    httpPost.setHeader("Accept-Charset", "utf-8");
    httpPost.setHeader("Content-type", "application/json");

    return Pair.of(client, httpPost);
  }

  /**
   * Sets up payload, sends request, throws a error if not 200 code
   * @param client: (CloseableHttpClient)
   * @param httpPost: (HttpPost)
   * @param payloadMap: (Map) contains the graphQl payload
   * @param errorPrefix: (String) contains the prefix to append to errors
   * @throws UnsupportedEncodingException : If can't convert Map to string
   * @throws JsonProcessingException: If can't convert Pojo to Json
   * @throws HttpCloseException: If can't close http client
   * @throws HttpResponseException: If can't get a http response
   * @throws HttpErrorCodeException: Contains the message and error code if http returns status code
   *     !=200
   */
  private static void _getHttpResponse(
      CloseableHttpClient client, HttpPost httpPost, Map payloadMap, String errorPrefix)
      throws HttpResponseException, HttpCloseException, HttpErrorCodeException,
          JsonProcessingException, UnsupportedEncodingException {
    // Add payload to request
    StringEntity entity = new StringEntity(Utility.mapToString(payloadMap));
    httpPost.setEntity(entity);

    // Get request
    CloseableHttpResponse response;
    try {
      response = client.execute(httpPost);
    } catch (IOException e) {
      throw new HttpResponseException(String.format("[%s] Error sending http post", errorPrefix));
    }

    try {
      client.close();
    } catch (IOException e) {
      throw new HttpCloseException(String.format("[%s] Error closing  http client", errorPrefix));
    }

    if (response.getStatusLine().getStatusCode() != 200) {
      String jsonString =
          new JSONObject()
              .put("code", Integer.toString(response.getStatusLine().getStatusCode()))
              .put(
                  "message",
                  String.format("[%s] %s", errorPrefix, response.getStatusLine().getReasonPhrase()))
              .toString();
      throw new HttpErrorCodeException(jsonString);
    }
  }

  /**
   * Creates a event using the AWS AppSync Api
   *
   * @param createEvent pojo that specifies format of create event payload
   * @throws UnsupportedEncodingException : If can't convert Map to string
   * @throws JsonProcessingException: If can't convert Pojo to Json
   * @throws HttpCloseException: If can't close http client
   * @throws HttpResponseException: If can't get a http response
   * @throws HttpErrorCodeException: Contains the message and error code if http returns status code
   *     !=200
   */
  public static void createEvent(Event createEvent)
      throws JsonProcessingException, UnsupportedEncodingException, HttpCloseException,
          HttpResponseException, HttpErrorCodeException {

    // Get the Http Client
    Pair<CloseableHttpClient, HttpPost> httpContext = _getHttpClient();
    CloseableHttpClient client = httpContext.left();
    HttpPost httpPost = httpContext.right();

    // Setting up our query involves adding it to a query element in the message body:
    String json = Utility.pojoToJsonUnquotedFields(createEvent);
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put(
        "mutation",
        String.format(
            "mutation CreateEvent {"
                + " createEvent(main_pk: \"%s\", input: %s) {"
                + "     main_pk"
                + "     main_sk"
                + "     coordinators"
                + "     name"
                + "     location"
                + "     imgPath"
                + "     linkSharing"
                + "     text"
                + "     data"
                + "     endTime"
                + "     createdTime"
                + "     hostInfo"
                + "   }"
                + "}",
            createEvent.getId(), json));

    // Get the Http Response
    _getHttpResponse(client, httpPost, requestBody, "TarrieAppSync::createEvent()");
  }

  // setHostingEvent
  public static void setHostingEvent(HostEvent hostEvent)
      throws JsonProcessingException, UnsupportedEncodingException, HttpCloseException,
          HttpResponseException, HttpErrorCodeException {

    // Get the Http Client
    Pair<CloseableHttpClient, HttpPost> httpContext = _getHttpClient();
    CloseableHttpClient client = httpContext.left();
    HttpPost httpPost = httpContext.right();

    // Setting up our query involves adding it to a query element in the message body:
    String json = Utility.pojoToJsonUnquotedFields(hostEvent);
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put(
        "mutation",
        String.format(
            "mutation SetHostingEvent {"
                + " setHostingEvent(main_pk: \"%s\", main_sk: \"%s\", input: %s) {"
                + "     main_pk"
                + "     main_sk"
                + "     coordinators"
                + "     name"
                + "     location"
                + "     imgPath"
                + "     data"
                + "     endTime"
                + "   }"
                + "}",
            hostEvent.getHostId(), hostEvent.getEventId(), json));

    // Get the Http Response
    _getHttpResponse(client, httpPost, requestBody, "TarrieAppSync::setHostingEvent()");
  }
}
