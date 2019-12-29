package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;

@ApiModel(description="The location of a Tarrie entity. Its OK if this object is empty because the event could be virtual")
public class Location {
    @ApiModelProperty(notes = "The state of the location ", example = "CA", position = 0)
    private String state;

    @ApiModelProperty(notes = "The city  location ", example = "Redlands", position = 1)
    private String city;

    @ApiModelProperty(notes = "The country", example = "USA", position = 2)
    private String country = "USA";

    @Size(min=0, max=30)
    @ApiModelProperty(notes="The location name", example="Northwestern University", position=3)
    private String locName;

    @ApiModelProperty(notes="The latitude of the location", example="34.055560", position=4)
    private float latitude;

    @ApiModelProperty(notes="The longitude of the location", example="-117.182600", position=5)
    private float longitude;

}
