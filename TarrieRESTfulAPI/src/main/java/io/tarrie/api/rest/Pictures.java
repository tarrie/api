package io.tarrie.api.rest;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.tarrie.utilities.Utility;
import io.tarrie.controller.Controller;
import io.tarrie.database.contants.ImgTypes;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/** Create event - {ProfileImg, EventTime, Location} - Promote Event: */
@Api(tags = "Groups endpoints")
@SwaggerDefinition(
    tags = {
      @Tag(name = "Groups endpoints", description = "Used to create, modify, and get groups")
    })
@Path("/pictures")
public class Pictures {

  /**
   * Uploads profile image to entity.
   *
   * @param entityId
   * @param fileBody
   * @param userIdBody
   * @return
   */
  @Path("/profile/{entityId}")
  @PUT
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadProfilePic(
      @ApiParam(name = "entityId", value = "ID of entity", required = true) @PathParam("entityId")
          String entityId,
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

    if ((!(Utility.isIdValid(entityId))) || (!(Utility.isIdValid(userId)))) {
      return Response.status(400)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Bad input; eventId or userId is not valid")
          .build();
    }

    String imgPath;
    try {
      imgPath = Controller.uploadProfileImg(is, mimeType, entityId);

      if (imgPath == null) {
        return Response.status(500)
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity("Internal Server error: upload failed")
            .build();
      }
    } catch (AmazonServiceException e) {
      String errorMsg =
          "The call was transmitted successfully, but Amazon S3 couldn't process it, so it returned an error response";
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("%s: %s ", errorMsg, e.getMessage()))
          .build();
    } catch (SdkClientException e) {
      String errorMsg =
          "Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3";
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("%s: %s ", errorMsg, e.getMessage()))
          .build();
    } catch (IOException e) {
      String errorMsg = "Couldn't convert input stream to a byte array";
      return Response.status(500)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity(String.format("%s: %s ", errorMsg, e.getMessage()))
          .build();
    } catch (Exception e) {
      return Response.status(400).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
    }

    JSONObject obj = new JSONObject();
    obj.put("imgPath", imgPath);
    obj.put("id", entityId);

    // image/gif, image/jpg, image/jpeg, image/png,
    return Response.status(200)
        .entity(obj.toString())
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }
}
