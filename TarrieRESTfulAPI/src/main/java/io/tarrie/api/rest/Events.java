package io.tarrie.api.rest;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import io.swagger.annotations.*;
import io.tarrie.database.contants.EntityTypeEnum;
import io.tarrie.database.contants.ImgTypes;
import io.tarrie.database.exceptions.*;
import io.tarrie.model.Entity;
import io.tarrie.model.consumes.EntityId;
import io.tarrie.utilities.Utility;
import io.tarrie.controller.Controller;
import io.tarrie.model.events.CreateEvent;
import io.tarrie.model.events.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

// http://localhost:8080/api/events
@Api(tags = "Events endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Events endpoints", description = "Used to create, modify, and get events")
    })
@Path("/events")
public class Events implements io.tarrie.api.interfaces.Events {

  ///  http://localhost:8080/events?eventId=EVT#123&eventId=EVT#234
  @ApiOperation(value = "Get a event")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Created", response = Event.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  public Response getEvents(@QueryParam("eventId") List<String> eventIds, EntityId entityId) {


    JSONArray jsonArray;
    try {
      jsonArray = Controller.getEvent(eventIds, entityId);
    } catch (HttpErrorCodeException e) {
      return Utility.processHttpErrorCodeException(e);
    } catch (ProcessingException e) {
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Processing Exception; %s", e.getMessage()))
          .build();
    } catch (MalformedInputException e) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Malformed input; %s", e.getMessage()))
          .build();
    }
    return Response.status(200).type(MediaType.APPLICATION_JSON).entity(jsonArray.toString()).build();
  }

  // https://crunchify.com/create-very-simple-jersey-rest-service-and-send-json-data-from-java-client/
  @ApiOperation(value = "Create a event")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Created", response = Event.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  public Response createEvent(CreateEvent createEvent) {

    if (createEvent.getUserId() == null) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Bad input; userId is missing")
          .build();
    }

    if (createEvent.getCreatorId() == null) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Bad input; creatorId is missing")
          .build();
    }

    if (!(Utility.isIdValid(createEvent.getUserId()))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Bad input; userId invalid")
          .build();
    }

    if (!(Utility.isIdValid(createEvent.getCreatorId()))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Bad input; creatorId invalid")
          .build();
    }

    String payload;
    try {
      Event eventCondensed;
      try {
        eventCondensed = Controller.createEvent(createEvent, Optional.empty());
      } catch (HttpCloseException | HttpResponseException | URISyntaxException e) {
        return Response.status(500).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
      } catch (HttpErrorCodeException e) {
        return Utility.processHttpErrorCodeException(e);
      } catch (ProcessingException e) {
        return Response.status(500)
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity(String.format("Processing Exception; %s", e.getMessage()))
            .build();
      }
      payload = Utility.pojoToJson(eventCondensed);

    } catch (MalformedInputException | JsonProcessingException e) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Bad input; %s", e.getMessage()))
          .build();
    }

    return Response.status(200).entity(payload).type(MediaType.APPLICATION_JSON_TYPE).build();
  }

  @ApiOperation(value = "Edit a event")
  @Path("{eventId}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "No content; event does not exist"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  public Response editEvent(
      @ApiParam(name = "eventId", value = "ID of event", required = true) @PathParam("eventId")
          String eventId,
      Event editEvent) {

    try {
      Controller.editEvent(eventId, editEvent);
    } catch (URISyntaxException
        | HttpCloseException
        | ProcessingException
        | HttpResponseException e) {
      return Response.status(500).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
    } catch (HttpErrorCodeException e) {
      return Utility.processHttpErrorCodeException(e);
    } catch (MalformedInputException e) {
      return Response.status(400).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
    }

    // Just return the main_pk
    JSONObject obj = new JSONObject();
    obj.put("main_pk", eventId);
    return Response.status(200).entity(obj.toString()).type(MediaType.APPLICATION_JSON).build();
  }

  @ApiOperation(value = "Upload profile picture to event")
  @Path("pictures/{eventId}")
  @PUT
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "No content; event does not exist"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  public Response uploadProfilePic(
      @ApiParam(name = "eventId", value = "ID of Event", required = true) @PathParam("eventId")
          String eventId,
      @FormDataParam("file") FormDataBodyPart fileBody,
      @FormDataParam("userId") FormDataBodyPart userIdBody) {

    System.out.println(String.format("[Events::uploadProfilePic()] %s", eventId));

    String mimeType = fileBody.getMediaType().toString();
    String userId = userIdBody.getValue();
    InputStream is = fileBody.getValueAs(InputStream.class);

    if (!(ImgTypes.ACCEPTABLE_MIME_IMAGES.contains(mimeType))) {
      String errorMsg =
          String.format(
              "Invalid MimeType must be one of: %s", ImgTypes.ACCEPTABLE_MIME_IMAGES.toString());
      return Response.status(400).entity(errorMsg).type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    if ((!(Utility.isIdValid(eventId, EntityTypeEnum.EVT)))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Bad input;  eventId is not valid: %s", eventId))
          .build();
    }

    if ((!(Utility.isIdValid(userId, EntityTypeEnum.USR)))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Bad input;  userId is not valid: %s", userId))
          .build();
    }

    String imgPath;
    try {
      // Actual uploading
      imgPath = Controller.uploadEventProfileImg(is, mimeType, eventId);

      if (imgPath == null) {
        return Response.status(500)
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity("Internal Server error: upload failed")
            .build();
      }
    } catch (AmazonServiceException e) {
      String errorMsg =
          "The call was transmitted successfully, but Amazon S3 couldn't process it, so it returned an error response";
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("%s: %s ", errorMsg, e.getMessage()))
          .build();
    } catch (HttpCloseException | HttpResponseException | URISyntaxException e) {
      return Response.status(500).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
    } catch (SdkClientException e) {
      String errorMsg =
          "Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3";
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("%s: %s ", errorMsg, e.getMessage()))
          .build();
    } catch (ProcessingException e) {
      String errorMsg = "Couldn't convert input stream to a byte array";
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("%s: %s ", errorMsg, e.getMessage()))
          .build();
    } catch (MalformedInputException e) {
      return Response.status(400).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
    } catch (HttpErrorCodeException e) {
      return Utility.processHttpErrorCodeException(e);
    }

    JSONObject obj = new JSONObject();
    obj.put("imgPath", imgPath);
    obj.put("main_pk", eventId);
    System.out.println(String.format("[Events::uploadProfilePic()] %s finished", eventId));

    // image/gif, image/jpg, image/jpeg, image/png,
    return Response.status(200)
        .entity(obj.toString())
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }
}
