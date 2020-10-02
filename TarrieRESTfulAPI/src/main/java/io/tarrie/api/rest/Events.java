package io.tarrie.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import io.tarrie.database.exceptions.HttpCloseException;
import io.tarrie.database.exceptions.HttpErrorCodeException;
import io.tarrie.database.exceptions.HttpResponseException;
import io.tarrie.utilities.Utility;
import io.tarrie.controller.Controller;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.consumes.CreateEvent;
import io.tarrie.model.events.Event;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

// http://localhost:8080/api/events
@Api(tags = "Events endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Events endpoints", description = "Used to create, modify, and get events")
    })
@Path("/events")
public class Events implements io.tarrie.api.interfaces.Events {

  // https://crunchify.com/create-very-simple-jersey-rest-service-and-send-json-data-from-java-client/
  @ApiOperation(value = "Create a event")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Created", response = Event.class),
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
      } catch (HttpCloseException | HttpResponseException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | URISyntaxException e) {
        return Response.status(500).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
      } catch (HttpErrorCodeException e) {
        Map<String, String> errorMap = HttpErrorCodeException.ErrorMsgToMap(e);
        return Response.status(Integer.parseInt(errorMap.get("code")))
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity(errorMap.get("message"))
            .build();
      }
      payload = Utility.pojoToJson(eventCondensed);

    } catch (MalformedInputException e) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Bad input; %s", e.getMessage()))
          .build();
    } catch (JsonProcessingException e) {
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Internal Server error; could not convert pojo to json")
          .build();
    } catch (IOException e) {
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Internal Server error; could not convert map to json")
          .build();
    }

    return Response.status(201).entity(payload).type(MediaType.APPLICATION_JSON_TYPE).build();
  }
}
