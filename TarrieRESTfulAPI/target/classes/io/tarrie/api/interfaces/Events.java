package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.api.model.*;
import io.tarrie.api.model.consumes.CreateEvent;
import io.tarrie.api.model.consumes.UserId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 *
 *
 * <ul>
 *   <li>ToDo: shareEvent via email and via Tarrie
 *       <ul>
 */
@Api(tags = "Events endpoint")
@SwaggerDefinition(
    tags = {@Tag(name = "Events endpoint", description = "Used to create, modify, and get events")})
@Path("/events")
public interface Events {

  /**
   * Create event
   *
   * <ul>
   *   <li>Admin's and Owner of a group can edit a event
   *   <li>Otherwise, only the user creator can edit the event
   * </ul>
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Created", response = Event.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User unauthorized to create event"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @ApiOperation(value = "Create a event")
  Response createEvent(CreateEvent createEvent);

  @Path("{eventId}")
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Deleted"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User unauthorized to delete event"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @ApiOperation(value = "Delete a event")
  Response deleteEvent(
      @ApiParam(name = "eventId", value = "ID of event to delete", required = true)
          @PathParam("eventId")
          String eventId,
      UserId userId);

  @GET
  @Path("groups/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "List Events thrown by a group")
  Response listGroupEvents(
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId")
          String groupId,
      @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)") @QueryParam("endTime")
          String endDateTimeString,
      @ApiParam UserId userId );

  // public Response getMsg(
  // @QueryParam("query") String queryParam,
  // @QueryParam("language") String languageParam,
  // @QueryParam("date") String dateParam,
  // @QueryParam("count") String countParam,
  // @QueryParam("offset") String offsetParam)

  /** Share a event with User or Group */
  Response eventShare(Entity tarrieEntity, Collection<Entity> entitiesToShareWith);
}
