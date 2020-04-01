package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.model.consumes.SearchEvent;
import io.tarrie.model.consumes.UserId;
import io.tarrie.model.events.EventCondensed;
import io.tarrie.model.condensed.GroupCondensed;
import io.tarrie.model.condensed.UserCondensed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * <ul>
 *     <li> Search for groups based on diff group attributes
 *     <li> Search for users based on diff user attributes
 *     <li> Search for events based on diff attributes
 * </ul>
 * ToDo: Introduce pagination.
 */
@Api(tags = "Search endpoints")
@SwaggerDefinition(
    tags = {@Tag(name = "Search endpoints", description = "Used to search for groups, users, or events")})
@Path("/search")
public interface Search {

  /**
   *
   * @param groupNames (optional) groupName to search on
   * @param groupBio (optional) groupBio to search on
   * @param groupIds (optional) groupId to search on
   * @param searcherUserId the id of user doing the searching
   * @return list of groups that match the search
   */
  @Path("/groups")
  @ApiOperation(value = "Searches for Tarrie entity based on different attributes")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
          value = {
                  @ApiResponse(
                          code = 200,
                          message = "OK",
                          responseContainer = "List",
                          response = GroupCondensed.class),
                  @ApiResponse(code = 500, message = "Internal server error")
          })
  @GET
  Response searchForGroup(
          @DefaultValue("-1") @ApiParam(name = "groupName", value = "The groupName of group to search on")
          @QueryParam("groupName")
                  Collection<String> groupNames,
          @DefaultValue("-1") @ApiParam(name = "groupBio", value = "The group description of group to search on")
          @QueryParam("groupBio")
                  String groupBio,
          @DefaultValue("-1") @ApiParam(name = "groupId", value = "The groupId of group to search on")
          @QueryParam("groupId")
                  Collection<String> groupIds,
          UserId searcherUserId);

  /**
   *
   * @param emailAddress (optional) emailAddress to search on
   * @param userName (optional) firstName to search on
   * @param userId (optional) userId to search on
   * @param searcherUserId the id of user doing the searching
   * @return list of entities that match the search
   */
  @Path("/users")
  @ApiOperation(value = "Searches for Tarrie entity based on different attributes")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
          value = {
                  @ApiResponse(
                          code = 200,
                          message = "OK",
                          responseContainer = "List",
                          response = UserCondensed.class),
                  @ApiResponse(code = 500, message = "Internal server error")
          })
  @GET
  Response searchForUser(
          @DefaultValue("-1") @ApiParam(name = "emailAddress", value = "The email address to search on")
          @QueryParam("emailAddress")
                  String emailAddress,
          @DefaultValue("-1") @ApiParam(name = "userName", value = "The name of user to search on")
          @QueryParam("userName")
                  String userName,
          @DefaultValue("-1") @ApiParam(name = "userId", value = "The userId of user to search on") @QueryParam("userId")
                  String userId,
          UserId searcherUserId);

  /**
   * ToDo: Fuzzy matching for hashTags
   * Searches for Tarrie event based on different attributes
   * @param startDateTimeString (optional) start of time interval
   * @param endDateTimeString (optional)  end of time interval
   * @param eventNames (optional) name of event
   * @param userIds (optional) Search for events hosted by userId
   * @param groupIds (optional) Search for events hosted by groupId
   * @param hashTags (optional) Searcb events associated with a hashTag
   * @param searchEvent pojo w/ userId of requestor and optional search location
   * @return
   */
  @Path("/events")
  @ApiOperation(value = "Searches for Tarrie event based on different attributes")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(
          value = {
                  @ApiResponse(
                          code = 200,
                          message = "OK",
                          responseContainer = "List",
                          response = EventCondensed.class),
                  @ApiResponse(code = 500, message = "Internal server error")
          })
  @GET
  Response searchForEvents(
          @DefaultValue("-1") @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
                  String startDateTimeString,
          @DefaultValue("-1") @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)") @QueryParam("endTime")
                  String endDateTimeString,
          @DefaultValue("-1") @ApiParam(name = "eventNames", value = "The eventName to search on")
          @QueryParam("eventNames")
                  Collection<String>  eventNames,
          @DefaultValue("-1") @ApiParam(name = "userIds", value = "Search for events hosted by userIds") @QueryParam("userIds")
                  Collection<String>  userIds,
          @DefaultValue("-1") @ApiParam(name = "groupIds", value = "Search for events hosted by groupIds") @QueryParam("groupIds")
                  Collection<String> groupIds,
          @DefaultValue("-1") @ApiParam(name = "hashtags", value = "hashTags to search on") @QueryParam("hashtags")
                  Collection<String> hashTags,
          SearchEvent searchEvent);


}
