package io.tarrie.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
// import io.swagger.v3.oas.annotations.Parameter;
import io.tarrie.controller.Controller;
import io.tarrie.database.contants.EntityTypeEnum;
import io.tarrie.database.contants.EventRelationshipEnum;
import io.tarrie.database.exceptions.HttpErrorCodeException;
import io.tarrie.database.exceptions.ProcessingException;
import io.tarrie.utilities.Utility;
import io.tarrie.model.consumes.UserId;
import org.apache.commons.lang3.EnumUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

// https://www.mkyong.com/tutorials/jax-rs-tutorials/
// http://localhost:8080/api/groups/members
//    * http://localhost:8080/api/groups/members search?query=hello
// ToDo: Make sure u use the userId to find out if person has access
@Api(tags = "Groups endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Groups endpoints", description = "Used to create, modify, and get groups")
    })
@Path("/groups")
public class Groups implements io.tarrie.api.interfaces.Groups {

  /// http://localhost:8080/groups/GRP%23boogoParty/events
  /// http://localhost:8080/groups/GRP%23boogoParty/events?eventRelationship=HOST
  /// were 	%23 == #
  @Path("{groupId}/events")
  @GET
  public Response listGroupEvents(
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId")
          String groupId,
      @DefaultValue("-1")
          @ApiParam(
              name = "eventRelationship",
              allowableValues = "HOST, SAVED, RSVP",
              value = "eventRelationship to query")
          @QueryParam("eventRelationship")
          String eventRelationship,
      @DefaultValue("-1")
          @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @DefaultValue("-1")
          @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)")
          @QueryParam("endTime")
          String endDateTimeString) {

    if ((!(Utility.isIdValid(groupId, EntityTypeEnum.GRP)))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Bad input; groupid is not valid:%s ", groupId))
          .build();
    }

    Map<String, List<Map<String, Object>>> payload;

    try {

      if (eventRelationship.equals("-1")) {
        payload = Controller.getEvents(groupId);
      } else if (EnumUtils.isValidEnum(EventRelationshipEnum.class, eventRelationship)) {
        payload = Controller.getEvents(groupId, EventRelationshipEnum.valueOf(eventRelationship));
      } else {
        return Response.status(400)
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity(
                String.format(
                    "Bad input; eventRelationship param is not valid:%s ", eventRelationship))
            .build();
      }

    } catch (HttpErrorCodeException e) {
      return Utility.processHttpErrorCodeException(e);
    } catch (ProcessingException e) {
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("Processing Exception; %s", e.getMessage()))
          .build();
    }
    return Response.status(200).entity(payload).type(MediaType.APPLICATION_JSON_TYPE).build();
  }
}
