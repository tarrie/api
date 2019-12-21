package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.api.model.*;
import io.tarrie.api.model.consumes.CreateEvent;
import io.tarrie.api.model.consumes.EditEvent;
import io.tarrie.api.model.consumes.UserId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * <ul>
 *   <li>edit event
 *   <li>create event
 *   <li>delete event
 *   <li>get events hosted by a group - multiple groups allowed
 *   <li>get event hosted by a user - multiple users allowed
 *   <li>get events with a given hashtag - multiple hashtags allowed
 *   <li>ToDo: shareEvent via email - Needs email service
 *   <li>ToDo: shareEvent via Tarrie - Needs notification object and SNS
 *   <li>ToDo: List related events to a given Hashtag - Needs ML + clustering
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
        @ApiResponse(code = 401, message = "User unauthorized to create event"),
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
        @ApiResponse(code = 200, message = "Deleted"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "No content; event does not exist"),
        @ApiResponse(code = 401, message = "User unauthorized to delete event"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response deleteEvent(
      @ApiParam(name = "eventId", value = "ID of event to delete", required = true)
          @PathParam("eventId")
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
        @ApiResponse(code = 401, message = "User unauthorized to edit event"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response editEvent(
      @ApiParam(name = "eventId", value = "ID of event to delete", required = true)
          @PathParam("eventId")
          String eventId,
      EditEvent editEvent);

  /**
   * List events thrown by a groups
   *
   * @param groupIds id of group
   * @param startDateTimeString (optional) query filter parameter of start time (ISO 8601 format)
   * @param endDateTimeString (optional) query filter parameter of end time (ISO 8601 format)
   * @param requesterUserId id of user requesting the information
   * @return response
   */
  @ApiOperation(value = "List Events thrown by a group")
  @Path("/groups")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = Event.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User unauthorized to view event"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response listGroupEvents(
      @ApiParam(name = "groupId", value = "ID of group", required = true) @QueryParam("groupId")
          Collection<String> groupIds,
      @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)") @QueryParam("endTime")
          String endDateTimeString,
      UserId requesterUserId);

  /**
   * List events hosted by users. Default behavior is to paginate the number of events. So it returns
   * x amount of events from today onward. ToDo: Pagination Constants
   *
   * @param viewUserIds ids of user with events
   * @param startDateTimeString (optional) query filter parameter of start time (ISO 8601 format)
   * @param endDateTimeString (optional) query filter parameter of end time (ISO 8601 format)
   * @param requesterUserId id of user requesting the information
   * @return response
   */
  @ApiOperation(value = "List Events thrown by a user")
  @GET
  @Path("/users")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = Event.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User unauthorized to view event"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response listUserEvents(
      @ApiParam(name = "userId", value = "ID of user", required = true) @QueryParam("userId")
          Collection<String> viewUserIds,
      @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)") @QueryParam("endTime")
          String endDateTimeString,
      UserId requesterUserId);

  /**
   * List events associated with hashTags. Uses ElasticSearch. Can search on multiple
   * hashtags (e.g /events/hashtags/query?hashtag=yeet&hashtag=free%20food)
   * ToDo: ElasticSearch fuzzy matching for misspelled hashTags
   * @param hashTag the hastag string to search on
   * @param requesterUserId the id of user requesting the search
   * @return response
   */
  @GET
  @Path("/hashtags")
  @ApiOperation(value = "List events associated with a hashTag")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
          value = {
                  @ApiResponse(
                          code = 200,
                          message = "OK",
                          responseContainer = "List",
                          response = Event.class),
                  @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
                  @ApiResponse(code = 500, message = "Internal server error")
          })
  Response listEventsByHashTag(@ApiParam(name = "hashtag", value = "hashTag to search on", required = true) @QueryParam("hashtag")
                                       Collection<String> hashTag, UserId requesterUserId);
}
