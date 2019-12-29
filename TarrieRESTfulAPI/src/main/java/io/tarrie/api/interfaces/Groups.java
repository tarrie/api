package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.api.model.Event;
import io.tarrie.api.model.Group;
import io.tarrie.api.model.consumes.UserId;
import io.tarrie.api.model.consumes.*;
import io.tarrie.api.model.produces.GroupCondensed;
import io.tarrie.api.model.produces.UserCondensed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 *
 *
 * <ul>
 *   <li>get a group by group id
 *   <li>add user to group
 *   <li>delete user from group
 *   <li>delete group
 *   <li>create group
 *   <li>change a user's membership
 *   <li>edit group info *
 *   <li>ToDo: invite user to join a group via Name, email address, or handle -- similar to Github
 *       -- uses ElasticSearch
 *   <li>ToDo: Email blast members of the group
 *   <li>ToDo: Message the members of the group
 *   <li? ToDo: Chat with group
 * </ul>
 */
@Api(tags = "Group endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Groups endpoints", description = "Used to create, modify, and get groups")
    })
@Path("/groups")
public interface Groups {

    /**
     * Gets a group
     * @param userId userId of the requester
     * @return pojo that represents a group
     */
    @Path("{groupId}")
    @ApiOperation(value = "Gets a group")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK", response = Groups.class),
                    @ApiResponse(code = 404, message = "Group does not exist"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 500, message = "Internal server error")
            })
    @GET
    Response getGroup(@ApiParam(name = "groupId", value = "ID of group", required = true)
                      @PathParam("groupId") String groupId, UserId userId);


  /**
   * Edits a group. Only a admin or a owner can edit a group
   * @param groupId id of group to edit
   * @param editGroup pojo of editable items
   * @return the group w/ new edits reflected
   */
  @Path("{groupId}")
  @ApiOperation(value = "Edit group")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
          value = {
                  @ApiResponse(
                          code = 200,
                          message = "OK",
                          response = GroupCondensed.class),
                  @ApiResponse(code = 404, message = "Group does not exist"),
                  @ApiResponse(code = 401, message = "User unauthorized to edit group"),
                  @ApiResponse(code = 500, message = "Internal server error")
          })
  Response editGroup(@ApiParam(name = "groupId", value = "ID of group", required = true)
                     @PathParam("groupId") String groupId,
                     EditGroup editGroup);

  /**
   * Adds user['s] to a group. <br>
   * Invariant: Only Admin's or the owner can add user to group
   *
   * @param addUserToGroup  pojo that the caller has to fill out as payload
   * @return the list of users that have been successfully added
   */
  @Path("{groupId}/users")
  @ApiOperation(value = "Adds a user to a group as a subscriber. Returns the list of userIds that have been successfully added")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
              @ApiResponse(
                      code = 200,
                      message = "OK",
                      responseContainer = "List",
                      response = UserCondensed.class),
        @ApiResponse(code = 404, message = "User does not exist"),
        @ApiResponse(code = 401, message = "User unauthorized to add member"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @PUT
  Response addUserToGroup(@ApiParam(name = "groupId", value = "ID of group", required = true)
                          @PathParam("groupId") String groupId, AddUserToGroup addUserToGroup);


  /**
   * Deletes a user from a group <br>
   * Invariants:
   * <ul>
   *   <li>Owner can delete anyone.
   *   <li>Admin's can delete subscriber's but not other Admin's or the Owner
   *   <li>Subscriber's have no power.
   * </ul>
   *
   * @param adminUserId userId of the admin (or owner) initiating the request
   * @param groupId id of the group in question
   * @param deletedUserId id of the user that's going to be deleted
   * @return http response
   */
  @Path("{groupId}/users/{userId}")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "User does not exist"),
        @ApiResponse(code = 401, message = "User unauthorized to delete member"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiOperation(value = "Deletes a user from a group")
  Response deleteUserFromGroup(@ApiParam(name = "groupId", value = "ID of group", required = true)
                               @PathParam("groupId") String groupId,
                               @ApiParam(name = "userId", value = "ID of user to be deleted from group", required = true)
                               @PathParam("userId") String deletedUserId,
                               UserId adminUserId);

  /**
   * Create a Tarrie group. <br>
   * Note: Only a user can create a group
   *
   * @param createGroup pojo input payload
   * @return response
   */
  @ApiOperation(value = "Creates a group")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful creation of group", response = Groups.class),
        @ApiResponse(code = 404, message = "Group handle already exists"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @POST
  Response createGroup(CreateGroup createGroup);

  /**
   * Change membership type of existing group member
   *
   * @param changeMembershipGroup
   * @return
   */
  @ApiOperation(value = "Change membership of a group member")
  @Path("{groupId}/users/{userId}/membership")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @PUT
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful change of membership"),
        @ApiResponse(code = 404, message = "User is not in group or does not exist"),
        @ApiResponse(code = 401, message = "User unauthorized to change membership"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response changeMembership(@ApiParam(name = "groupId", value = "ID of group", required = true)
                            @PathParam("groupId") String groupId,
                            @ApiParam(name = "userId", value = "ID of user having membership adjusted", required = true)
                            @PathParam("userId") String changedUserId,
                            ChangeMembershipGroup changeMembershipGroup);


  /**
   * Delete a group. Note: Only the owner of the group can delete the group
   *
   * @param ownerUserId id of user initializing the request
   * @param groupId id of group to be deleted
   * @return
   */
  @ApiOperation(value = "Deletes a group")
  @Path("{groupId}")
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful deletion of group"),
        @ApiResponse(code = 404, message = "Group does not  exists"),
        @ApiResponse(code = 401, message = "User not authorized to delete group"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response deleteGroup(@ApiParam(name = "groupId", value = "ID of group", required = true)
                       @PathParam("groupId") String groupId, UserId ownerUserId);

  /* ******************* Events *************/

  /**
   * <ul>
   *   <li>[] create group
   *   <li>[{groupId}] get a group by group id
   *   <li>[users] add user to group
   *   <li>[users/{userId}] delete user from group
   *   <li>[{groupId}] delete group
   *   <li>[{groupId}/users/{userId}/membership] change a user's membership
   *   <li>[{groupId}] edit group info
   *   <li>[{groupId}/events/{eventId}] get a event group is hosting
   *   <li>[{groupId}/events] create a event in a group
   *   <li>[{groupId}/events/{eventId}] delete a event group is hosting
   *   <li>[{groupId}/events/{eventId}] edit a existing event group is hosting
   *   <li>[{groupId}/events] list events group is hosting - filterable by date
   *   <li>ToDo: invite user to join a group via Name, email address, or handle -- similar to Github
   *       -- uses ElasticSearch
   *   <li>ToDo: Email blast members of the group
   *   <li>ToDo: Message the members of the group
   *   <li? ToDo: Chat with group clubMembers
   * </ul>
   */

  /**
   * Get a event that group is hosting
   *
   * @param userId userId userId of the requester
   * @return pojo that represents a event
   */
  @ApiOperation(value = "Get a event a group is hosting")
  @Path("{groupId}/events/{eventId}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "OK", response = Event.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User unauthorized to view event"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getEvent(UserId userId);

    /**
     * Create event for a group
     *
     * <ul>
     *   <li>Admin's and Owner of a group can edit a event
     *   <li>Otherwise, only the user creator can edit the event
     * </ul>
     */
    @ApiOperation(value = "Create a event")
    @Path("{groupId}/events")
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
     * Delete a event a group is hosting
     *
     * @param eventId id of event to delete
     * @param userId id of user issuing delete event request
     * @return response
     */
    @ApiOperation(value = "Delete a event")
    @Path("{groupId}/events/{eventId}")
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
            @ApiParam(name = "groupId", value = "ID of group", required = true)
            @PathParam("groupId")
                    String groupId,
            UserId userId);

    /**
     * Edit a group existing event
     *
     * @param eventId id of event to edit
     * @param editEvent pojo of editable attributes of the event
     * @return response
     */
    @ApiOperation(value = "Edit a event")
    @Path("{groupId}/events/{eventId}")
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
            @ApiParam(name = "groupId", value = "ID of group", required = true)
            @PathParam("groupId")
                    String groupId,
            EditEvent editEvent);

    /**
     * List events thrown by group
     *
     * @param groupId id of group
     * @param startDateTimeString (optional) query filter parameter of start time (ISO 8601 format)
     * @param endDateTimeString (optional) query filter parameter of end time (ISO 8601 format)
     * @param requesterUserId id of user requesting the information
     * @return response
     */
    @ApiOperation(value = "List Events thrown by a group")
    @Path("{groupId}/events")
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
            @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId")
                    String groupId,
            @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
            @QueryParam("startTime")
                    String startDateTimeString,
            @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)") @QueryParam("endTime")
                    String endDateTimeString,
            UserId requesterUserId);

}
