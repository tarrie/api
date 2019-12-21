package io.tarrie.api.interfaces;

import io.swagger.annotations.*;
import io.tarrie.api.model.consumes.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * <ul>
 *   <li>add user to group
 *   <li>delete user from group
 *   <li>delete group
 *   <li>create group
 *   <li>change a user's membership
 *   <li>ToDo: Modify group picture, description, name
 * <ul>
 * ToDo: Messaging and email blasting.
 * ToDo: GroupChat?
 */
@Api(tags = "Group endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Groups endpoints", description = "Used to create, modify, and get groups")
    })
@Path("/groups")
public interface Groups {
  /**
   * Invariant: Only Admin's or the owner can add user to group
   *
   * @param addUserToGroup
   * @return
   */
  @Path("/users")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "User does not exist"),
        @ApiResponse(code = 401, message = "User unauthorized to add member"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @PUT
  @ApiOperation(value = "Adds a user to a group as a subscriber")
  Response addUserToGroup(AddUserToGroup addUserToGroup);

  /**
   * Invariant:
   *
   * <ul>
   *   <li>Owner can delete anyone.
   *   <li>Admin's can delete subscriber's but not other Admin's or the Owner
   *   <li>Subscriber's have no power.
   * </ul>
   *
   * @param deleteUserFromGroup
   * @return http response
   */
  @Path("/users")
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
  Response deleteUserFromGroup(DeleteUserFromGroup deleteUserFromGroup);

  /**
   * Create a Tarrie group. Note: Only user can create groups
   *
   * @param createGroup
   * @return
   */
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
  @Path("users/membership")
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
  Response changeMembership(ChangeMembershipGroup changeMembershipGroup);

  /**
   * Delete a group. Note: Only the owner of the group can delete the group
   *
   * @param deleteGroup
   * @return
   */
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful deletion of group"),
        @ApiResponse(code = 404, message = "Group does not  exists"),
        @ApiResponse(code = 401, message = "User unauthorized to delete group"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  Response deleteGroup(DeleteGroup deleteGroup);
}
