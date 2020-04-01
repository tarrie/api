package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.model.User;
import io.tarrie.model.events.EventCondensed;
import io.tarrie.model.condensed.GroupCondensed;
import io.tarrie.model.condensed.UserCondensed;
import io.tarrie.model.consumes.*;
import io.tarrie.model.produces.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * <ul>
 *   <li> Create a user
 *   <li> Delete a user
 *   <li> Get a user by userId
 *   <li> Edit a user
 *   <li>[events] Get the events the user is hosting, rsvp'd , or saved
 *   <li>[events/saved] Save a event
 *   <li>[events/saved] Remove event from saved list
 *   <li>[events/saved] List events user has saved
 *   <li>[events/rsvp] Rsvp to event
 *   <li>[events/rsvp] Remove event from Rsvp
 *   <li>[events/rsvp] List event user has Rsvp
 *   <li>[contacts] Add entity to user contacts
 *   <li>[contacts] List a user's contacts
 *   <li>[contacts] Remove entity from user's contacts
 *   <li>[groups] List groups that user is affiliated with (member, following, Owner, Admin)
 *   <li>[groups/following] User follow a new group
 *   <li>[groups/following] List the groups user is following
 *   <li>[groups/following] Un-follow a group
 *   <li>[groups/member] User member of a new group
 *   <li>[groups/member] List the groups user is a member of
 *   <li>[groups/member] Leave a group
 *   <li>[browsing/groups] view the `viewable' events that a group is hosting
 *   <li>[browsing/explore/events] view the top level events public across the network or across home
 *   <li> ToDo: [browsing/groups/{groupId} DELETE] - Blocks a group from posting on user feed
 *   <li> ToDo: [groups/{groupId}/message POST] - Messages a group
 *   <li>[groups/favorites] add group to favorites
 *   <li>[groups/favorites] remove group from favorites
 *   <li>[groups/favorites] view group favorites
 * </ul>
 */
@Api(tags = "User endpoints")
@SwaggerDefinition(
    tags = {@Tag(name = "User endpoints", description = "Used to create, modify, and get users")})
@Path("/users")
public interface Users {


  /**
   * Check if a collection of users actually exist in tarrie
   * @param userIds collection of groupIds to check
   * @param userId id of user making request
   * @return subset of userIds that exist in Tarrie
   */
  @ApiOperation(value = "Check if a collection of users exist. Returns the list of userIds that exist from query")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/exists")
  @ApiResponses(
          value = {
                  @ApiResponse(code = 201, message = "OK",responseContainer = "List",response = UserId.class),
                  @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
                  @ApiResponse(code = 500, message = "Internal server error")
          })
  Response userExists(@ApiParam(name = "userId", value = "The userId")
                      @QueryParam("userId")
                              Collection<String> userIds, UserId userId);

  /** Create a Tarrie user. */
  @ApiOperation(value = "Creates a user")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Created", response = User.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "User handle or email already exist"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @POST
  Response userCreate(CreateUser createUser);

  /** Deletes a user */
  @Path("{userId}")
  @ApiOperation(value = "Deletes a user")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "User does not exist"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @DELETE
  Response userDelete(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      UserId requesterUserId);

  /** Gets a user */
  @Path("{userId}")
  @ApiOperation(value = "Gets a user")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = User.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 404, message = "User does not exist"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GET
  Response getUser(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      UserId requesterUserId);

  /** Edits a user. */
  @Path("{userId}")
  @ApiOperation(value = "Edit user")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = UserCondensed.class),
        @ApiResponse(code = 404, message = "User does not exist"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response editUser(
      @ApiParam(name = "userId", value = "ID of user that is being edited", required = true)
          @PathParam("userId")
          String userId,
      EditGroup editUser);

  /* ************** Events *********************/

  /** @return the events the user is hosting, rsvp'd , or saved. */
  @Path("{userId}/events")
  @ApiOperation(value = "get the events the user is hosting, rsvp'd , or saved.")
  @GET
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = GetUserEvents.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getUserEvents(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      @DefaultValue("-1")
          @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @DefaultValue("-1")
          @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)")
          @QueryParam("endTime")
          String endDateTimeString);

  /* ** Saved Events **/
  @Path("{userId}/events/saved")
  @ApiOperation(value = "user is saving event")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response saveEvent(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      EventId eventId);

  @Path("{userId}/events/saved/{eventId}")
  @ApiOperation(value = "user removing event from saved list")
  @DELETE
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response unSaveEvent(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      @ApiParam(name = "eventId", value = "ID of event", required = true) @PathParam("eventId")
          String eventId);

  @Path("{userId}/events/saved")
  @ApiOperation(value = "list events user has saved")
  @GET
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = EventCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getSavedEvents(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);



  /* ** Rsvp'd Events **/
  // If event is saved, remove from saved, and add it to rsvp
  @Path("{userId}/events/rsvp")
  @ApiOperation(value = "user is rsvp'n to a event")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = EventCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response rsvpToEvent(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      EventId eventId);

  @Path("{userId}/events/rsvp/{eventId}")
  @ApiOperation(value = "user is un-rsvp'n to event")
  @DELETE
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  Response unRsvpFromEvent(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      @ApiParam(name = "eventId", value = "ID of event", required = true) @PathParam("eventId")
          String eventId);

  /** Needs query parameters on dates maybe groups */
  @Path("{userId}/events/rsvp")
  @ApiOperation(value = "list events in user's rsvp list")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = EventCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getRsvpEvents(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);

  /* **************   Contacts ********************/
  @Path("{userId}/contacts")
  @ApiOperation(value = "add a entity to a users contacts (User and Groups)")
  @PUT
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = UserCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  Response addContact(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);

  @Path("{userId}/contacts")
  @ApiOperation(value = "List a users contacts")
  @GET
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = UserCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  Response getContacts(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);

  /** id in {userId, groupId} */
  @Path("{userId}/contacts/{entityId}")
  @ApiOperation(value = "Remove a entity from a users contacts")
  @DELETE
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response deleteContacts(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      @ApiParam(name = "entityId", value = "ID of entity", required = true) @PathParam("entityId")
          String entityId);

  /* ***** Group ********************/
  @Path("{userId}/groups")
  @ApiOperation(
      value = "List groups that user is affiliated with (member, following, Owner, Admin)")
  @GET
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = GetUserGroups.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  Response getAffiliatedGroups(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);

  /* ************** Group-  following ********************/
  @Path("{userId}/groups/following")
  @ApiOperation(value = "user is following a new group")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = GroupCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response followGroup(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      GroupId groupId);

  @Path("{userId}/groups/following")
  @ApiOperation(value = "list groups a user is following")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = GroupCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getGroupsFollowing(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);
  /**
   * Can't unsubscribe from a group you own without - Transferring ownership - or Deleting the group
   */
  @Path("{userId}/groups/following/{groupId}")
  @ApiOperation(value = "user is un-following a group")
  @DELETE
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response unFollowGroup(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId")
          String groupId);

  /* **************  Group- Club ********************/

  @Path("{userId}/groups/member")
  @ApiOperation(value = "user is requesting to join a group as a group member")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = GroupCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response joinGroup(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      GroupId groupId);

  @Path("{userId}/groups/member")
  @ApiOperation(value = "list groups a user is a member of")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = GroupCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getGroupsUserIsMemberOf(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);

  @Path("{userId}/groups/member/{groupId}")
  @ApiOperation(value = "user is leaving a group")
  @DELETE
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response leaveGroup(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId")
          String groupId);
  /* **************   Browsing ********************/

  /**
   * Uses the requester userId and its group access to filter the events it can show the person
   * Basically only shows the events. From here user can subscribe, message, or Blocks events that
   * are hosted by the group. No input aside from the two id's
   *
   * <p>This is the endpoint to click a group ToDo: Optional input of event id to center on. But you
   */
  @Path("{userId}/browsing/groups/{groupId}")
  @ApiOperation(value = "gets a group public page while a user is browsing")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = ViewGroupEvents.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response viewGroupEvents(
      @DefaultValue("-1")
          @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @DefaultValue("-1")
          @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)")
          @QueryParam("endTime")
          String endDateTimeString,
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("userId")
          String userId);

  /**
   * Returns the top level hastags and the associated events
   *
   * <ul>
   *   <li>Home - following and club members
   *   <li>Discover - events across the network
   * </ul>
   */
  @Path("{userId}/browsing/explore/events")
  @ApiOperation(value = "get the top level hashtags and associated events")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = TopLevelEvents.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response viewTopLevelEvents(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      GetTopLevelEvents getTopLevelEvents);

  // ToDo: BlockGroup - Blocks a group from posting on user feed
  // ToDo: Message Group

  /* **************   Favorites ********************/
  @Path("{userId}/groups/favorite")
  @ApiOperation(value = "add a group to user's favorites")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK", response = GroupCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response addGroupToFavorites(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId,
      GroupId groupId);

  @Path("{userId}/groups/favorite/{groupId}")
  @ApiOperation(value = "remove a group from user's favorites")
  @DELETE
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response removeGroupFromFavorites(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);

  @Path("{userId}/groups/favorite")
  @ApiOperation(value = "get the groups that user's has in favorites")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "OK",
            responseContainer = "List",
            response = GroupCondensed.class),
        @ApiResponse(code = 400, message = "Bad input; missing required attributes"),
        @ApiResponse(code = 401, message = "User not authorized"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response getGroupFavorites(
      @ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId")
          String userId);
}
