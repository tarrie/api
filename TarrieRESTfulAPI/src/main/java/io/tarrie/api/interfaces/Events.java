package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.model.consumes.CreateEvent;
import io.tarrie.model.consumes.EditEvent;
import io.tarrie.model.consumes.EventId;
import io.tarrie.model.consumes.UserId;
import io.tarrie.model.events.Event;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 *
 *
 * <ul>
 *   <li>get a event by id - query parameter is eventId
 *   <li>edit event. ToDo: In creating event we need rich text editor.
 *   <li>create event
 *   <li>delete event
 *   <li>ToDo: Write on the events page??? --- Probably won't implement. This is last.
 *   <li>ToDo: shareEvent via email - Needs email service
 *   <li>ToDo: shareEvent via Tarrie - Needs notification object and SNS
 *   <li>ToDo: List related events to a given Hashtag - Needs ML + clustering
 *   <li>ToDo: Support for multiple images when creating a event, and to rearrange order of imgs in
 *       creation or edits
 *   <li>ToDo: Attach file to event -- but this can just be a cloud link
 * </ul>
 *
 * ToDo: Introduce pagination.
 */
@Api(tags = "Events endpoint")
@SwaggerDefinition(
    tags = {@Tag(name = "Events endpoint", description = "Used to create, modify, and get events")})
@Path("/events")
public interface Events {

  /**
   * Check if a collection of events actually exist in tarrie
   * @param eventIds collection of groupIds to check
   * @param userId id of user making request
   * @return subset of eventIds that exist in Tarrie
   */
  @ApiOperation(value = "Check if a collection of events exist. Returns the list of eventIds that exist from query")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/exists")
  @ApiResponses(
          value = {
                  @ApiResponse(code = 201, message = "OK",responseContainer = "List",response = EventId.class),
                  @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
                  @ApiResponse(code = 500, message = "Internal server error")
          })
  Response eventExists(@ApiParam(name = "eventId", value = "The eventId")
                      @QueryParam("eventId")
                              Collection<String> eventIds, UserId userId);

  /**
   * Get event by event id
   *
   * @param userId userId userId of the requester
   * @return pojo that represents a event
   */
  @ApiOperation(value = "Get a event")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "OK", response = Event.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getEvent(
      @ApiParam(name = "eventId", value = "eventId to query on", required = true)
          @QueryParam("eventId")
          Collection<String> eventIds,
      UserId userId);

  /**
   * Create event
   *
   * <ul>
   *   <li>Admin's and Owner of a group can edit a event
   *   <li>Otherwise, only the user creator can edit the event
   * </ul>
   */
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
  Response createEvent(CreateEvent createEvent);

  /**
   * Delete a event
   *
   * @param eventId id of event to delete
   * @param userId id of user issuing delete event request
   * @return response
   */
  @ApiOperation(value = "Delete a event")
  @Path("{eventId}")
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "No content; event does not exist"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response deleteEvent(
      @ApiParam(name = "eventId", value = "ID of event", required = true) @PathParam("eventId")
          String eventId,
      UserId userId);

  /**
   * Edit a existing event
   *
   * @param eventId id of event to edit
   * @param editEvent pojo of editable attributes of the event
   * @return response
   */
  @ApiOperation(value = "Edit a event")
  @Path("{eventId}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "No content; event does not exist"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response editEvent(
      @ApiParam(name = "eventId", value = "ID of event", required = true) @PathParam("eventId")
          String eventId,
      EditEvent editEvent);
}
