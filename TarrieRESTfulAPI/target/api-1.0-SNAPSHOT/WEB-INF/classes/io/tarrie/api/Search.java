package io.tarrie.api;

import io.swagger.annotations.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Api(tags="/search")
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "Write description here")
})
@Path("/search")
public class Search {

    /** when testing, this is reachable at http://localhost:8080/api/search?query=hello */
    @GET
    @ApiOperation(
            value = "Lists all employees",
            notes = "Lists all employees"
    )
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of employees"),
            @ApiResponse(code = 404, message = "Employee records not found"),
            @ApiResponse(code = 500, message = "Internal servererror")
    })
    public Response getMsg(@QueryParam("query") String q) throws IOException {
        JSONArray results = new JSONArray();
        results.put("hello world!");
        results.put(q);
        return Response.status(200).type("application/json").entity(results.toString(4))
                // below header is for CORS
                .header("Access-Control-Allow-Origin", "*").build();
    }
}
