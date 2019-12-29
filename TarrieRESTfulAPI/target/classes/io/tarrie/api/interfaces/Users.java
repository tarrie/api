package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.api.model.Group;
import io.tarrie.api.model.User;
import io.tarrie.api.model.consumes.CreateGroup;
import io.tarrie.api.model.consumes.CreateUser;
import io.tarrie.api.model.consumes.EditGroup;
import io.tarrie.api.model.consumes.UserId;
import io.tarrie.api.model.produces.UserCondensed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 *
 * <ul>
 *   <li>[favorites] add group to favorites
 *   <li>[favorites] remove group from favorites
 *   <li>[favorites] view group favorites
 *   <li>[browsing] view the `viewable' events that a group is hosting
 *   <li>[browsing] view the top level events public across the network
 *   <li>[browsing] ToDo: view the top level events that following- Filterable by clubMember, Admin,
 *       Owner
 *       <ul>
 *         <li>@Path("{userId}/browsing/home/events")
 *       </ul>
 *   <li>Add group to favorite
 *   <li>view event by hashtag
 *   <li>remove group from favorite
 *   <li>get all group favorites
 * </ul>
 */
@Api(tags = "User endpoints")
@SwaggerDefinition(
    tags = {@Tag(name = "User endpoints", description = "Used to create, modify, and get users")})
@Path("/users")
public interface Users {

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
  @GET
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
  Response getUserEvents(
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
  Response saveEvent(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  @Path("{userId}/events/saved/{eventId}")
  @ApiOperation(value = "user is deleting saved event")
  @DELETE
  Response unSaveEvent(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId,
                       @ApiParam(name = "eventId", value = "ID of event", required = true) @PathParam("eventId") String eventId);

  @Path("{userId}/events/saved")
  @ApiOperation(value = "list events user has saved")
  @GET
  Response getSavedEvents(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  /* ** Rsvp'd Events **/
  // If event is saved, remove from saved, and add it to rsvp
  @Path("{userId}/events/rsvp")
  @ApiOperation(value = "user is rsvp'n to a event")
  @PUT
  Response rsvpToEvent();

  @Path("{userId}/events/rsvp/{eventId}")
  @ApiOperation(value = "user is un-rsvp'n to event")
  @DELETE
  Response unRsvpFromEvent(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId,
                           @ApiParam(name = "eventId", value = "ID of event", required = true) @PathParam("eventId") String eventId );

  /** Needs query parameters on dates maybe groups */
  @Path("{userId}/events/rsvp")
  @ApiOperation(value = "list events in user's rsvp list")
  @GET
  Response getRsvpEvents(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  /* **************   Contacts ********************/
  @Path("{userId}/contacts")
  @ApiOperation(value = "add a entity to a users contacts (User and Groups)")
  @PUT
  Response addContact(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  @Path("{userId}/contacts")
  @ApiOperation(value = "List a users contacts")
  @GET
  Response getContacts(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  /** id in {userId, groupId} */
  @Path("{userId}/contacts/{entityId}")
  @ApiOperation(value = "Remove a entity from a users contacts")
  @DELETE
  Response deleteContacts(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId,
                          @ApiParam(name = "entityId", value = "ID of entity", required = true) @PathParam("entityId") String entityId);

  /* ***** Group ********************/
  @Path("{userId}/groups")
  @ApiOperation(value = "List groups that user is affilated with (member, following, Owner, Admin)")
  @GET
  Response getAffiliatedGroups(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  /* ************** Group-  following ********************/
  @Path("{userId}/groups/following")
  @ApiOperation(value = "user is following a new group")
  @PUT
  Response followGroup(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  @Path("{userId}/groups/following")
  @ApiOperation(value = "list groups a user is following")
  @GET
  Response getGroupsFollowing(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);
  /**
   * Can't unsubscribe from a group you own without - Transferring ownership - or Deleting the group
   */
  @Path("{userId}/groups/following/{groupId}")
  @ApiOperation(value = "user is un-following a group")
  @DELETE
  Response unFollowGroup(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId,
                         @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId") String groupId);

  /* **************  Group- Club ********************/
  @Path("{userId}/groups/member")
  @ApiOperation(value = "user is joining a group as a group member")
  @PUT
  Response joinGroup(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  @Path("{userId}/groups/member")
  @ApiOperation(value = "list groups a user is a member of")
  @GET
  Response getGroupsUserIsMemberOf(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  @Path("{userId}/groups/member/{groupId}")
  @ApiOperation(value = "user is leaving a group")
  @DELETE
  Response leaveGroup(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId,
                      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId") String groupId);
  /* **************   Browsing ********************/

  /**
   * Uses the requester userId and its group access to filter the events it can show the person
   * Basically only shows the events. From here user can subscribe, message, or Blocks events that
   * are hosted by the group. No input aside from the two id's
   *
   * <p>This is the endpoint to click a group
   * ToDo: Optional input of event id to center on. But you
   */
  @Path("{userId}/browsing/groups/{groupId}")
  @ApiOperation(value = "gets a group public page while a user is browsing")
  @GET
  Response viewGroupEvents(
      @DefaultValue("-1")
          @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @DefaultValue("-1")
          @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)")
          @QueryParam("endTime")
          String endDateTimeString,
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("userId") String userId);

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
  Response viewTopLevelEvents(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  // ToDo: BlockGroup - Blocks a group from posting on user feed
  // ToDo: Message Group

  /* **************   Favorites ********************/
  @Path("{userId}/groups/favorite")
  @ApiOperation(value = "add a group to user's favorites")
  @PUT
  Response addGroupToFavorites(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  @Path("{userId}/groups/favorite/{groupId}")
  @ApiOperation(value = "remove a group from user's favorites")
  @DELETE
  Response removeGroupFromFavorites(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);

  @Path("{userId}/groups/favorite")
  @ApiOperation(value = "get the groups that user's has in favorites")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  Response getGroupFavorites(@ApiParam(name = "userId", value = "ID of user", required = true) @PathParam("userId") String userId);
}
