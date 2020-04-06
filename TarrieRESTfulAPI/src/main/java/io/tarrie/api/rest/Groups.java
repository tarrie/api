package io.tarrie.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import io.swagger.annotations.*;
// import io.swagger.v3.oas.annotations.Parameter;
import io.tarrie.controller.Controller;
import io.tarrie.database.contants.ImgTypes;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.Entity;
import io.tarrie.model.consumes.AddUserToGroup;
import io.tarrie.Utility;
import io.tarrie.model.consumes.UserId;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// https://www.mkyong.com/tutorials/jax-rs-tutorials/
// http://localhost:8080/api/groups/members
//    * http://localhost:8080/api/groups/members search?query=hello
// ToDo: Make sure u use the userId to find out if person has access
@Api(tags = "Groups endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Groups endpoints", description = "Used to create, modify, and get groups")
    })
@Path("/groups")
public class Groups implements io.tarrie.api.interfaces.Groups {

  @Path("{groupId}/events")
  @GET
  public Response listGroupEvents(
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId")
          String groupId,
      @DefaultValue("-1")
          @ApiParam(name = "startTime", value = "start time in (ISO 8601 format)")
          @QueryParam("startTime")
          String startDateTimeString,
      @DefaultValue("-1")
          @ApiParam(name = "endTime", value = "end time in (ISO 8601 format)")
          @QueryParam("endTime")
          String endDateTimeString,
      UserId requesterUserId) {

    String payload;

    if ((!(Utility.isIdValid(groupId))) || (!(Utility.isIdValid(requesterUserId.getUserId())))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Bad input; groupid or userid is not valid")
          .build();
    }

    try {
      payload = Utility.pojoToJson(Controller.getHostedEvents(groupId));
    } catch (JsonProcessingException e) {
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Internal Server error; could not convert pojo to json")
          .build();
    }
    return Response.status(200).entity(payload).type(MediaType.APPLICATION_JSON_TYPE).build();
  }

  @Path("{groupId}/images/profile")
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadProfilePic(
      @ApiParam(name = "groupId", value = "ID of group", required = true) @PathParam("groupId")
          String groupId,
      @FormDataParam("file") FormDataBodyPart fileBody,
      @FormDataParam("userId") FormDataBodyPart userIdBody) {

    String mimeType = fileBody.getMediaType().toString();
    String userId = userIdBody.getValue();
    InputStream is = fileBody.getValueAs(InputStream.class);

    if (!(ImgTypes.ACCEPTABLE_MIME_IMAGES.contains(mimeType))) {
      String errorMsg =
          String.format(
              "Invalid MimeType must be one of: %s", ImgTypes.ACCEPTABLE_MIME_IMAGES.toString());
      return Response.status(400).entity(errorMsg).type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    if ((!(Utility.isIdValid(groupId))) || (!(Utility.isIdValid(userId)))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Bad input; groupid or userid is not valid")
          .build();
    }

    String imgPath;

    try {
      imgPath=Controller.uploadProfileImg(is, mimeType, groupId);

      if (imgPath ==null){
          return Response.status(500)
                  .type(MediaType.TEXT_PLAIN_TYPE)
                  .entity("Internal Server error: upload failed")
                  .build();
      }
    } catch (Exception e) {
      return Response.status(400)
              .type(MediaType.TEXT_PLAIN_TYPE)
              .entity(e.getMessage()).build();
    }

      JSONObject obj = new JSONObject();
      obj.put("imgPath",imgPath);
      obj.put("id",groupId);


    // image/gif, image/jpg, image/jpeg, image/png,
    return Response.status(200)
            .entity(obj.toString())
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build();
  }
}
