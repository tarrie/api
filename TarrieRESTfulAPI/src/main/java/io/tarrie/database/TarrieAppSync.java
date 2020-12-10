package io.tarrie.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.tarrie.database.exceptions.HttpCloseException;
import io.tarrie.database.exceptions.HttpErrorCodeException;
import io.tarrie.database.exceptions.HttpResponseException;
import io.tarrie.database.exceptions.ProcessingException;
import io.tarrie.model.events.Event;
import io.tarrie.model.events.HostEvent;
import io.tarrie.utilities.Utility;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import software.amazon.awssdk.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Based on: https://www.baeldung.com/aws-appsync-spring
// https://www.baeldung.com/httpclient-post-http-request
// https://github.com/tinnou/appsync-java-sample/blob/master/src/main/java/tinnou/Main.java
public class TarrieAppSync {


  /** Gets context for http post request */
  private static Pair<CloseableHttpClient, HttpPost> _getHttpClient() throws URISyntaxException {
    final URI uri;

    uri = new URI(Utility.getParam("AppSyncApiUrl"));
    Collection<Header> headers = new ArrayList<>();
    headers.add(new BasicHeader("x-api-key", Utility.getParam("AppSyncApiKey")));
    CloseableHttpClient client = HttpClientBuilder.create().setDefaultHeaders(headers).build();
    HttpPost httpPost = new HttpPost(uri);

    return Pair.of(client, httpPost);
  }

  /**
   * Sets up payload, sends request, throws a error if not 200 code
   *
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
   * @throws IOException: If can't do some serialization/deserilization transformation
   */
  private static void _getHttpResponse(
      CloseableHttpClient client, HttpPost httpPost, Map payloadMap, String errorPrefix)
      throws HttpResponseException, HttpCloseException, HttpErrorCodeException,
          ProcessingException {
    // Add payload to request
    StringEntity entity;
    try {
      entity = new StringEntity(Utility.mapToString(payloadMap));
    } catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new ProcessingException(
          String.format("[%s::_getHttpResponse()] could not convert mapToString", errorPrefix));
    }
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

      Integer errCode = response.getStatusLine().getStatusCode();
      String errMsg = String.format(
                      "[%s,  %s]\n %s",
                      errorPrefix,
                      response.getStatusLine().getReasonPhrase(),
                      Utility.responseBodyToString(response));

      throw new HttpErrorCodeException(errCode,errMsg);
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
      throws HttpCloseException, HttpResponseException, HttpErrorCodeException, URISyntaxException,
          ProcessingException {

    // Get the Http Client
    Pair<CloseableHttpClient, HttpPost> httpContext = _getHttpClient();
    CloseableHttpClient client = httpContext.left();
    HttpPost httpPost = httpContext.right();

    // Setting up our query involves adding it to a query element in the message body:
    String json;
    try {
      json = Utility.pojoToJsonUnquotedFields(createEvent);
    } catch (JsonProcessingException e) {
      throw new ProcessingException("[TarrieAppSync::createEvent] pojoToJsonUnquotedFields failed");
    }

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put(
        "query",
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
                + "     hostInfo {main_pk main_sk imgPath name}"
                + "   }"
                + "}",
            createEvent.getId(), json));

    // Get the Http Response
    _getHttpResponse(client, httpPost, requestBody, "TarrieAppSync::createEvent()");
  }

  /**
   * Creates a event using the AWS AppSync Api
   *
   * @param edit: pojo that specifies format of create event payload
   * @throws UnsupportedEncodingException : If can't convert Map to string
   * @throws JsonProcessingException: If can't convert Pojo to Json
   * @throws HttpCloseException: If can't close http client
   * @throws HttpResponseException: If can't get a http response
   * @throws HttpErrorCodeException: Contains the message and error code if http returns status code
   *     !=200
   */
  public static void editEvent(Event edit)
      throws HttpCloseException, HttpResponseException, HttpErrorCodeException, URISyntaxException,
          ProcessingException {
    System.out.println(String.format("[TarrieAppSync::editEvent] %s", edit.getImgPath()));

    // Get the Http Client
    Pair<CloseableHttpClient, HttpPost> httpContext = _getHttpClient();
    CloseableHttpClient client = httpContext.left();
    HttpPost httpPost = httpContext.right();

    // Setting up our query involves adding it to a query element in the message body:
    String json;
    try {
      json = Utility.pojoToJsonUnquotedFields(edit);
    } catch (JsonProcessingException e) {
      throw new ProcessingException("[TarrieAppSync::createEvent] pojoToJsonUnquotedFields failed");
    }

    //System.out.println(String.format("[TarrieAppSync::editEvent] %s", json));

    Map<String, Object> requestBody = new HashMap<>();
    String payload =
        String.format(
            "mutation EditEvent {"
                + " editEvent(main_pk: \"%s\", input: %s) {"
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
                + "     hostInfo {main_pk main_sk imgPath name}"
                + "   }"
                + "}",
            edit.getId(), json);
    requestBody.put("query", payload);
    //System.out.println(String.format("[TarrieAppSync::editEvent] %s", payload));

    // Get the Http Response
    _getHttpResponse(client, httpPost, requestBody, "TarrieAppSync::createEvent()");
  }

  /**
   * Query that sets up Hosting For event. Basically puts the event under the primary key of the
   * owner for quick queries.
   *
   * @param hostEvent
   * @throws IOException
   * @throws HttpCloseException
   * @throws HttpResponseException
   * @throws HttpErrorCodeException
   * @throws URISyntaxException
   */
  public static void setEventRelationship(HostEvent hostEvent)
      throws HttpCloseException, HttpResponseException, HttpErrorCodeException, URISyntaxException,
          ProcessingException {
    // System.out.println(String.format("[TarrieAppSync::setHostingEvent()] %s", hostEvent));

    // Get the Http Client
    Pair<CloseableHttpClient, HttpPost> httpContext = _getHttpClient();
    CloseableHttpClient client = httpContext.left();
    HttpPost httpPost = httpContext.right();

    // Setting up our query involves adding it to a query element in the message body:
    Map<String, Object> requestBody = new HashMap<>();
    String formattedPayload =
        String.format(
            "mutation CreateEventRelationship {"
                + " createEventRelationship(main_pk: \"%s\", main_sk: \"%s\"%s) {"
                + "     main_pk"
                + "     main_sk"
                + "     data"
                + "     lastChangedCounter"
                + "   }"
                + "}",
            hostEvent.getId(),
            hostEvent.getEventId(),
            hostEvent.getData() == null
                ? ""
                : String.format(", data: \"%s\"", hostEvent.getData()));
    requestBody.put("query", formattedPayload);

    // Get the Http Response
    _getHttpResponse(client, httpPost, requestBody, "TarrieAppSync::setHostingEvent()");
  }

  public static void editEventRelationship(HostEvent hostEvent)
      throws URISyntaxException, HttpCloseException, HttpResponseException, HttpErrorCodeException,
          ProcessingException {

    // Get the Http Client
    Pair<CloseableHttpClient, HttpPost> httpContext = _getHttpClient();
    CloseableHttpClient client = httpContext.left();
    HttpPost httpPost = httpContext.right();

    // Setting up our query involves adding it to a query element in the message body:
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put(
        "query",
        String.format(
            "mutation EditEventRelationship {"
                + " editEventRelationship(main_pk: \"%s\", main_sk: \"%s\"%s) {"
                + "     main_pk"
                + "     main_sk"
                + "     data"
                + "     lastChangedCounter"
                + "   }"
                + "}",
            hostEvent.getId(),
            hostEvent.getEventId(),
            hostEvent.getData() == null
                ? ""
                : String.format(", data: \"%s\"", hostEvent.getData())));

    // Get the Http Response
    _getHttpResponse(client, httpPost, requestBody, "TarrieAppSync::editHostedEvent()");
  }
}
