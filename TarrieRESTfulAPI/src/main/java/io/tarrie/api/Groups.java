package io.tarrie.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
//import io.swagger.v3.oas.annotations.Parameter;
import io.tarrie.api.model.consumes.AddUserToGroup;
import io.tarrie.Utility;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

// https://www.mkyong.com/tutorials/jax-rs-tutorials/
//    * http://localhost:8080/api/search?query=hello
@Api(tags = "Groups endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Groups endpoints", description = "Used to create, modify, and get groups")
    })
@Path("/groups")
public class Groups {

  /**
   * Adds a member to a group
   *https://github.com/swagger-api/swagger-core/wiki/annotations
   * @return
   * @throws IOException
   */
  @Path("/members")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @ApiOperation(value = "Adds a user to a group as a club member")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful addition to group"),
        @ApiResponse(code = 401, message = "Not Authorized to add User to group"),
        @ApiResponse(code = 500, message = "Internal Error")
      })
  public Response addMemberToGroup(
          AddUserToGroup addUserToGroup) throws JsonProcessingException {
    System.out.println(addUserToGroup.adminUserId);
    System.out.println(Utility.pojoToJson(addUserToGroup));

    return Response.status(200).entity("SUCCESS").build();
  }
}
