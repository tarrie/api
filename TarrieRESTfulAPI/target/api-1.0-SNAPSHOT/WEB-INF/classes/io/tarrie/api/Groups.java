package io.tarrie.api;

import io.swagger.annotations.*;
//import io.swagger.v3.oas.annotations.Parameter;
import io.tarrie.api.model.consumes.AddUserToGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

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
   *
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
          AddUserToGroup addUserToGroup) {
    System.out.println(addUserToGroup.groupId);

    System.out.println("hefh");
    System.out.println(addUserToGroup);



    return Response.status(200).entity("SUCCESS").build();
  }
}
